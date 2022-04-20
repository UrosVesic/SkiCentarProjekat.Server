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
import rs.ac.bg.fon.np.sc.commonLib.domen.OpstiDomenskiObjekat;

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
        try {
            upit = "INSERT INTO " + odo.vratiImeKlase()
                    + " VALUES (" + odo.vratiVrednostiAtributa() + ")";
            konekcija = DbFabrikaKonekcije.getInstanca().getKonekcija();
            Statement statement = konekcija.createStatement();
            statement.executeUpdate(upit, Statement.RETURN_GENERATED_KEYS);
            ResultSet rsId = statement.getGeneratedKeys();
            if (rsId.next()) {
                long id = rsId.getLong(1);
                odo.postaviVrednostPK(id);
            }
            else{
                throw new Exception("Neuspesno generisanje primarnog kljuca");
            }
            statement.close();
        } catch (SQLException ex) {
            throw ex;
        }
        return odo;
    }

}
