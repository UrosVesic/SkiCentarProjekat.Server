/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.fon.np.sc.server.niti;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.ac.bg.fon.np.sc.commonLib.domen.Kupac;
import rs.ac.bg.fon.np.sc.commonlib.domen.Korisnik;
import rs.ac.bg.fon.np.sc.commonlib.domen.OpstiDomenskiObjekat;
import rs.ac.bg.fon.np.sc.commonlib.domen.SkiCentar;
import rs.ac.bg.fon.np.sc.commonlib.domen.SkiKarta;
import rs.ac.bg.fon.np.sc.commonlib.domen.SkiPas;
import rs.ac.bg.fon.np.sc.commonlib.domen.Staza;
import rs.ac.bg.fon.np.sc.commonlib.domen.Zicara;
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
            case Operacije.ZAPAMTI_SKI_KARTU:
                odgovor = zapamtiSkiKartu(zahtev);
                break;
            case Operacije.PRETRAZI_SKI_KARTE:
                odgovor = pretraziSkiKarte(zahtev);
                break;
            case Operacije.ZAPAMTI_STAZU:
                odgovor = zapamtiStazu(zahtev);
                break;
            case Operacije.PRETRAZI_STAZE:
                odgovor = pretraziStaze(zahtev);
                break;
            case Operacije.UCITAJ_STAZU:
                odgovor = ucitajStazu(zahtev);
                break;
            case Operacije.PROMENI_STAZU:
                odgovor = promeniStazu(zahtev);
                break;
            case Operacije.ZAPAMTI_ZICARU:
                odgovor = zapamtiZicaru(zahtev);
                break;
            case Operacije.ZAPAMTI_SKI_CENTAR:
                odgovor = zapamtiSkiCentar(zahtev);
                break;
            case Operacije.PRETRAZI_SKI_CENTAR:
                odgovor = pretarziSkiCentar(zahtev);
                break;
            case Operacije.PROMENI_SKI_CENTAR:
                odgovor = promeniSkiCentar(zahtev);
                break;
            case Operacije.UCITAJ_LISTU_SKI_KARATA:
                odgovor = ucitajListuSkiKarata(zahtev);
                break;
            case Operacije.ZAPAMTI_SKI_PAS:
                odgovor = zapamtiSkiPas(zahtev);
                break;
            case Operacije.PRETRAZI_SKI_PASOVE:
                odgovor = pretraziSkiPasove(zahtev);
                break;
            case Operacije.UCITAJ_SKI_PAS:
                odgovor = ucitajSkiPas(zahtev);
                break;
            case Operacije.PROMENI_SKI_PAS:
                odgovor = promeniSkiPas(zahtev);
                break;
            case Operacije.UCITAJ_LISTU_KUPACA:
                odgovor = ucitajListuKupaca(zahtev);
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

    private Odgovor zapamtiSkiKartu(Zahtev zahtev) {
        Gson gson = new Gson();
        String objekat = zahtev.getParametar();
        SkiKarta skiKarta = new Gson().fromJson(objekat, SkiKarta.class);
        Odgovor odgovor = new Odgovor();
        try {
            Kontroler.getInstanca().zapamtiSkiKartuSO(skiKarta);
            objekat = new Gson().toJson(skiKarta);
            odgovor.setRezultat(objekat);
            odgovor.setUspesno(true);
        } catch (Exception ex) {
            odgovor.setUspesno(false);
            odgovor.setException(ex);
        }
        return odgovor;
    }

    private Odgovor pretraziSkiKarte(Zahtev zahtev) {
        Gson gson = new Gson();
        String objekat = zahtev.getParametar();
        SkiKarta skiKarta = gson.fromJson(objekat, SkiKarta.class);
        Odgovor odgovor = new Odgovor();
        try {
            List<OpstiDomenskiObjekat> lista = Kontroler.getInstanca().pretraziSkiKarte(skiKarta);
            objekat = new Gson().toJson(lista);
            odgovor.setRezultat(objekat);
            odgovor.setUspesno(true);
        } catch (Exception ex) {
            odgovor.setUspesno(false);
            odgovor.setException(ex);
        }
        return odgovor;
    }

    private Odgovor zapamtiStazu(Zahtev zahtev) {
        Gson gson = new Gson();
        String objekat = zahtev.getParametar();
        Staza staza = new Gson().fromJson(objekat, Staza.class);
        Odgovor odgovor = new Odgovor();
        try {
            Kontroler.getInstanca().zapamtiStazu(staza);
            objekat = new Gson().toJson(staza);
            odgovor.setRezultat(objekat);
            odgovor.setUspesno(true);
        } catch (Exception ex) {
            odgovor.setUspesno(false);
            odgovor.setException(ex);
        }
        return odgovor;
    }

    private Odgovor pretraziStaze(Zahtev zahtev) {
        Gson gson = new Gson();
        String objekat = zahtev.getParametar();
        SkiCentar skiCentar = gson.fromJson(objekat, SkiCentar.class);
        Staza staza = new Staza();
        staza.setSkiCentar(skiCentar);
        Odgovor odgovor = new Odgovor();
        try {
            List<OpstiDomenskiObjekat> lista = Kontroler.getInstanca().pretraziStaze(staza);
            objekat = new Gson().toJson(lista);
            odgovor.setRezultat(objekat);
            odgovor.setUspesno(true);
        } catch (Exception ex) {
            odgovor.setUspesno(false);
            odgovor.setException(ex);
        }
        return odgovor;
    }

    private Odgovor ucitajStazu(Zahtev zahtev) {
        Gson gson = new Gson();
        String objekat = zahtev.getParametar();
        Staza staza = new Gson().fromJson(objekat, Staza.class);
        Odgovor odgovor = new Odgovor();
        try {
            Kontroler.getInstanca().ucitajStazu(staza);
            objekat = new Gson().toJson(staza);
            odgovor.setRezultat(objekat);
            odgovor.setUspesno(true);
        } catch (Exception ex) {
            odgovor.setUspesno(false);
            odgovor.setException(ex);
        }
        return odgovor;
    }

    private Odgovor promeniStazu(Zahtev zahtev) {
        Gson gson = new Gson();
        String objekat = zahtev.getParametar();
        Staza staza = new Gson().fromJson(objekat, Staza.class);
        Odgovor odgovor = new Odgovor();
        try {
            Kontroler.getInstanca().promeniStazu(staza);
            objekat = new Gson().toJson(staza);
            odgovor.setRezultat(objekat);
            odgovor.setUspesno(true);
        } catch (Exception ex) {
            odgovor.setUspesno(false);
            odgovor.setException(ex);
        }
        return odgovor;
    }

    private Odgovor zapamtiZicaru(Zahtev zahtev) {
        Gson gson = new Gson();
        String objekat = zahtev.getParametar();
        Zicara zicara = new Gson().fromJson(objekat, Zicara.class);
        Odgovor odgovor = new Odgovor();
        try {
            Kontroler.getInstanca().zapamtiZicaru(zicara);
            objekat = new Gson().toJson(zicara);
            odgovor.setRezultat(objekat);
            odgovor.setUspesno(true);
        } catch (Exception ex) {
            odgovor.setUspesno(false);
            odgovor.setException(ex);
        }
        return odgovor;
    }

    private Odgovor zapamtiSkiCentar(Zahtev zahtev) {
        Gson gson = new Gson();
        String objekat = zahtev.getParametar();
        SkiCentar skiCentar = new Gson().fromJson(objekat, SkiCentar.class);
        Odgovor odgovor = new Odgovor();
        try {
            Kontroler.getInstanca().zapamtiSkiCentar(skiCentar);
            objekat = new Gson().toJson(skiCentar);
            odgovor.setRezultat(objekat);
            odgovor.setUspesno(true);
        } catch (Exception ex) {
            odgovor.setUspesno(false);
            odgovor.setException(ex);
        }
        return odgovor;
    }

    private Odgovor pretarziSkiCentar(Zahtev zahtev) {
        Gson gson = new Gson();
        String objekat = zahtev.getParametar();
        SkiCentar skiCentar = gson.fromJson(objekat, SkiCentar.class);
        Odgovor odgovor = new Odgovor();
        try {
            Kontroler.getInstanca().pretraziSkiCentar(skiCentar);
            objekat = new Gson().toJson(skiCentar);
            odgovor.setRezultat(objekat);
            odgovor.setUspesno(true);
        } catch (Exception ex) {
            odgovor.setUspesno(false);
            odgovor.setException(ex);
        }
        return odgovor;
    }

    private Odgovor promeniSkiCentar(Zahtev zahtev) {
        Gson gson = new Gson();
        String objekat = zahtev.getParametar();
        SkiCentar skiCentar = new Gson().fromJson(objekat, SkiCentar.class);
        Odgovor odgovor = new Odgovor();
        try {
            Kontroler.getInstanca().promeniSkiCentar(skiCentar);
            objekat = new Gson().toJson(skiCentar);
            odgovor.setRezultat(objekat);
            odgovor.setUspesno(true);
        } catch (Exception ex) {
            odgovor.setUspesno(false);
            odgovor.setException(ex);
        }
        return odgovor;
    }

    private Odgovor ucitajListuSkiKarata(Zahtev zahtev) {
        String rezultat;
        Odgovor odgovor = new Odgovor();
        try {
            List<OpstiDomenskiObjekat> lista = Kontroler.getInstanca().ucitajListuSkiKarata();
            rezultat = new Gson().toJson(lista);
            odgovor.setRezultat(rezultat);
            odgovor.setUspesno(true);
        } catch (Exception ex) {
            odgovor.setUspesno(false);
            odgovor.setException(ex);
        }
        return odgovor;
    }

    private Odgovor zapamtiSkiPas(Zahtev zahtev) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String objekat = zahtev.getParametar();
        SkiPas skiPas = new Gson().fromJson(objekat, SkiPas.class);
        Odgovor odgovor = new Odgovor();
        try {
            Kontroler.getInstanca().zapamtiSkiPas(skiPas);
            objekat = gson.toJson(skiPas);
            odgovor.setRezultat(objekat);
            odgovor.setUspesno(true);
        } catch (Exception ex) {
            odgovor.setUspesno(false);
            odgovor.setException(ex);
        }
        return odgovor;
    }

    private Odgovor pretraziSkiPasove(Zahtev zahtev) {
        Gson gson = new Gson();
        String objekat = zahtev.getParametar();
        Kupac kupac = gson.fromJson(objekat, Kupac.class);
        SkiPas skiPas = new SkiPas();
        skiPas.setKupac(kupac);
        Odgovor odgovor = new Odgovor();
        try {
            List<OpstiDomenskiObjekat> lista = Kontroler.getInstanca().pretraziSkiPasove(skiPas);
            objekat = new Gson().toJson(lista);
            odgovor.setRezultat(objekat);
            odgovor.setUspesno(true);
        } catch (Exception ex) {
            odgovor.setUspesno(false);
            odgovor.setException(ex);
        }
        return odgovor;
    }

    private Odgovor ucitajSkiPas(Zahtev zahtev) {
        GsonBuilder gsonBuilder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        Gson gson = gsonBuilder.create();
        String objekat = zahtev.getParametar();
        SkiPas skiPas = gson.fromJson(objekat, SkiPas.class);
        Odgovor odgovor = new Odgovor();
        try {
            Kontroler.getInstanca().ucitajSkiPas(skiPas);
            gson = gsonBuilder.setDateFormat("yyyy-MM-dd").excludeFieldsWithoutExposeAnnotation().create();
            objekat = gson.toJson(skiPas);
            odgovor.setRezultat(objekat);
            odgovor.setUspesno(true);
        } catch (Exception ex) {
            odgovor.setUspesno(false);
            odgovor.setException(ex);
        }
        return odgovor;
    }

    public Odgovor promeniSkiPas(Zahtev zahtev) {
        GsonBuilder gsonBuilder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        Gson gson = gsonBuilder.create();
        String objekat = zahtev.getParametar();
        SkiPas skiPas = new Gson().fromJson(objekat, SkiPas.class);
        Odgovor odgovor = new Odgovor();
        try {
            Kontroler.getInstanca().promeniSkiPas(skiPas);
            gson = gsonBuilder.setDateFormat("MMM dd, yyyy").excludeFieldsWithoutExposeAnnotation().create();
            objekat = gson.toJson(skiPas);
            odgovor.setRezultat(objekat);
            odgovor.setUspesno(true);
        } catch (Exception ex) {
            odgovor.setUspesno(false);
            odgovor.setException(ex);
        }
        return odgovor;
    }

    private Odgovor ucitajListuKupaca(Zahtev zahtev) {
        String rezultat;
        Odgovor odgovor = new Odgovor();
        try {
            List<OpstiDomenskiObjekat> lista = Kontroler.getInstanca().ucitajListuKupaca();
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
