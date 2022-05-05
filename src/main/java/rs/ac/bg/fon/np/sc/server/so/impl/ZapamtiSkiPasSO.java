/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.fon.np.sc.server.so.impl;

import rs.ac.bg.fon.np.sc.commonlib.domen.OpstiDomenskiObjekat;
import rs.ac.bg.fon.np.sc.commonlib.domen.SkiPas;
import rs.ac.bg.fon.np.sc.commonlib.domen.StavkaSkiPasa;
import rs.ac.bg.fon.np.sc.server.db.BrokerBP;
import rs.ac.bg.fon.np.sc.server.so.OpstaSO;

/**
 *
 * @author UrosVesic
 */
public class ZapamtiSkiPasSO extends OpstaSO {

    public ZapamtiSkiPasSO(BrokerBP b, OpstiDomenskiObjekat odo) {
        super(b, odo);
    }

    @Override
    public void izvrsiOperaciju() throws Exception {
        SkiPas skiPas = (SkiPas) odo;
        b.zapamtiSlog(skiPas);
        for (StavkaSkiPasa stavkaSkiPasa : skiPas.getStavkeSkiPasa()) {
            stavkaSkiPasa.setSkiPas(skiPas);
            b.zapamtiSlog(stavkaSkiPasa);
        }
    }

    @Override
    protected void proveriPreduslove() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
