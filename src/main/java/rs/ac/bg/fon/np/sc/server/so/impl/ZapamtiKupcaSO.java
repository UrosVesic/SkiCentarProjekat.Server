/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.fon.np.sc.server.so.impl;

import rs.ac.bg.fon.np.sc.commonLib.domen.OpstiDomenskiObjekat;
import rs.ac.bg.fon.np.sc.commonLib.validator.ValidationException;
import rs.ac.bg.fon.np.sc.server.db.BrokerBP;
import rs.ac.bg.fon.np.sc.server.so.OpstaSO;

/**
 * Klasa koja predstavlja sistemsku operaciju cuvanje kupca. Nasledjuje klasu OpstaSO.
 * @see rs.ac.bg.fon.np.sc.server.so.OpstaSO
 * @author UrosVesic
 */
public class ZapamtiKupcaSO extends OpstaSO {

    public ZapamtiKupcaSO(BrokerBP b, OpstiDomenskiObjekat odo) {
        super(b, odo);
    }
    /**
     * Izvrsava sistemsku operaciju - pamti kupca u bazi podataka
     * @throws Exception ako nije moguce zapamtiti kupca
     */
    @Override
    public void izvrsiOperaciju() throws Exception {
        b.zapamtiSlogGenerisiKljuc(odo);
    }

    @Override
    protected void proveriPreduslove() {
    }

}
