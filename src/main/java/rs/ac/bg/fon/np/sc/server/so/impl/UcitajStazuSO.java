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
 * Klasa koja predstavlja sistemsku operaciju ucitavanje staza. Nasledjuje klasu OpstaSO.
 * @see rs.ac.bg.fon.np.sc.server.so.OpstaSO
 * @author UrosVesic
 */
public class UcitajStazuSO extends OpstaSO {

    public UcitajStazuSO(BrokerBP b, OpstiDomenskiObjekat odo) {
        super(b, odo);
    }
    /**
     * Izvrsava sistemsku operaciju - ucitava stazu iz baze
     * @throws Exception ako ne postoji staza koja ispunjava uslov ili je nije moguce ucitati
     */
    @Override
    public void izvrsiOperaciju() throws Exception {
        odo = b.pronadjiSlogPoKljucu(odo);
        if(odo == null){
            throw new Exception("Ne postoji staza sa zadatim kljucem");
        }
    }

    @Override
    public void proveriPreduslove(){
    }

}
