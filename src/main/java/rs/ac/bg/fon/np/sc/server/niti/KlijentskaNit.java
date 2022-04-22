/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.fon.np.sc.server.niti;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.ac.bg.fon.np.sc.commonlib.domen.Korisnik;
import rs.ac.bg.fon.np.sc.commonlib.domen.OpstiDomenskiObjekat;
import rs.ac.bg.fon.np.sc.commonlib.komunikacija.Odgovor;
import rs.ac.bg.fon.np.sc.commonlib.komunikacija.Operacije;
import rs.ac.bg.fon.np.sc.commonlib.komunikacija.Posiljalac;
import rs.ac.bg.fon.np.sc.commonlib.komunikacija.Primalac;
import rs.ac.bg.fon.np.sc.commonlib.komunikacija.Zahtev;
import rs.ac.bg.fon.np.sc.server.kontroler.Kontroler;

/**
 *
 * @author UrosVesic
 */
public class KlijentskaNit extends Thread {

    private Socket socket;
    private ServerskaNit serverskaNit;
    private Korisnik trenutniKorisnik;

    public KlijentskaNit(Socket socket, ServerskaNit serverskaNit) {
        this.socket = socket;
        this.serverskaNit = serverskaNit;
    }

    @Override
    public void run() {
        while (!socket.isClosed()) {
            try {
                Zahtev zahtev = (Zahtev) new Primalac(socket).primi();
                obradiZahtev(zahtev);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        serverskaNit.obavestiOOdjavljivanju(this);
    }

    void zaustavi() {
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(KlijentskaNit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void obradiZahtev(Zahtev zahtev) {
        Odgovor odgovor = new Odgovor();
        switch (zahtev.getOperacija()) {
            case Operacije.PRIJAVI_SE:
                odgovor = prijaviSe(zahtev);
                break;
            case Operacije.UCITAJ_LISTU_SKI_CENTARA:
                odgovor = ucitajListuSkiCentara(zahtev);
                break;
            default:
                throw new AssertionError();
        }

        new Posiljalac(socket).posalji(odgovor);
    }

    private Odgovor prijaviSe(Zahtev zahtev) {
        String objekat = zahtev.getParametar();
        Korisnik korisnik = new Gson().fromJson(objekat, Korisnik.class);
        Odgovor odgovor = new Odgovor();
        try {
            Kontroler.getInstanca().prijaviSe(korisnik);
            objekat = new Gson().toJson(korisnik);
            odgovor.setRezultat(objekat);
            trenutniKorisnik = korisnik;
            Kontroler.getInstanca().dodajKorisnikaUTabelu(trenutniKorisnik);
            odgovor.setUspesno(true);
        } catch (Exception ex) {
            odgovor.setUspesno(false);
            odgovor.setException(ex);
        }
        return odgovor;
    }

    private Odgovor ucitajListuSkiCentara(Zahtev zahtev) {
        String rezultat;
        Odgovor odgovor = new Odgovor();
        try {
            List<OpstiDomenskiObjekat> lista = Kontroler.getInstanca().ucitajListuSkiCentara();
            rezultat = new Gson().toJson(lista);
            odgovor.setRezultat(rezultat);
            odgovor.setUspesno(true);
        } catch (Exception ex) {
            odgovor.setUspesno(false);
            odgovor.setException(ex);
        }
        return odgovor;
    }

}
