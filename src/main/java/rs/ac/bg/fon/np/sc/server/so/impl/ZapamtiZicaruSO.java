/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.fon.np.sc.server.so.impl;

import rs.ac.bg.fon.np.sc.commonlib.domen.OpstiDomenskiObjekat;
import rs.ac.bg.fon.np.sc.commonlib.domen.Zicara;
import rs.ac.bg.fon.np.sc.commonlib.validator.ValidationException;
import rs.ac.bg.fon.np.sc.commonlib.validator.Validator;
import rs.ac.bg.fon.np.sc.server.db.BrokerBP;
import rs.ac.bg.fon.np.sc.server.so.OpstaSO;

/**
 * Klasa koja predstavlja sistemsku operaciju cuvanje zicare. Nasledjuje klasu OpstaSO.
 * @see rs.ac.bg.fon.np.sc.server.so.OpstaSO
 * @author UrosVesic
 */
public class ZapamtiZicaruSO extends OpstaSO {

    public ZapamtiZicaruSO(BrokerBP b, OpstiDomenskiObjekat odo) {
        super(b, odo);
    }
    /**
     * Izvrsava sistemsku operaciju - pamti kupca u bazi podataka
     * @throws Exception ako nije moguce zapamtiti zicaru
     */
    @Override
    public void izvrsiOperaciju() throws Exception {
        b.zapamtiSlog(odo);
    }

    @Override
    public void proveriPreduslove() throws ValidationException {
        Zicara z = (Zicara) odo;
        Validator.startValidation().validateGreaterThanZero(z.getKapacitet(), "Kapacitet mora biti veci od 0").throwIfInvalide();
    }

}
