/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.fon.np.sc.server.so.impl;

import java.util.List;
import rs.ac.bg.fon.np.sc.commonlib.domen.OpstiDomenskiObjekat;
import rs.ac.bg.fon.np.sc.commonlib.domen.SkiPas;
import rs.ac.bg.fon.np.sc.commonlib.domen.StavkaSkiPasa;
import rs.ac.bg.fon.np.sc.commonlib.validator.ValidationException;
import rs.ac.bg.fon.np.sc.commonlib.validator.Validator;
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
        SkiPas sp = (SkiPas) odo;
        Validator.startValidation().validateIfDateIsInSeason(sp.getDatumIzdavanja(), sp.getSezona(), "Datum izdavanja ski pasa nije u sezoni koja je navedena").throwIfInvalide();
    }

}
