/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.fon.np.sc.server.so.impl;

import java.math.BigDecimal;
import rs.ac.bg.fon.np.sc.commonLib.domen.OpstiDomenskiObjekat;
import rs.ac.bg.fon.np.sc.commonLib.domen.SkiKarta;
import rs.ac.bg.fon.np.sc.commonLib.validator.ValidationException;
import rs.ac.bg.fon.np.sc.commonLib.validator.Validator;
import rs.ac.bg.fon.np.sc.server.db.BrokerBP;
import rs.ac.bg.fon.np.sc.server.so.OpstaSO;

/**
 * Klasa koja predstavlja sistemsku operaciju cuvanje ski karte. Nasledjuje
 * klasu OpstaSO.
 *
 * @see rs.ac.bg.fon.np.sc.server.so.OpstaSO
 * @author UrosVesic
 */
public class ZapamtiSkiKartuSO extends OpstaSO {

    public ZapamtiSkiKartuSO(BrokerBP b, OpstiDomenskiObjekat odo) {
        super(b, odo);
    }

    /**
     * Izvrsava sistemsku operaciju - pamti kupca u bazi podataka
     *
     * @throws Exception ako nije moguce zapamtiti ski kartu
     */
    @Override
    public void izvrsiOperaciju() throws Exception {
        b.zapamtiSlogGenerisiKljuc(odo);
    }

    @Override
    public void proveriPreduslove() throws ValidationException {
        SkiKarta sk = (SkiKarta) odo;
        Validator.startValidation().validateFieldsNotNullOrEmpty(sk).throwIfInvalide().validateGreaterThanZero(sk.getCenaSkiKarte().longValue(), "Cena mora biti veca od 0").throwIfInvalide();
    }

}
