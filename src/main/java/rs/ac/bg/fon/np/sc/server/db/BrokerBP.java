/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.fon.np.sc.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import rs.ac.bg.fon.np.sc.commonlib.domen.OpstiDomenskiObjekat;

/**
 *
 * @author UrosVesic
 */
public class BrokerBP {

    private Connection konekcija;

    public void uspostaviKonekciju() throws Exception {
        DbFabrikaKonekcije.getInstanca().getKonekcija();
    }

    public void raskiniKonekciju() throws Exception {
        DbFabrikaKonekcije.getInstanca().getKonekcija().close();
    }

    public void potvrdiTransakciju() throws Exception {
        DbFabrikaKonekcije.getInstanca().getKonekcija().commit();
    }

    public void ponistiTransakciju() throws Exception {
        DbFabrikaKonekcije.getInstanca().getKonekcija().rollback();
    }

    public OpstiDomenskiObjekat zapamtiSlog(OpstiDomenskiObjekat odo) throws Exception {
        String upit;
        konekcija = DbFabrikaKonekcije.getInstanca().getKonekcija();
        try (Statement statement = konekcija.createStatement();) {
            upit = "INSERT INTO " + odo.vratiImeKlase()
                    + " VALUES (" + odo.vratiVrednostiAtributa() + ")";
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
        return odo;
    }

    public void promeniSlog(OpstiDomenskiObjekat odo) throws SQLException, Exception {
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

    public void pronadjiSlog(OpstiDomenskiObjekat odo) throws SQLException, Exception {
        konekcija = DbFabrikaKonekcije.getInstanca().getKonekcija();
        try (Statement statement = konekcija.createStatement()) {
            String upit = "SELECT * FROM " + odo.vratiImeKlase() + " WHERE " + odo.vratiUslovZaNadjiSlog();
            ResultSet rs = statement.executeQuery(upit);
            if (rs.next()) {
                odo.napuni(rs);
                for (int i = 0; i < odo.vratiBrojVezanihObjekata(); i++) {
                    OpstiDomenskiObjekat vezo = odo.vratiVezaniObjekat(i);
                    pronadjiSlog(vezo);
                    odo.postaviVrednostVezanogObjekta(vezo, i);
                }
            } else {
                throw new Exception("Slog nije pronadjen\n");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    public boolean daLiPostojiSlog(OpstiDomenskiObjekat odo) throws SQLException, Exception {
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

}
