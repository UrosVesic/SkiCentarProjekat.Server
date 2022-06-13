/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.fon.np.sc.server.so.impl;

import rs.ac.bg.fon.np.sc.commonLib.domen.OpstiDomenskiObjekat;
import rs.ac.bg.fon.np.sc.commonLib.domen.SkiCentar;
import rs.ac.bg.fon.np.sc.commonLib.validator.ValidationException;
import rs.ac.bg.fon.np.sc.commonLib.validator.Validator;
import rs.ac.bg.fon.np.sc.server.db.BrokerBP;
import rs.ac.bg.fon.np.sc.server.so.OpstaSO;

/**
 * Klasa koja predstavlja sistemsku operaciju cuvanje ski centra. Nasledjuje
 * klasu OpstaSO.
 *
 * @see rs.ac.bg.fon.np.sc.server.so.OpstaSO
 * @author UrosVesic
 */
public class ZapamtiSkiCentarSO extends OpstaSO {

    public ZapamtiSkiCentarSO(BrokerBP b, OpstiDomenskiObjekat odo) {
        super(b, odo);
    }

    /**
     * Izvrsava sistemsku operaciju - pamti kupca u bazi podataka
     *
     * @throws Exception ako nije moguce zapamtiti ski centar
     */
    @Override
    public void izvrsiOperaciju() throws Exception {
        b.zapamtiSlogGenerisiKljuc(odo);
    }

    @Override
    public void proveriPreduslove() throws ValidationException {
        SkiCentar sc = (SkiCentar) odo;
        Validator.startValidation().validateFieldsNotNullOrEmpty(sc).validateWorkingHoursFormat(sc.getRadnoVreme(), "Pogresan format radnog vremena")
                .throwIfInvalide();
    }

}
