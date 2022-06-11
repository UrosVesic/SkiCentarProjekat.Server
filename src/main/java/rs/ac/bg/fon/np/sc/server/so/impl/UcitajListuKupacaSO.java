/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.fon.np.sc.server.so.impl;

import rs.ac.bg.fon.np.sc.commonLib.domen.OpstiDomenskiObjekat;
import rs.ac.bg.fon.np.sc.server.db.BrokerBP;
import rs.ac.bg.fon.np.sc.server.so.OpstaSO;

/**
 * Klasa koja predstavlja sistemsku operaciju ucitavanja kupaca. Nasledjuje klasu OpstaSO.
 * @see rs.ac.bg.fon.np.sc.server.so.OpstaSO
 * @author UrosVesic
 */
public class UcitajListuKupacaSO extends OpstaSO {

    public UcitajListuKupacaSO(BrokerBP b, OpstiDomenskiObjekat odo) {
        super(b, odo);
    }
    /**
     * Izvrsava sistemsku operaciju - ucitava sve kupce listu
     * @throws Exception ako ne postoji nijedan kupac u bazi ili nije moguce ucitati listu kupaca
     */
    @Override
    public void izvrsiOperaciju() throws Exception {
        lista = b.vratiSve(odo);
        if (lista.isEmpty()) {
            throw new Exception("Ne postoje kupci u bazi");
        }
    }

    @Override
    public void proveriPreduslove(){
    }

}
