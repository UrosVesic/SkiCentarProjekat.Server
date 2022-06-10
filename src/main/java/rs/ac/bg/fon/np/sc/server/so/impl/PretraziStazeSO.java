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
 * Klasa koja predstavlja sistemsku operaciju pretrage staza. Nasledjuje klasu
 * OpstaSO.
 *
 * @see rs.ac.bg.fon.np.sc.server.so.OpstaSO
 * @author UrosVesic
 */
public class PretraziStazeSO extends OpstaSO {

    public PretraziStazeSO(BrokerBP b, OpstiDomenskiObjekat odo) {
        super(b, odo);
    }

    /**
     * Izvrsava sistemsku operaciju - pretrazuje staze
     *
     * @throws Exception ako ne postoji nijedna staza koja ispunjava kriterijum
     * ili je nije moguce ucitati
     */
    @Override
    public void izvrsiOperaciju() throws Exception {
        lista = b.pronadjiSlogove(odo);
        if (lista.isEmpty()) {
            throw new Exception("Nisu pronadjene staze");
        }
    }

    @Override
    public void proveriPreduslove() throws ValidationException {
        Staza s = (Staza) odo;
        Validator.startValidation().validateNotNullOrEmpty(s.getSkiCentar().getNazivSkiCentra(), "Niste uneli naziv ski centra za pretragu").throwIfInvalide();
    }

}
