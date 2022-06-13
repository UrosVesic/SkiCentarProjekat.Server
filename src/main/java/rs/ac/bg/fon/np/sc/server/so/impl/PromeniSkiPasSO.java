/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.fon.np.sc.server.so.impl;

import java.util.List;
import rs.ac.bg.fon.np.sc.commonLib.domen.OpstiDomenskiObjekat;
import rs.ac.bg.fon.np.sc.commonLib.domen.SkiPas;
import rs.ac.bg.fon.np.sc.commonLib.domen.StavkaSkiPasa;
import rs.ac.bg.fon.np.sc.commonLib.validator.ValidationException;
import rs.ac.bg.fon.np.sc.commonLib.validator.Validator;
import rs.ac.bg.fon.np.sc.server.db.BrokerBP;
import rs.ac.bg.fon.np.sc.server.so.OpstaSO;

/**
 * Klasa koja predstavlja sistemsku operaciju promena ski pasa. Nasledjuje klasu
 * OpstaSO.
 *
 * @see rs.ac.bg.fon.np.sc.server.so.OpstaSO
 * @author UrosVesic
 */
public class PromeniSkiPasSO extends OpstaSO {

    public PromeniSkiPasSO(BrokerBP b, OpstiDomenskiObjekat odo) {
        super(b, odo);
    }

    /**
     * Izvrsava sistemsku operaciju - menja podatke o ski pasu u bazi podataka
     *
     * @throws Exception ako nije moguce promeniti podatke o ski pasu
     */
    @Override
    public void izvrsiOperaciju() throws Exception {
        SkiPas skiPas = (SkiPas) odo;
        StavkaSkiPasa stavkaSkiPasa = skiPas.getStavkeSkiPasa().get(0);
        stavkaSkiPasa.setSkiPas(skiPas);
        List<OpstiDomenskiObjekat> stavkeIzBaze = b.pronadjiSlogove(stavkaSkiPasa);
        b.promeniSlog(odo);

        StavkaSkiPasa stavka1 = skiPas.getStavkeSkiPasa().get(0);
        stavka1.setSkiPas(skiPas);
        long id = b.vratiMaxRbSlabogObjekta(stavka1);

        for (StavkaSkiPasa stavka : skiPas.getStavkeSkiPasa()) {
            stavka.setSkiPas(skiPas);
            if (b.daLiPostojiSlog(stavka)) {
                b.promeniSlog(stavka);
            } else {
                stavka.setRedniBroj(id);
                id++;
                b.zapamtiSlog(stavka);
            }
        }

        for (OpstiDomenskiObjekat stavkaIzBaze : stavkeIzBaze) {
            if (!skiPas.getStavkeSkiPasa().contains(stavkaIzBaze)) {
                b.obrisiSlog(stavkaIzBaze);
            }
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
