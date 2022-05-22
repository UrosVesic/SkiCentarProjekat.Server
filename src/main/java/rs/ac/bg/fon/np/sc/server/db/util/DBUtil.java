/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.fon.np.sc.server.db.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Klasa koja cita i vraca parametre za konfiguraciju konekcije sa bazom
 *
 * @author UrosVesic
 */
public class DBUtil {

    /**
     * Iscitava konfiguracioni fajl db.json
     */
    private final InputStreamReader in;
    /**
     * Instanca klase DBUtil, implementacija Singleton paterna
     */
    private static DBUtil instance;
    /**
     * Gson objekat za rad sa json formatom
     */
    private final Gson gson;
    /**
     * JsonObject iščitan iz db.json fajla
     */
    private final JsonObject json;

    /**
     * Default konstrukot klase DBUtil. Otvara konfoguracioni fajl db.json, i
     * inicijalizuje objekte gson i json.
     */
    public DBUtil() {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        in = new InputStreamReader(classloader.getResourceAsStream("server/db.json"));
        gson = new Gson();
        json = gson.fromJson(in, JsonObject.class);
    }
    /**
     * Staticka metoda koja kreira objekat klase DBUtil
     * @return Postojecu instancu klase DBUtil ili novu ako ista ne postoji
     */
    public static DBUtil getInstance()  {
        if (instance == null) {
            instance = new DBUtil();
        }
        return instance;
    }
    
    /**
     * Vraca parametar url za konekciju sa bazom
     * @return Url parametar kao String
     */
    public String getUrl() {
        return json.get("url").getAsString();
    }
    /**
     * Vraca parametar user za konekciju sa bazom
     * @return User parametar kao String
     */
    public String getUser() {
        return json.get("username").getAsString();
    }
    /**
     * Vraca parametar password za konekciju sa bazom
     * @return Password parametar kao String
     */
    public String getPassword() {
        return json.get("password").getAsString();
    }
    /**
     * Vraca parametar Connection factory class name za konekciju sa bazom
     * @return Connection factory class name parametar kao String
     */
    public String getConnectionFactoryClassName() {
        return json.get("connection_factory_class_name").getAsString();
    }

}
