/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.fon.np.sc.server.niti;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author UrosVesic
 */
public class ServerskaNit extends Thread {

    ServerSocket serverSocket;
    List<KlijentskaNit> klijentskeNiti;

    public ServerskaNit(ServerSocket serverSocket) {
        klijentskeNiti = new ArrayList<>();
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        while (!serverSocket.isClosed()) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Klijent se povezao");
                KlijentskaNit k = new KlijentskaNit(socket, this);
                k.start();
                klijentskeNiti.add(k);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        zaustaviKlijentskeNiti();
    }

    private void zaustaviKlijentskeNiti() {
        for (KlijentskaNit klijentskaNit : klijentskeNiti) {
            klijentskaNit.zaustavi();
        }
    }

    public void zaustavi() throws IOException {
        serverSocket.close();
    }

    void obavestiOOdjavljivanju(KlijentskaNit aThis) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
