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
public class PretraziSkiKarteSO extends OpstaSO {

    public PretraziSkiKarteSO(BrokerBP b, OpstiDomenskiObjekat odo) {
        super(b, odo);
    }

    @Override
    public void izvrsiOperaciju() throws Exception {
            lista = b.pronadjiSlogove(odo);
            if(lista.isEmpty()){
                throw new Exception("Ne postoji nijedna ski karta koja ispunjava uslov");
            }
    }

    @Override
    public void proveriPreduslove() throws Exception {
    }

}
