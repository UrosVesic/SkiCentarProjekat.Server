/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.fon.np.sc.server.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import rs.ac.bg.fon.np.sc.commonlib.domen.OpstiDomenskiObjekat;

/**
 * Klasa zaduzena za interakciju sa bazom podataka i izvrsavanje CRUD operacija
 * @author UrosVesic
 */
public class BrokerBP {
    
    /**
     * Konekcija sa bazom podataka
     */
    private Connection konekcija;
    /**
     * Uspostavlja konekciju sa bazom podataka, instancira objekat konekcija.
     * @throws Exception  ako nije moguce uspostaviti konekciju sa bazom podataka
     */
    public void uspostaviKonekciju() throws Exception {
        DbFabrikaKonekcije.getInstanca().getKonekcija();
    }
    /**
     * Raskida konekciju sa bazom podataka
     * @throws Exception ako nije moguce zatvoriti konekciju sa bazom podataka
     */
    public void raskiniKonekciju() throws Exception {
        DbFabrikaKonekcije.getInstanca().getKonekcija().close();
    }
    /**
     * Potvrdjuje transakciju
     * @throws Exception ako nije moguce potvrditi transakciju
     */
    public void potvrdiTransakciju() throws Exception {
        DbFabrikaKonekcije.getInstanca().getKonekcija().commit();
    }
    /**
     * Ponistava transakciju
     * @throws Exception ako nije moguce ponistiti transakciju
     */
    public void ponistiTransakciju() throws Exception {
        DbFabrikaKonekcije.getInstanca().getKonekcija().rollback();
    }
    
    /**
     * Cuva domenski objekat kao slog u bazi podatala
     * @param odo domenski objekat koji treba sacuvati
     * @throws Exception ukoliko dodje do greske prilikom uspostavljanja konekcije ili kreiranja SQL statement-a, ili primarni kljuc nije uspesno generisan
     */
    public void zapamtiSlog(OpstiDomenskiObjekat odo) throws Exception {
        String upit;
        konekcija = DbFabrikaKonekcije.getInstanca().getKonekcija();
        try (Statement statement = konekcija.createStatement()) {
            upit = "INSERT INTO " + odo.vratiImeKlase()
                    + "(" + odo.vratiImenaAtrubita() + ")" + " VALUES (" + odo.vratiVrednostiAtributa() + ")";
            statement.executeUpdate(upit, Statement.RETURN_GENERATED_KEYS);
            ResultSet rsId = statement.getGeneratedKeys();
            if (rsId.next()) {
                long id = rsId.getLong(1);
                odo.postaviVrednostPK(id);
            } else {
                throw new Exception("Neuspesno generisanje primarnog kljuca");
            }
        } catch (SQLException ex) {
            throw ex;
        }
    }
    /**
     * Menja slog u bazi podataka sa podacima iz domenskog objekta
     * @param odo objekat koji sadrzi primarni kljuc sloga koji treba promeniti i ostale podatke koje treba upisati u slog
     * @throws Exception ukoliko dodje do greske prilikom uspostavljanja konekcije ili kreiranja SQL statement-a, ili ne postoji zadati slog za promenu
     */
    public void promeniSlog(OpstiDomenskiObjekat odo) throws Exception {
        konekcija = DbFabrikaKonekcije.getInstanca().getKonekcija();
        try (Statement statement = konekcija.createStatement()) {
            String upit = "UPDATE " + odo.vratiImeKlase() + " SET " + odo.postaviVrednostiAtributa() + " WHERE " + odo.vratiUslovZaPromeniSlog();
            if (statement.executeUpdate(upit) == 0) {
                throw new Exception("Ne postoji slog za promenu u bazi");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }
    /**
     * Brise slog sa primarnim kljucem koji je postavljen u okviru objekta odo
     * @param odo objekat koji u sebi nosi primarni kljuc za brisanje sloga
     * @throws Exception ukoliko dodje do greske prilikom uspostavljanja konekcije ili kreiranja SQL statement-a, ili slog sa zadatim primarnim kljucem ne postoji
     */
    public void obrisiSlog(OpstiDomenskiObjekat odo) throws Exception {
        konekcija = DbFabrikaKonekcije.getInstanca().getKonekcija();
        try (Statement statement = konekcija.createStatement()) {
            String upit = "DELETE FROM " + odo.vratiImeKlase() + " WHERE " + odo.vratiUslovZaNadjiSlog();
            if (statement.executeUpdate(upit) == 0) {
                throw new Exception("Ne postoji slog za brisanje u bazi");
            }
        } catch (SQLException ex) {
            throw ex;
        }

    }
    /**
     * Pronalazi slog u bazi po primarnom kljucu
     * @param odo objekat koji u sebi nosi primarni kljuc za pronalazenje sloga
     * @return pronadjeni domenski objekat ili
     *         null ako ne postoji domenski objekat
     * @throws Exception ukoliko dodje do greske prilikom uspostavljanja konekcije ili kreiranja SQL statement-a, ili ako nije moguce pronaci slog
     */
    public OpstiDomenskiObjekat pronadjiSlogPoKljucu(OpstiDomenskiObjekat odo) throws Exception {
        OpstiDomenskiObjekat pronadjeni = null;
        konekcija = DbFabrikaKonekcije.getInstanca().getKonekcija();
        try (Statement statement = konekcija.createStatement()) {
            String upit = "SELECT * FROM " + odo.vratiImeKlase() + " WHERE " + odo.vratiUslovZaNadjiSlog();
            ResultSet rs = statement.executeQuery(upit);
            if (rs.next()) {
                pronadjeni = odo.kreirajInstancu();
                pronadjeni.napuni(rs);
                for (int i = 0; i < pronadjeni.vratiBrojVezanihObjekata(); i++) {
                    OpstiDomenskiObjekat vezo = pronadjeni.vratiVezaniObjekat(i);
                    vezo = pronadjiSlogPoKljucu(vezo);
                    pronadjeni.postaviVrednostVezanogObjekta(vezo, i);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        }
        return pronadjeni;
    }
    
    /**
     * Proverava postojanje sloga u bazi sa zadatim primarnim kljucem
     * @param odo domenski objekat koji u sebi nosi primarni kljuc za pronalazenje sloga
     * @return true ako slog postoji ili false ukoliko slog sa zadatim kljucem ne postoji u bazi
     * @throws Exception ukoliko dodje do greske prilikom uspostavljanja konekcije ili kreiranja SQL statement-a
     */
    public boolean daLiPostojiSlog(OpstiDomenskiObjekat odo) throws Exception {
        konekcija = DbFabrikaKonekcije.getInstanca().getKonekcija();
        try (Statement statement = konekcija.createStatement()) {
            String upit = "SELECT * FROM " + odo.vratiImeKlase() + " WHERE " + odo.vratiUslovZaNadjiSlog();
            ResultSet rs = statement.executeQuery(upit);
            return rs.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }
    /**
     * Vraca sve slogove iz baze koji odgovaraju tipu parametra odo
     * @param odo parametar koji odredjuje iz koje ce se tabele uzimati slogovi
     * @return listu svih objekata izvucenih iz baze podataka ili praznu listu ukoliko ne postoji domenski objekat
     * @throws Exception ukoliko dodje do greske prilikom uspostavljanja konekcije, kreiranja SQL statement-a,izvrsavanja upita ili nije moguce pronaci neki od vezanih objekata u bazi
     */
    public List<OpstiDomenskiObjekat> vratiSve(OpstiDomenskiObjekat odo) throws Exception {
        List<OpstiDomenskiObjekat> lista = new ArrayList<>();
        konekcija = DbFabrikaKonekcije.getInstanca().getKonekcija();
        try (Statement statement = konekcija.createStatement();) {
            String upit = "SELECT * FROM " + odo.vratiImeKlase();
            ResultSet rs = statement.executeQuery(upit);
            while (rs.next()) {
                OpstiDomenskiObjekat odo1 = odo.kreirajInstancu();
                odo1.napuni(rs);
                for (int i = 0; i < odo.vratiBrojVezanihObjekata(); i++) {
                    OpstiDomenskiObjekat vezo = odo1.vratiVezaniObjekat(i);
                    vezo = pronadjiSlogPoKljucu(vezo);
                    odo1.postaviVrednostVezanogObjekta(vezo, i);
                }

                lista.add(odo1);
            }
            rs.close();
        } catch (SQLException ex) {
            throw ex;
        }
        return lista;
    }
    /**
     * Pronalazi slogove iz baze koji ispunjavaju zadati kriterijum
     * @param odo domenski objekat koji u sebi sadrzi podatke za sastavljanje uslova za pretragu
     * @return listu pronadjenih domenskih objekata ili praznu listu ukoliko nijedan slog ne ispunajva zadati uslov
     * @throws Exception ukoliko dodje do greske prilikom uspostavljanja konekcije, kreiranja SQL statement-a,izvrsavanja upita ili nije moguce pronaci neki od vezanih objekata u bazi
     */
    public List<OpstiDomenskiObjekat> pronadjiSlogove(OpstiDomenskiObjekat odo) throws Exception {
        List<OpstiDomenskiObjekat> lista = new ArrayList<>();
        konekcija = DbFabrikaKonekcije.getInstanca().getKonekcija();
        try (Statement statement = konekcija.createStatement()) {
            String upit = "SELECT * FROM " + odo.vratiImeKlase() + " WHERE " + odo.vratiUslovZaNadjiSlogove();
            ResultSet rs = statement.executeQuery(upit);
            while (rs.next()) {
                OpstiDomenskiObjekat odo1 = odo.kreirajInstancu();
                odo1.napuni(rs);
                lista.add(odo1);
                for (int i = 0; i < odo1.vratiBrojVezanihObjekata(); i++) {
                    OpstiDomenskiObjekat vezo = odo1.vratiVezaniObjekat(i);
                    vezo = pronadjiSlogPoKljucu(vezo);
                    odo1.postaviVrednostVezanogObjekta(vezo, i);
                }
            }

        } catch (SQLException ex) {
            throw ex;
        }
        return lista;
    }
    /**
     * Pronalazi slogove iz baze po vrednosti unique kolone, ukoliko prosledjena vrednost nije unique i postoji vise slogova koji ispunajvaju uslov, metoda vraca samo prvi objekat
     * @param odo domenski objekat koji u sebi sadrzi podatke za sastavljanje uslova za pretragu
     * @return pronadjeni domenski objekat ili null ukoliko domenski objekat nije pronadjen
     * @throws Exception ukoliko dodje do greske prilikom uspostavljanja konekcije, kreiranja SQL statement-a,izvrsavanja upita ili nije moguce pronaci neki od vezanih objekata u bazi
     */
    public OpstiDomenskiObjekat pronadjiSlogUnique(OpstiDomenskiObjekat odo) throws Exception {
        OpstiDomenskiObjekat pronadjeni = null;
        konekcija = DbFabrikaKonekcije.getInstanca().getKonekcija();
        try (Statement statement = konekcija.createStatement()) {
            String upit = "SELECT * FROM " + odo.vratiImeKlase() + " WHERE " + odo.vratiUslovZaNadjiSlog2();
            ResultSet rs = statement.executeQuery(upit);
            if (rs.next()) {
                pronadjeni = odo.kreirajInstancu();
                pronadjeni.napuni(rs);
                for (int i = 0; i < pronadjeni.vratiBrojVezanihObjekata(); i++) {
                    OpstiDomenskiObjekat vezo = pronadjeni.vratiVezaniObjekat(i);
                    vezo = pronadjiSlogPoKljucu(vezo);
                    pronadjeni.postaviVrednostVezanogObjekta(vezo, i);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        }
        return pronadjeni;
    }

}
