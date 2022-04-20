/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.fon.np.sc.server.niti;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.ac.bg.fon.np.sc.commonlib.komunikacija.Odgovor;
import rs.ac.bg.fon.np.sc.commonlib.komunikacija.Posiljalac;
import rs.ac.bg.fon.np.sc.commonlib.komunikacija.Primalac;
import rs.ac.bg.fon.np.sc.commonlib.komunikacija.Zahtev;

/**
 *
 * @author UrosVesic
 */
public class KlijentskaNit extends Thread {

    Socket socket;
    ServerskaNit serverskaNit;

    public KlijentskaNit(Socket socket, ServerskaNit serverskaNit) {
        this.socket = socket;
        this.serverskaNit = serverskaNit;
    }


    @Override
    public void run() {
        while (!socket.isClosed()) {
            try {
                Zahtev zahtev = (Zahtev) new Primalac(socket).primi();
                obradiZahtev(zahtev);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        serverskaNit.obavestiOOdjavljivanju(this);
    }

    void zaustavi() {
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(KlijentskaNit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void obradiZahtev(Zahtev zahtev) {
        Odgovor odgovor = new Odgovor();
       

        new Posiljalac(socket).posalji(odgovor);
    }

   

}
