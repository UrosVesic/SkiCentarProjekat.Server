/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.fon.np.sc.server.db;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton klasa koja je zaduzena za kreiranje konekcije sa bazom podataka
 * @author UrosVesic
 */
public class DbFabrikaKonekcije {
    
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
    private DbFabrikaKonekcije() {
    }
    
    /**
     * Pravi novu ili vraca vec postojecu instancu klase DbFarbrikaKonekcije. Predstavlja implementaciju Singleton paterna
     * @return instancu klase DbFabrikaKonekcije
     */
    public static DbFabrikaKonekcije getInstanca() {
        if (instanca == null) {
            instanca = new DbFabrikaKonekcije();
        }
        return instanca;
    }
    
    /**
     * Vraca novu konekciju sa bazom podataka
     * @return konekciju sa bazom podataka kao objekat klase Connection
     * @throws Exception ako nije moguce uspostaviti konekciju sa bazom podataka
     */
    public Connection getKonekcija() throws Exception {

        if (konekcija == null || konekcija.isClosed()) {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            try (InputStreamReader in = new InputStreamReader(classloader.getResourceAsStream("server/db.json"))) {

                Gson gson = new Gson();

                JsonObject json = gson.fromJson(in, JsonObject.class);

                String url = json.get("url").getAsString();
                String user = json.get("username").getAsString();
                String password = json.get("password").getAsString();

                konekcija = DriverManager.getConnection(url, user, password);
                konekcija.setAutoCommit(false);
            } catch (SQLException ex) {
                System.out.println("Neuspesno uspostavljanje konekcije!\n" + ex.getMessage());
                throw ex;
            } catch (IOException ex) {
                System.out.println("Neuspesno citanje konfuguracije iz fajla");
                throw ex;
            }
        }
        return konekcija;
    }

}
