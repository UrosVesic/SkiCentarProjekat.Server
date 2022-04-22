/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.fon.np.sc.server.so.impl;

import rs.ac.bg.fon.np.sc.commonlib.domen.OpstiDomenskiObjekat;
import rs.ac.bg.fon.np.sc.server.db.BrokerBP;
import rs.ac.bg.fon.np.sc.server.so.OpstaSO;

/**
 *
 * @author UrosVesic
 */
public class ZapamtiSkiKartuSO extends OpstaSO {

    public ZapamtiSkiKartuSO(BrokerBP b, OpstiDomenskiObjekat odo) {
        super(b, odo);
    }

    @Override
    public void izvrsiOperaciju() throws Exception {
        b.zapamtiSlog(odo);
    }

    @Override
    public void proveriPreduslove() throws Exception {
        throw new UnsupportedOperationException();
    }

}
