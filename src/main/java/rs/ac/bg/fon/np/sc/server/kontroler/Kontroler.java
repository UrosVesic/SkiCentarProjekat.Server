/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.fon.np.sc.server.kontroler;

import rs.ac.bg.fon.np.sc.server.forme.ServerskaForma;
import rs.ac.bg.fon.np.sc.server.modelitabela.ModelTabeleKorisnik;

/**
 *
 * @author UrosVesic
 */
public class Kontroler {

    private static Kontroler instanca;
    ServerskaForma serverskaForma;

    private Kontroler() {

    }

    public void setServerskaForma(ServerskaForma serverskaForma) {
        this.serverskaForma = serverskaForma;
    }
    
    

    public static Kontroler getInstanca() {
        if (instanca == null) {
            instanca = new Kontroler();
        }
        return instanca;
    }

    public void pripremiTabelu() {
        ModelTabeleKorisnik model = new ModelTabeleKorisnik();
        serverskaForma.getTblKorisnici().setModel(model);
    }
}
