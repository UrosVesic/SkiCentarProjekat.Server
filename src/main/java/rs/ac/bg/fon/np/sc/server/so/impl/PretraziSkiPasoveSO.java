/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.fon.np.sc.server.so.impl;

import rs.ac.bg.fon.np.sc.commonLib.domen.Kupac;
import rs.ac.bg.fon.np.sc.commonLib.domen.OpstiDomenskiObjekat;
import rs.ac.bg.fon.np.sc.commonLib.validator.ValidationException;
import rs.ac.bg.fon.np.sc.commonLib.validator.Validator;
import rs.ac.bg.fon.np.sc.server.db.BrokerBP;
import rs.ac.bg.fon.np.sc.server.so.OpstaSO;

/**
 * Klasa koja predstavlja sistemsku operaciju pretrage ski pasova. Nasledjuje
 * klasu OpstaSO.
 *
 * @see rs.ac.bg.fon.np.sc.server.so.OpstaSO
 * @author UrosVesic
 */
public class PretraziSkiPasoveSO extends OpstaSO {

    public PretraziSkiPasoveSO(BrokerBP b, OpstiDomenskiObjekat odo) {
        super(b, odo);
    }

    /**
     * Izvrsava sistemsku operaciju - pretrazuje ski pasove
     *
     * @throws Exception ako ne postoji nijedan ski pas koji ispunjva kriterijum
     * ili ga nije moguce ucitati
     */
    @Override
    public void izvrsiOperaciju() throws Exception {
        lista = b.pronadjiSlogove(odo);
        if (lista.isEmpty()) {
            throw new Exception("Ne postoji ski pas za zadatog kupca");
        }
    }

    @Override
    public void proveriPreduslove() throws ValidationException {
        Kupac k = (Kupac) odo;
        Validator.startValidation().validateNotNullOrEmpty(k.getIme(), "Niste uneli ime za pretragu.").throwIfInvalide();
    }

}
