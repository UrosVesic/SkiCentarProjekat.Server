/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.fon.np.sc.server.so.impl;

import rs.ac.bg.fon.np.sc.commonLib.domen.OpstiDomenskiObjekat;
import rs.ac.bg.fon.np.sc.commonLib.domen.SkiPas;
import rs.ac.bg.fon.np.sc.commonLib.domen.StavkaSkiPasa;
import rs.ac.bg.fon.np.sc.commonLib.validator.ValidationException;
import rs.ac.bg.fon.np.sc.commonLib.validator.Validator;
import rs.ac.bg.fon.np.sc.server.db.BrokerBP;
import rs.ac.bg.fon.np.sc.server.so.OpstaSO;

/**
 * Klasa koja predstavlja sistemsku operaciju cuvanje ski pasa. Nasledjuje klasu
 * OpstaSO.
 *
 * @see rs.ac.bg.fon.np.sc.server.so.OpstaSO
 * @author UrosVesic
 */
public class ZapamtiSkiPasSO extends OpstaSO {

    public ZapamtiSkiPasSO(BrokerBP b, OpstiDomenskiObjekat odo) {
        super(b, odo);
    }

    /**
     * Izvrsava sistemsku operaciju - pamti kupca u bazi podataka
     *
     * @throws Exception ako nije moguce zapamtiti ski pas ili neku od stavki
     */
    @Override
    public void izvrsiOperaciju() throws Exception {
        SkiPas skiPas = (SkiPas) odo;
        b.zapamtiSlogGenerisiKljuc(skiPas);
        long id = 1;
        for (StavkaSkiPasa stavkaSkiPasa : skiPas.getStavkeSkiPasa()) {
            stavkaSkiPasa.setSkiPas(skiPas);
            stavkaSkiPasa.setRedniBroj(id);
            id++;
            b.zapamtiSlog(stavkaSkiPasa);
        }
    }

    @Override
    public void proveriPreduslove() throws ValidationException {
        SkiPas skiPas = (SkiPas) odo;
        Validator.startValidation().validateFieldsNotNullOrEmpty(skiPas).throwIfInvalide().validateSeasonFormat(skiPas.getSezona(), "Nepravilan format sezone").throwIfInvalide()
                .validateNotNullOrEmpty(skiPas.getStavkeSkiPasa(), "Ne moze se sacuvati ski pas bez stavki")
                .validateIfDateIsInSeason(skiPas.getDatumIzdavanja(), skiPas.getSezona(), "Datum izdavanja ski pasa nije u navedenoj sezoni")
                .throwIfInvalide();
        for (StavkaSkiPasa stavkaSkiPasa : skiPas.getStavkeSkiPasa()) {
            Validator.startValidation().validateIfDateIsInSeason(stavkaSkiPasa.getPocetakVazenja(), skiPas.getSezona(), "Stavka " + stavkaSkiPasa.getRedniBroj() + ". nije u sezoni za koju se izdaje ski pas").throwIfInvalide();
        }
    }

}
