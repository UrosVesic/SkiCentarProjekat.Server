/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.fon.np.sc.server.so.impl;

import rs.ac.bg.fon.np.sc.commonLib.domen.OpstiDomenskiObjekat;
import rs.ac.bg.fon.np.sc.commonLib.domen.SkiKarta;
import rs.ac.bg.fon.np.sc.commonLib.validator.ValidationException;
import rs.ac.bg.fon.np.sc.commonLib.validator.Validator;
import rs.ac.bg.fon.np.sc.server.db.BrokerBP;
import rs.ac.bg.fon.np.sc.server.so.OpstaSO;

/**
 * Klasa koja predstavlja sistemsku operaciju pretrage ski karata. Nasledjuje
 * klasu OpstaSO.
 *
 * @see rs.ac.bg.fon.np.sc.server.so.OpstaSO
 * @author UrosVesic
 */
public class PretraziSkiKarteSO extends OpstaSO {

    public PretraziSkiKarteSO(BrokerBP b, OpstiDomenskiObjekat odo) {
        super(b, odo);
    }

    /**
     * Izvrsava sistemsku operaciju - pretrazuje ski karte
     *
     * @throws Exception ako ne postoji nijedna ski karta koja ispunjava
     * kriterijum ili je nije moguce ucitati
     */
    @Override
    public void izvrsiOperaciju() throws Exception {
        lista = b.pronadjiSlogove(odo);
        if (lista.isEmpty()) {
            throw new Exception("Ne postoji nijedna ski karta koja ispunjava uslov");
        }
    }

    @Override
    public void proveriPreduslove() throws ValidationException {
        SkiKarta sk = (SkiKarta) odo;
        Validator.startValidation().validateNotNull(sk.getCenaSkiKarte(), "Niste uneli cenu za pretragu").throwIfInvalide()
                .validateGreaterThanZero(sk.getCenaSkiKarte().longValue(), "Cena mora biti veca od 0")
                .throwIfInvalide();
    }

}
