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
 *
 * @author UrosVesic
 */

/*
 ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            try (InputStreamReader in = new InputStreamReader(classloader.getResourceAsStream("server/db.json"))) {

                Gson gson = new Gson();

                JsonObject json = gson.fromJson(in, JsonObject.class);

                String url = json.get("url").getAsString();
                String user = json.get("username").getAsString();
                String password = json.get("password").getAsString();
 */
public class DBUtil {

    private InputStreamReader in;
    private static DBUtil instance;
    private Gson gson;
    private JsonObject json;

    public DBUtil() {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        in = new InputStreamReader(classloader.getResourceAsStream("server/db.json"));
        gson = new Gson();
        json = gson.fromJson(in, JsonObject.class);
    }

    public static DBUtil getInstance() throws IOException {
        if (instance == null) {
            instance = new DBUtil();
        }
        return instance;
    }

    public String getUrl() {
        return json.get("url").getAsString();
    }

    public String getUser() {
        return json.get("username").getAsString();
    }

    public String getPassword() {
        return json.get("password").getAsString();
    }

    public String getConnectionFactoryClassName() {
        return json.get("connection_factory_class_name").getAsString();
    }

}
