/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.fon.np.sc.server.db;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author UrosVesic
 */
public class DbFabrikaKonekcije {

    private Connection konekcija;
    private static DbFabrikaKonekcije instanca;

    private DbFabrikaKonekcije() {
    }

    public static DbFabrikaKonekcije getInstanca() {
        if (instanca == null) {
            instanca = new DbFabrikaKonekcije();
        }
        return instanca;
    }

    public Connection getKonekcija() throws SQLException, IOException {

        if (konekcija == null || konekcija.isClosed()) {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            try (InputStreamReader in = new InputStreamReader(classloader.getResourceAsStream("server/server.json"))){
                
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
            }
        }
        return konekcija;
    }

}
