/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.fon.np.sc.server.so.impl;

import rs.ac.bg.fon.np.sc.commonLib.domen.OpstiDomenskiObjekat;
import rs.ac.bg.fon.np.sc.commonLib.domen.Staza;
import rs.ac.bg.fon.np.sc.commonLib.validator.ValidationException;
import rs.ac.bg.fon.np.sc.commonLib.validator.Validator;
import rs.ac.bg.fon.np.sc.server.db.BrokerBP;
import rs.ac.bg.fon.np.sc.server.so.OpstaSO;

/**
 * Klasa koja predstavlja sistemsku operaciju promena ski staze. Nasledjuje
 * klasu OpstaSO.
 *
 * @see rs.ac.bg.fon.np.sc.server.so.OpstaSO
 * @author UrosVesic
 */
public class PromeniStazuSO extends OpstaSO {

    public PromeniStazuSO(BrokerBP b, OpstiDomenskiObjekat odo) {
        super(b, odo);
    }

    /**
     * Izvrsava sistemsku operaciju - menja podatke o stazi u bazi podataka
     *
     * @throws Exception ako nije moguce promeniti podatke o bazi
     */
    @Override
    public void izvrsiOperaciju() throws Exception {
        b.promeniSlog(odo);
    }

    @Override
    public void proveriPreduslove() throws ValidationException {
        Staza s = (Staza) odo;
        Validator.startValidation().validateFieldsNotNullOrEmpty(s).throwIfInvalide();
    }

}
