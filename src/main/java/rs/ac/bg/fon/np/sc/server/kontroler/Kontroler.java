/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.fon.np.sc.server.kontroler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.List;
import rs.ac.bg.fon.np.sc.commonLib.dto.SkiCentarDto;
import rs.ac.bg.fon.np.sc.commonLib.domen.Kupac;
import rs.ac.bg.fon.np.sc.commonLib.domen.Korisnik;
import rs.ac.bg.fon.np.sc.commonLib.domen.OpstiDomenskiObjekat;
import rs.ac.bg.fon.np.sc.commonLib.domen.SkiCentar;
import rs.ac.bg.fon.np.sc.commonLib.domen.SkiKarta;
import rs.ac.bg.fon.np.sc.commonLib.domen.SkiPas;
import rs.ac.bg.fon.np.sc.commonLib.domen.Staza;
import rs.ac.bg.fon.np.sc.commonLib.domen.Zicara;
import rs.ac.bg.fon.np.sc.server.db.BrokerBP;
import rs.ac.bg.fon.np.sc.server.forme.ServerskaForma;
import rs.ac.bg.fon.np.sc.server.modelitabela.ModelTabeleKorisnik;
import rs.ac.bg.fon.np.sc.server.niti.ServerskaNit;
import rs.ac.bg.fon.np.sc.server.so.OpstaSO;
import rs.ac.bg.fon.np.sc.server.so.impl.PretraziSkiCentarSO;
import rs.ac.bg.fon.np.sc.server.so.impl.PretraziSkiKarteSO;
import rs.ac.bg.fon.np.sc.server.so.impl.PretraziSkiPasoveSO;
import rs.ac.bg.fon.np.sc.server.so.impl.PretraziStazeSO;
import rs.ac.bg.fon.np.sc.server.so.impl.PrijaviSeSO;
import rs.ac.bg.fon.np.sc.server.so.impl.PromeniSkiCentarSO;
import rs.ac.bg.fon.np.sc.server.so.impl.PromeniSkiPasSO;
import rs.ac.bg.fon.np.sc.server.so.impl.PromeniStazuSO;
import rs.ac.bg.fon.np.sc.server.so.impl.UcitajListuKupacaSO;
import rs.ac.bg.fon.np.sc.server.so.impl.UcitajListuSkiCentaraSO;
import rs.ac.bg.fon.np.sc.server.so.impl.UcitajListuSkiKarataSO;
import rs.ac.bg.fon.np.sc.server.so.impl.UcitajSkiCentarSO;
import rs.ac.bg.fon.np.sc.server.so.impl.UcitajSkiPasSO;
import rs.ac.bg.fon.np.sc.server.so.impl.UcitajStazuSO;
import rs.ac.bg.fon.np.sc.server.so.impl.ZapamtiKupcaSO;
import rs.ac.bg.fon.np.sc.server.so.impl.ZapamtiSkiCentarDetaljnije;
import rs.ac.bg.fon.np.sc.server.so.impl.ZapamtiSkiCentarSO;
import rs.ac.bg.fon.np.sc.server.so.impl.ZapamtiSkiKartuSO;
import rs.ac.bg.fon.np.sc.server.so.impl.ZapamtiSkiPasSO;
import rs.ac.bg.fon.np.sc.server.so.impl.ZapamtiStazuSO;
import rs.ac.bg.fon.np.sc.server.so.impl.ZapamtiZicaruSO;

/**
 * Klasa koja prosledjuje pozive sistemskih operacija do odgovarajucih klasa za
 * njihovo izvrsavanje
 *
 * @author UrosVesic
 */
public class Kontroler {

    private static Kontroler instanca;
    private ServerskaForma serverskaForma;
    private ServerskaNit serverskaNit;
    private final BrokerBP b;

    private Kontroler() {
        b = new BrokerBP();
    }

    public void setServerskaForma(ServerskaForma serverskaForma) {
        this.serverskaForma = serverskaForma;
    }

    public static Kontroler getInstanca() {
        if (instanca == null) {
            instanca = new Kontroler();
        }
        return instanca;
    }

    public void pripremiTabelu() {
        ModelTabeleKorisnik model = new ModelTabeleKorisnik();
        serverskaForma.getTblKorisnici().setModel(model);
    }

    public void pokreniServer() throws IOException {
        Gson gson = new Gson();
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try (InputStreamReader in = new InputStreamReader(classloader.getResourceAsStream("server/server.json"))) {
            JsonObject obj = gson.fromJson(in, JsonObject.class);
            int port = obj.get("port").getAsInt();
            ServerSocket serverSocket = new ServerSocket(9000);
            serverskaNit = new ServerskaNit(serverSocket);
            serverskaNit.start();
            serverskaForma.getBtnPokreni().setEnabled(false);
            serverskaForma.getBtnZaustavi().setEnabled(true);
            serverskaForma.getLblStatusServera().setText("Server je pokrenut");
            serverskaForma.getLblStatusServera().setForeground(Color.GREEN);
        } catch (FileNotFoundException ex) {
            System.out.println("Ne postoji fajl");
            ex.printStackTrace();
        }

    }

    public void zaustaviServer() throws IOException {
        serverskaNit.zaustavi();
        serverskaForma.getLblStatusServera().setText("Server je zaustavljen");
        serverskaForma.getLblStatusServera().setForeground(Color.red);
        serverskaForma.getBtnPokreni().setEnabled(true);
        serverskaForma.getBtnZaustavi().setEnabled(false);
    }

    public OpstiDomenskiObjekat prijaviSe(Korisnik korisnik) throws Exception {
        OpstaSO so = new PrijaviSeSO(b, korisnik);
        so.opsteIzvrsenjeSo();
        return so.getOdo();
    }

    public void dodajKorisnikaUTabelu(Korisnik trenutniKorisnik) {
        ModelTabeleKorisnik model = (ModelTabeleKorisnik) serverskaForma.getTblKorisnici().getModel();
        model.dodaj(trenutniKorisnik);
    }

    public List<OpstiDomenskiObjekat> ucitajListuSkiCentara() throws Exception {
        OpstaSO so = new UcitajListuSkiCentaraSO(b, new SkiCentar());
        so.opsteIzvrsenjeSo();
        return so.getLista();
    }

    public void zapamtiSkiKartuSO(SkiKarta skiKarta) throws Exception {
        OpstaSO so = new ZapamtiSkiKartuSO(b, skiKarta);
        so.opsteIzvrsenjeSo();
    }

    public List<OpstiDomenskiObjekat> pretraziSkiKarte(SkiKarta skiKarta) throws Exception {
        OpstaSO so = new PretraziSkiKarteSO(b, skiKarta);
        so.opsteIzvrsenjeSo();
        return so.getLista();
    }

    public void zapamtiStazu(Staza staza) throws Exception {
        OpstaSO so = new ZapamtiStazuSO(b, staza);
        so.opsteIzvrsenjeSo();
    }

    public List<OpstiDomenskiObjekat> pretraziStaze(Staza staza) throws Exception {
        OpstaSO so = new PretraziStazeSO(b, staza);
        so.opsteIzvrsenjeSo();
        return so.getLista();
    }

    public OpstiDomenskiObjekat ucitajStazu(Staza staza) throws Exception {
        OpstaSO so = new UcitajStazuSO(b, staza);
        so.opsteIzvrsenjeSo();
        return so.getOdo();
    }

    public void promeniStazu(Staza staza) throws Exception {
        OpstaSO so = new PromeniStazuSO(b, staza);
        so.opsteIzvrsenjeSo();
    }

    public void zapamtiZicaru(Zicara zicara) throws Exception {
        OpstaSO so = new ZapamtiZicaruSO(b, zicara);
        so.opsteIzvrsenjeSo();
    }

    public void zapamtiSkiCentar(SkiCentar skiCentar) throws Exception {
        OpstaSO so = new ZapamtiSkiCentarSO(b, skiCentar);
        so.opsteIzvrsenjeSo();
    }

    public OpstiDomenskiObjekat pretraziSkiCentar(SkiCentar skiCentar) throws Exception {
        OpstaSO so = new PretraziSkiCentarSO(b, skiCentar);
        so.opsteIzvrsenjeSo();
        return so.getOdo();
    }

    public void promeniSkiCentar(SkiCentar skiCentar) throws Exception {
        OpstaSO so = new PromeniSkiCentarSO(b, skiCentar);
        so.opsteIzvrsenjeSo();
    }

    public List<OpstiDomenskiObjekat> ucitajListuSkiKarata() throws Exception {
        OpstaSO so = new UcitajListuSkiKarataSO(b, new SkiKarta());
        so.opsteIzvrsenjeSo();
        return so.getLista();
    }

    public void zapamtiSkiPas(SkiPas skiPas) throws Exception {
        OpstaSO so = new ZapamtiSkiPasSO(b, skiPas);
        so.opsteIzvrsenjeSo();
    }

    public List<OpstiDomenskiObjekat> pretraziSkiPasove(SkiPas skiPas) throws Exception {
        OpstaSO so = new PretraziSkiPasoveSO(b, skiPas);
        so.opsteIzvrsenjeSo();
        return so.getLista();
    }

    public OpstiDomenskiObjekat ucitajSkiPas(SkiPas skiPas) throws Exception {
        OpstaSO so = new UcitajSkiPasSO(b, skiPas);
        so.opsteIzvrsenjeSo();
        return so.getOdo();
    }

    public void promeniSkiPas(SkiPas skiPas) throws Exception {
        OpstaSO so = new PromeniSkiPasSO(b, skiPas);
        so.opsteIzvrsenjeSo();
    }

    public List<OpstiDomenskiObjekat> ucitajListuKupaca() throws Exception {
        OpstaSO so = new UcitajListuKupacaSO(b, new Kupac());
        so.opsteIzvrsenjeSo();
        return so.getLista();
    }

    public void zapamtiKupca(Kupac kupac) throws Exception {
        OpstaSO so = new ZapamtiKupcaSO(b, kupac);
        so.opsteIzvrsenjeSo();
    }

    public void odjaviKorisnika(Korisnik trenutniKorisnik) {
        ModelTabeleKorisnik model = (ModelTabeleKorisnik) serverskaForma.getTblKorisnici().getModel();
        model.obrisi(trenutniKorisnik);
    }

    public SkiCentarDto ucitajSkiCentar(SkiCentarDto skiCentarDto) throws Exception {
        SkiCentar skiCentar = skiCentarDto.getSkiCentar();
        UcitajSkiCentarSO so = new UcitajSkiCentarSO(b, skiCentar);
        so.opsteIzvrsenjeSo();
        return so.getDto();
    }

    public void zapamtiSkiCentarDet(SkiCentarDto skiCentarDto) throws Exception {
        OpstaSO so = new ZapamtiSkiCentarDetaljnije(b, skiCentarDto);
        so.opsteIzvrsenjeSo();
    }
}
