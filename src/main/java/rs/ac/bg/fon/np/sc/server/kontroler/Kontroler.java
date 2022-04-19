/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.fon.np.sc.server.kontroler;

/**
 *
 * @author UrosVesic
 */
public class Kontroler {

    private static Kontroler instanca;

    private Kontroler() {

    }

    public static Kontroler getInstanca() {
        if (instanca == null) {
            instanca = new Kontroler();
        }
        return instanca;
    }
}
