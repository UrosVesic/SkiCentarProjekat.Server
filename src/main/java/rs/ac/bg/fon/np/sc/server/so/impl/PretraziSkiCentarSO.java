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
 * Klasa koja predstavlja sistemsku operaciju pretrage ski centra. Nasledjuje
 * klasu OpstaSO.
 *
 * @see OpstaSO
 * @author UrosVesic
 */
public class PretraziSkiCentarSO extends OpstaSO {

    public PretraziSkiCentarSO(BrokerBP b, OpstiDomenskiObjekat odo) {
        super(b, odo);
    }

    /**
     * Izvrsava sistemsku operaciju - pretrazuje ski centar
     *
     * @throws Exception ako ne postoji trazeni ski centar ili ga nije moguce
     * ucitati
     */
    @Override
    public void izvrsiOperaciju() throws Exception {
        odo = b.pronadjiSlogUnique(odo);
        if (odo == null) {
            throw new Exception("Ne postoji ski centar");
        }

    }

    @Override
    public void proveriPreduslove() throws ValidationException {
        SkiCentar sc = (SkiCentar) odo;
        Validator.startValidation().validateNotNullOrEmpty(sc.getNazivSkiCentra(), "Niste uneli naziv za pretragu").throwIfInvalide();
    }

}
