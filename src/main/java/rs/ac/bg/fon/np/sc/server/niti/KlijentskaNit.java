/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.fon.np.sc.server.niti;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.ac.bg.fon.np.sc.commonLib.dto.SkiCentarDto;
import rs.ac.bg.fon.np.sc.commonLib.domen.Kupac;
import rs.ac.bg.fon.np.sc.commonLib.domen.Korisnik;
import rs.ac.bg.fon.np.sc.commonLib.domen.OpstiDomenskiObjekat;
import rs.ac.bg.fon.np.sc.commonLib.domen.SkiCentar;
import rs.ac.bg.fon.np.sc.commonLib.domen.SkiKarta;
import rs.ac.bg.fon.np.sc.commonLib.domen.SkiPas;
import rs.ac.bg.fon.np.sc.commonLib.domen.Staza;
import rs.ac.bg.fon.np.sc.commonLib.domen.Zicara;
import rs.ac.bg.fon.np.sc.commonLib.komunikacija.Operacije;
import rs.ac.bg.fon.np.sc.commonLib.komunikacija.Posiljalac;
import rs.ac.bg.fon.np.sc.commonLib.komunikacija.Primalac;
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
                String zahtev = (String) new Primalac(socket).primi();
                obradiZahtev(zahtev);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        Kontroler.getInstanca().odjaviKorisnika(trenutniKorisnik);
    }

    void zaustavi() {
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(KlijentskaNit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void obradiZahtev(String zahtev) {
        Gson gson = new Gson();
        JsonElement element = JsonParser.parseString(zahtev);
        JsonObject odgovor = null;
        switch (element.getAsJsonObject().get("operacija").getAsInt()) {
            case Operacije.PRIJAVI_SE:
                odgovor = prijaviSe(element.getAsJsonObject().get("parametar").getAsJsonObject());
                break;
            case Operacije.UCITAJ_LISTU_SKI_CENTARA:
                odgovor = ucitajListuSkiCentara();
                break;
            case Operacije.ZAPAMTI_SKI_KARTU:
                odgovor = zapamtiSkiKartu(element.getAsJsonObject().get("parametar").getAsJsonObject());
                break;
            case Operacije.PRETRAZI_SKI_KARTE:
                odgovor = pretraziSkiKarte(element.getAsJsonObject().get("parametar").getAsJsonObject());
                break;
            case Operacije.ZAPAMTI_STAZU:
                odgovor = zapamtiStazu(element.getAsJsonObject().get("parametar").getAsJsonObject());
                break;
            case Operacije.PRETRAZI_STAZE:
                odgovor = pretraziStaze(element.getAsJsonObject().get("parametar").getAsJsonObject());
                break;
            case Operacije.UCITAJ_STAZU:
                odgovor = ucitajStazu(element.getAsJsonObject().get("parametar").getAsJsonObject());
                break;
            case Operacije.PROMENI_STAZU:
                odgovor = promeniStazu(element.getAsJsonObject().get("parametar").getAsJsonObject());
                break;
            case Operacije.ZAPAMTI_ZICARU:
                odgovor = zapamtiZicaru(element.getAsJsonObject().get("parametar").getAsJsonObject());
                break;
            case Operacije.ZAPAMTI_SKI_CENTAR:
                odgovor = zapamtiSkiCentar(element.getAsJsonObject().get("parametar").getAsJsonObject());
                break;
            case Operacije.PRETRAZI_SKI_CENTAR:
                odgovor = pretarziSkiCentar(element.getAsJsonObject().get("parametar").getAsJsonObject());
                break;
            case Operacije.PROMENI_SKI_CENTAR:
                odgovor = promeniSkiCentar(element.getAsJsonObject().get("parametar").getAsJsonObject());
                break;
            case Operacije.UCITAJ_LISTU_SKI_KARATA:
                odgovor = ucitajListuSkiKarata();
                break;
            case Operacije.ZAPAMTI_SKI_PAS:
                odgovor = zapamtiSkiPas(element.getAsJsonObject().get("parametar").getAsJsonObject());
                break;
            case Operacije.PRETRAZI_SKI_PASOVE:
                odgovor = pretraziSkiPasove(element.getAsJsonObject().get("parametar").getAsJsonObject());
                break;
            case Operacije.UCITAJ_SKI_PAS:
                odgovor = ucitajSkiPas(element.getAsJsonObject().get("parametar").getAsJsonObject());
                break;
            case Operacije.PROMENI_SKI_PAS:
                odgovor = promeniSkiPas(element.getAsJsonObject().get("parametar").getAsJsonObject());
                break;
            case Operacije.UCITAJ_LISTU_KUPACA:
                odgovor = ucitajListuKupaca();
                break;
            case Operacije.ZAPAMTI_KUPCA:
                odgovor = zapamtiKupca(element.getAsJsonObject().get("parametar").getAsJsonObject());
                break;
            case Operacije.UCITAJ_SKI_CENTAR_DETALJNIJE:
                odgovor = ucitajSkiCentarDetaljnije(element.getAsJsonObject().get("parametar").getAsJsonObject());
                break;
            case Operacije.ZAPAMTI_SKI_CENTAR_DETALJNIJE:
                odgovor = zapamtiSkiCentarDetaljnije(element.getAsJsonObject().get("parametar").getAsJsonObject());
                break;
            default:
                throw new AssertionError();
        }
        String jsonString = gson.toJson(odgovor);
        new Posiljalac(socket).posalji(jsonString);
    }

    private JsonObject prijaviSe(JsonObject parametar) {
        JsonObject obj = new JsonObject();
        Gson gson = new Gson();
        Korisnik korisnik = gson.fromJson(parametar, Korisnik.class);
        try {
            korisnik = (Korisnik) Kontroler.getInstanca().prijaviSe(korisnik);
            obj = new JsonObject();
            obj.add("rezultat", gson.toJsonTree(korisnik));
            trenutniKorisnik = korisnik;
            Kontroler.getInstanca().dodajKorisnikaUTabelu(trenutniKorisnik);
            obj.addProperty("uspesno", true);
        } catch (Exception ex) {
            ex.printStackTrace();
            obj.addProperty("uspesno", false);
            obj.addProperty("exception", ex.getMessage());
        }
        return obj;
    }

    private JsonObject ucitajListuSkiCentara() {
        Gson gson = new Gson();
        JsonObject obj = new JsonObject();
        try {
            List<OpstiDomenskiObjekat> lista = Kontroler.getInstanca().ucitajListuSkiCentara();
            obj = new JsonObject();
            obj.add("rezultat", gson.toJsonTree(lista));
            obj.addProperty("uspesno", true);
        } catch (Exception ex) {
            obj.addProperty("uspesno", false);
            obj.addProperty("exception", ex.getMessage());
        }
        return obj;
    }

    private JsonObject zapamtiSkiKartu(JsonObject jsonSkiKarta) {
        Gson gson = new Gson();
        JsonObject obj = new JsonObject();
        SkiKarta skiKarta = gson.fromJson(jsonSkiKarta, SkiKarta.class);
        try {
            Kontroler.getInstanca().zapamtiSkiKartuSO(skiKarta);
            obj.add("rezultat", gson.toJsonTree(skiKarta));
            obj.addProperty("uspesno", true);
        } catch (Exception ex) {
            obj.addProperty("uspesno", false);
            obj.addProperty("exception", ex.getMessage());
        }
        return obj;
    }

    private JsonObject pretraziSkiKarte(JsonObject parametar) {
        Gson gson = new Gson();
        SkiKarta skiKarta = gson.fromJson(parametar, SkiKarta.class);
        JsonObject obj = new JsonObject();
        try {
            List<OpstiDomenskiObjekat> lista = Kontroler.getInstanca().pretraziSkiKarte(skiKarta);
            obj.add("rezultat", gson.toJsonTree(lista));
            obj.addProperty("uspesno", true);
        } catch (Exception ex) {
            obj.addProperty("uspesno", false);
            obj.addProperty("exception", ex.getMessage());
        }
        return obj;
    }

    private JsonObject zapamtiStazu(JsonObject parametar) {
        Gson gson = new Gson();
        Staza staza = new Gson().fromJson(parametar, Staza.class);
        JsonObject obj = new JsonObject();
        try {
            Kontroler.getInstanca().zapamtiStazu(staza);
            obj.add("rezultat", gson.toJsonTree(staza));

            obj.addProperty("uspesno", true);
        } catch (Exception ex) {
            obj.addProperty("uspesno", false);
            obj.addProperty("exception", ex.getMessage());
        }
        return obj;
    }

    private JsonObject pretraziStaze(JsonObject parametar) {
        Gson gson = new Gson();

        SkiCentar skiCentar = gson.fromJson(parametar, SkiCentar.class);
        Staza staza = new Staza();
        staza.setSkiCentar(skiCentar);
        JsonObject obj = new JsonObject();
        try {
            List<OpstiDomenskiObjekat> lista = Kontroler.getInstanca().pretraziStaze(staza);
            obj.add("rezultat", gson.toJsonTree(lista));

            obj.addProperty("uspesno", true);
        } catch (Exception ex) {
            obj.addProperty("uspesno", false);
            obj.addProperty("exception", ex.getMessage());
        }
        return obj;
    }

    private JsonObject ucitajStazu(JsonObject parametar) {
        Gson gson = new Gson();

        Staza staza = new Gson().fromJson(parametar, Staza.class);
        JsonObject obj = new JsonObject();
        try {
            staza = (Staza) Kontroler.getInstanca().ucitajStazu(staza);
            obj.add("rezultat", gson.toJsonTree(staza));

            obj.addProperty("uspesno", true);
        } catch (Exception ex) {
            obj.addProperty("uspesno", false);
            obj.addProperty("exception", ex.getMessage());
        }
        return obj;
    }

    private JsonObject promeniStazu(JsonObject parametar) {
        Gson gson = new Gson();

        Staza staza = new Gson().fromJson(parametar, Staza.class);
        JsonObject obj = new JsonObject();
        try {
            Kontroler.getInstanca().promeniStazu(staza);
            obj.add("rezultat", gson.toJsonTree(staza));

            obj.addProperty("uspesno", true);
        } catch (Exception ex) {
            obj.addProperty("uspesno", false);
            obj.addProperty("exception", ex.getMessage());
        }
        return obj;
    }

    private JsonObject zapamtiZicaru(JsonObject parametar) {
        Gson gson = new Gson();

        Zicara zicara = new Gson().fromJson(parametar, Zicara.class);
        JsonObject obj = new JsonObject();
        try {
            Kontroler.getInstanca().zapamtiZicaru(zicara);
            obj.add("rezultat", gson.toJsonTree(zicara));

            obj.addProperty("uspesno", true);
        } catch (Exception ex) {
            obj.addProperty("uspesno", false);
            obj.addProperty("exception", ex.getMessage());
        }
        return obj;
    }

    private JsonObject zapamtiSkiCentar(JsonObject parametar) {
        Gson gson = new Gson();

        SkiCentar skiCentar = new Gson().fromJson(parametar, SkiCentar.class);
        JsonObject obj = new JsonObject();
        try {
            Kontroler.getInstanca().zapamtiSkiCentar(skiCentar);
            obj.add("rezultat", gson.toJsonTree(skiCentar));

            obj.addProperty("uspesno", true);
        } catch (Exception ex) {
            obj.addProperty("uspesno", false);
            obj.addProperty("exception", ex.getMessage());
        }
        return obj;
    }

    private JsonObject pretarziSkiCentar(JsonObject parametar) {
        Gson gson = new Gson();

        SkiCentar skiCentar = gson.fromJson(parametar, SkiCentar.class);
        JsonObject obj = new JsonObject();
        try {
            skiCentar = (SkiCentar) Kontroler.getInstanca().pretraziSkiCentar(skiCentar);
            obj.add("rezultat", gson.toJsonTree(skiCentar));

            obj.addProperty("uspesno", true);
        } catch (Exception ex) {
            obj.addProperty("uspesno", false);
            obj.addProperty("exception", ex.getMessage());
        }
        return obj;
    }

    private JsonObject promeniSkiCentar(JsonObject parametar) {
        Gson gson = new Gson();

        SkiCentar skiCentar = new Gson().fromJson(parametar, SkiCentar.class);
        JsonObject obj = new JsonObject();
        try {
            Kontroler.getInstanca().promeniSkiCentar(skiCentar);
            obj.add("rezultat", gson.toJsonTree(skiCentar));

            obj.addProperty("uspesno", true);
        } catch (Exception ex) {
            obj.addProperty("uspesno", false);
            obj.addProperty("exception", ex.getMessage());
        }
        return obj;
    }

    private JsonObject ucitajListuSkiKarata() {
        Gson gson = new Gson();
        JsonObject obj = new JsonObject();
        try {
            List<OpstiDomenskiObjekat> lista = Kontroler.getInstanca().ucitajListuSkiKarata();
            obj.add("rezultat", gson.toJsonTree(lista));
            obj.addProperty("uspesno", true);
        } catch (Exception ex) {
            obj.addProperty("uspesno", false);
            obj.addProperty("exception", ex.getMessage());
        }
        return obj;
    }

    private JsonObject zapamtiSkiPas(JsonObject parametar) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        SkiPas skiPas = new Gson().fromJson(parametar, SkiPas.class);
        JsonObject obj = new JsonObject();
        try {
            Kontroler.getInstanca().zapamtiSkiPas(skiPas);
            obj.add("rezultat", gson.toJsonTree(skiPas));
            obj.addProperty("uspesno", true);
        } catch (Exception ex) {
            obj.addProperty("uspesno", false);
            obj.addProperty("exception", ex.getMessage());
        }
        return obj;
    }

    private JsonObject pretraziSkiPasove(JsonObject parametar) {
        Gson gson = new Gson();
        Kupac kupac = gson.fromJson(parametar, Kupac.class);
        SkiPas skiPas = new SkiPas();
        skiPas.setKupac(kupac);
        JsonObject obj = new JsonObject();
        try {
            List<OpstiDomenskiObjekat> lista = Kontroler.getInstanca().pretraziSkiPasove(skiPas);
            obj.add("rezultat", gson.toJsonTree(lista));
            obj.addProperty("uspesno", true);
        } catch (Exception ex) {
            obj.addProperty("uspesno", false);
            obj.addProperty("exception", ex.getMessage());
        }
        return obj;
    }

    private JsonObject ucitajSkiPas(JsonObject parametar) {
        GsonBuilder gsonBuilder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        Gson gson = gsonBuilder.create();

        SkiPas skiPas = gson.fromJson(parametar, SkiPas.class);
        JsonObject obj = new JsonObject();
        try {
            skiPas = (SkiPas) Kontroler.getInstanca().ucitajSkiPas(skiPas);
            gson = gsonBuilder.setDateFormat("yyyy-MM-dd").excludeFieldsWithoutExposeAnnotation().create();
            obj.add("rezultat", gson.toJsonTree(skiPas));
            obj.addProperty("uspesno", true);
        } catch (Exception ex) {
            obj.addProperty("uspesno", false);
            obj.addProperty("exception", ex.getMessage());
        }
        return obj;
    }

    private JsonObject promeniSkiPas(JsonObject parametar) {
        GsonBuilder gsonBuilder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        Gson gson = gsonBuilder.create();

        SkiPas skiPas = new Gson().fromJson(parametar, SkiPas.class);
        JsonObject obj = new JsonObject();
        try {
            Kontroler.getInstanca().promeniSkiPas(skiPas);
            gson = gsonBuilder.setDateFormat("MMM dd, yyyy").excludeFieldsWithoutExposeAnnotation().create();
            obj.add("rezultat", gson.toJsonTree(skiPas));
            obj.addProperty("uspesno", true);
        } catch (Exception ex) {
            obj.addProperty("uspesno", false);
            obj.addProperty("exception", ex.getMessage());
        }
        return obj;
    }

    private JsonObject ucitajListuKupaca() {
        Gson gson = new Gson();
        JsonObject obj = new JsonObject();
        try {
            List<OpstiDomenskiObjekat> lista = Kontroler.getInstanca().ucitajListuKupaca();
            obj.add("rezultat", gson.toJsonTree(lista));
            obj.addProperty("uspesno", true);
        } catch (Exception ex) {
            obj.addProperty("uspesno", false);
            obj.addProperty("exception", ex.getMessage());
        }
        return obj;
    }

    private JsonObject zapamtiKupca(JsonObject parametar) {
        Gson gson = new Gson();
        Kupac kupac = new Gson().fromJson(parametar, Kupac.class);
        JsonObject obj = new JsonObject();
        try {
            Kontroler.getInstanca().zapamtiKupca(kupac);
            obj.add("rezultat", gson.toJsonTree(kupac));
            obj.addProperty("uspesno", true);
        } catch (Exception ex) {
            obj.addProperty("uspesno", false);
            obj.addProperty("exception", ex.getMessage());
        }
        return obj;
    }

    private JsonObject ucitajSkiCentarDetaljnije(JsonObject parametar) {
        Gson gson = new Gson();

        SkiCentarDto skiCentarDto = new Gson().fromJson(parametar, SkiCentarDto.class);
        JsonObject obj = new JsonObject();
        try {
            SkiCentarDto dto = Kontroler.getInstanca().ucitajSkiCentar(skiCentarDto);
            obj.add("rezultat", gson.toJsonTree(dto));
            obj.addProperty("uspesno", true);
        } catch (Exception ex) {
            obj.addProperty("uspesno", false);
            obj.addProperty("exception", ex.getMessage());
        }
        return obj;
    }

    private JsonObject zapamtiSkiCentarDetaljnije(JsonObject parametar) {
        Gson gson = new Gson();
        SkiCentarDto skiCentarDto = new Gson().fromJson(parametar, SkiCentarDto.class);
        JsonObject obj = new JsonObject();
        try {
            Kontroler.getInstanca().zapamtiSkiCentarDet(skiCentarDto);
            obj.add("rezultat", gson.toJsonTree(skiCentarDto));
            obj.addProperty("uspesno", true);
        } catch (Exception ex) {
            obj.addProperty("uspesno", false);
            obj.addProperty("exception", ex.getMessage());
        }
        return obj;
    }

}
