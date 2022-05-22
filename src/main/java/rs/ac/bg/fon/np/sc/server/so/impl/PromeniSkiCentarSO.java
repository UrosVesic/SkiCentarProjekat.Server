/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.fon.np.sc.server.so.impl;

import rs.ac.bg.fon.np.sc.commonlib.domen.OpstiDomenskiObjekat;
import rs.ac.bg.fon.np.sc.commonlib.domen.SkiCentar;
import rs.ac.bg.fon.np.sc.commonlib.validator.ValidationException;
import rs.ac.bg.fon.np.sc.server.db.BrokerBP;
import rs.ac.bg.fon.np.sc.server.so.OpstaSO;
import rs.ac.bg.fon.np.sc.commonlib.validator.Validator;

/**
 * Klasa koja predstavlja sistemsku operaciju promena ski centra. Nasledjuje klasu OpstaSO.
 * @see rs.ac.bg.fon.np.sc.server.so.OpstaSO
 * @author UrosVesic
 */
public class PromeniSkiCentarSO extends OpstaSO {

    public PromeniSkiCentarSO(BrokerBP b, OpstiDomenskiObjekat odo) {
        super(b, odo);
    }
    /**
     * Izvrsava sistemsku operaciju - menja slog u bazi podataka
     * @throws Exception ako nije moguce promeniti slog u bazi
     */
    @Override
    public void izvrsiOperaciju() throws Exception {
        b.promeniSlog(odo);
    }

    @Override
    public void proveriPreduslove() throws ValidationException {
        SkiCentar sc = (SkiCentar) odo;
        Validator.startValidation().validateWorkingHoursFormat(sc.getRadnoVreme(), "Pogresan format radnog vremena").throwIfInvalide();
    }

}
