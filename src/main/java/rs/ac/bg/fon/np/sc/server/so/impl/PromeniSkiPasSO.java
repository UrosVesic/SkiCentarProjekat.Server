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
import rs.ac.bg.fon.np.sc.server.db.BrokerBP;
import rs.ac.bg.fon.np.sc.server.so.OpstaSO;

/**
 *
 * @author UrosVesic
 */
public class PromeniSkiPasSO extends OpstaSO {

    public PromeniSkiPasSO(BrokerBP b, OpstiDomenskiObjekat odo) {
        super(b, odo);
    }

    @Override
    public void izvrsiOperaciju() throws Exception {
        SkiPas skiPas = (SkiPas) odo;
        StavkaSkiPasa stavkaSkiPasa = skiPas.getStavkeSkiPasa().get(0);
        stavkaSkiPasa.setSkiPas(skiPas);
        List<OpstiDomenskiObjekat> stavkeIzBaze = b.pronadjiSlogove(stavkaSkiPasa);
        b.promeniSlog(odo);

        for (StavkaSkiPasa stavka : skiPas.getStavkeSkiPasa()) {
            stavka.setSkiPas(skiPas);
            if (b.daLiPostojiSlog(stavka)) {
                b.promeniSlog(stavka);
            } else {
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
    public void proveriPreduslove() throws Exception {
        throw new UnsupportedOperationException();
    }

}
