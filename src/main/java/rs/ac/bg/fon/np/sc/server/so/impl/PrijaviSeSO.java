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
 * Klasa koja predstavlja sistemsku operaciju prijavljivanje korisnika na sistem. Nasledjuje klasu OpstaSO.
 * @see rs.ac.bg.fon.np.sc.server.so.OpstaSO
 * @author UrosVesic
 */
public class PrijaviSeSO extends OpstaSO {

    public PrijaviSeSO(BrokerBP b, OpstiDomenskiObjekat odo) {
        super(b, odo);
    }
    /**
     * Izvrsava sistemsku operaciju - prijavljuje korisnika
     * @throws Exception ako su uneti pogresni parametri za prijavu
     */
    @Override
    public void izvrsiOperaciju() throws Exception {
        odo = b.pronadjiSlogPoKljucu(odo);
        if (odo == null) {
            throw new Exception("Ne postoji korisnik u bazi");
        }
    }

    @Override
    public void proveriPreduslove() {
    }

}
