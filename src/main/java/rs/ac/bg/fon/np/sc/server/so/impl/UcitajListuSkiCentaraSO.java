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
 * Klasa koja predstavlja sistemsku operaciju ucitavanje ski centara Nasledjuje klasu OpstaSO.
 * @see rs.ac.bg.fon.np.sc.server.so.OpstaSO
 * @author UrosVesic
 */
public class UcitajListuSkiCentaraSO extends OpstaSO {

    public UcitajListuSkiCentaraSO(BrokerBP b, OpstiDomenskiObjekat odo) {
        super(b, odo);
    }
    
    /**
     * Izvrsava sistemsku operaciju - ucitava ski centre u listu
     * @throws Exception ako ne postoji nijedan ski centar ili nije moguce ucitati ski centre
     */
    @Override
    public void izvrsiOperaciju() throws Exception {
        lista = b.vratiSve(odo);
        if (lista.isEmpty()) {
            throw new Exception("Ne postoji nijedan ski centar u bazi");
        }
    }

    @Override
    public void proveriPreduslove(){
    }

}
