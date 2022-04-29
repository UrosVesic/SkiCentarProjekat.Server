/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.fon.np.sc.server.so.impl;

import java.util.ArrayList;
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
public class UcitajSkiPasSO extends OpstaSO {

    public UcitajSkiPasSO(BrokerBP b, OpstiDomenskiObjekat odo) {
        super(b, odo);
    }

    @Override
    public void izvrsiOperaciju() throws Exception {
        //b.pronadjiSlozenSlog(odo);
        b.pronadjiSlogPoKljucu(odo);
        SkiPas skiPas = (SkiPas) odo;
        StavkaSkiPasa stavka = new StavkaSkiPasa();
        stavka.setSkiPas(skiPas);
        try {
            List<OpstiDomenskiObjekat> listaStavkiOdo = b.pronadjiSlogove(stavka);
            List<StavkaSkiPasa> stavkeSkiPasa = new ArrayList<>();
            for (OpstiDomenskiObjekat opstiDomenskiObjekat : listaStavkiOdo) {
                stavkeSkiPasa.add((StavkaSkiPasa) opstiDomenskiObjekat);
            }
            skiPas.setStavkeSkiPasa(stavkeSkiPasa);
        } catch (Exception ex) {
            throw ex;
        }

    }

    @Override
    public void proveriPreduslove() throws Exception {
    }

}
