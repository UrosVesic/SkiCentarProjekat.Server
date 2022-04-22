/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.fon.np.sc.server.kontroler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.List;
import rs.ac.bg.fon.np.sc.commonlib.domen.Korisnik;
import rs.ac.bg.fon.np.sc.commonlib.domen.OpstiDomenskiObjekat;
import rs.ac.bg.fon.np.sc.commonlib.domen.SkiCentar;
import rs.ac.bg.fon.np.sc.server.db.BrokerBP;
import rs.ac.bg.fon.np.sc.server.forme.ServerskaForma;
import rs.ac.bg.fon.np.sc.server.modelitabela.ModelTabeleKorisnik;
import rs.ac.bg.fon.np.sc.server.niti.ServerskaNit;
import rs.ac.bg.fon.np.sc.server.so.OpstaSO;
import rs.ac.bg.fon.np.sc.server.so.impl.PrijaviSeSO;
import rs.ac.bg.fon.np.sc.server.so.impl.UcitajListuSkiCentaraSO;

/**
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

    public void prijaviSe(Korisnik korisnik) throws Exception {
        OpstaSO so = new PrijaviSeSO(b, korisnik);
        so.opsteIzvrsenjeSo();
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
}
