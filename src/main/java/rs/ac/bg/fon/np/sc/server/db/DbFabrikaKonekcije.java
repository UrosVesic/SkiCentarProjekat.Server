/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.fon.np.sc.server.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import rs.ac.bg.fon.np.sc.server.db.util.DBUtil;

/**
 * Singleton klasa koja je zaduzena za kreiranje konekcije sa bazom podataka
 *
 * @author UrosVesic
 */
public class DbFabrikaKonekcije {

    private ConnectionPool pool;

    /**
     * Konekcija sa bazom podataka
     */
    private Connection konekcija;
    /**
     * Instanca klase DbFarbrikaKonekcije
     */
    private static DbFabrikaKonekcije instanca;

    /**
     * Default konstruktor koji postavlja atribute na predefinisane vrednosti
     */
    private DbFabrikaKonekcije() throws Exception {
        try {
            pool = new ConnectionPool();
            pool.inicijalizacijaConnectionPoolDataSource();
        } catch (Exception ex) {
            throw new Exception("Neuspensa inicijalizacija connection pool data source-a");
        }
    }

    /**
     * Pravi novu ili vraca vec postojecu instancu klase DbFarbrikaKonekcije.
     * Predstavlja implementaciju Singleton paterna
     *
     * @return instancu klase DbFabrikaKonekcije
     * @throws Exception
     */
    public static DbFabrikaKonekcije getInstanca() throws Exception {
        if (instanca == null) {
            instanca = new DbFabrikaKonekcije();
        }
        return instanca;
    }

    /**
     * Vraca novu konekciju sa bazom podataka
     *
     * @return konekciju sa bazom podataka kao objekat klase Connection
     * @throws Exception ako nije moguce uspostaviti konekciju sa bazom podataka
     */
    public Connection getKonekcija() throws Exception {

        if (konekcija == null || konekcija.isClosed()) {
            try {
                konekcija = pool.uspostaviKonekciju();
                konekcija.setAutoCommit(false);
            } catch (SQLException ex) {
                System.out.println("Neuspesno uspostavljanje konekcije!\n" + ex.getMessage());
                throw ex;

            }
        }
        return konekcija;
    }

}
