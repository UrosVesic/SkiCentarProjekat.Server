/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.fon.np.sc.server.so.impl;

import java.util.ArrayList;
import java.util.List;
import rs.ac.bg.fon.np.sc.commonLib.domen.OpstiDomenskiObjekat;
import rs.ac.bg.fon.np.sc.commonLib.domen.SkiPas;
import rs.ac.bg.fon.np.sc.commonLib.domen.StavkaSkiPasa;
import rs.ac.bg.fon.np.sc.server.db.BrokerBP;
import rs.ac.bg.fon.np.sc.server.so.OpstaSO;

/**
 * Klasa koja predstavlja sistemsku operaciju ucitavanje ski pasa. Nasledjuje klasu OpstaSO.
 * @see rs.ac.bg.fon.np.sc.server.so.OpstaSO
 * @author UrosVesic
 */
public class UcitajSkiPasSO extends OpstaSO {

    public UcitajSkiPasSO(BrokerBP b, OpstiDomenskiObjekat odo) {
        super(b, odo);
    }
    /**
     * Izvrsava sistemsku operaciju - ucitava ski pas
     * @throws Exception ako ne postoji ski pas po zadatom kriterijumu ili ga nije moguce ucitati iz baze
     */
    @Override
    public void izvrsiOperaciju() throws Exception {
        odo = b.pronadjiSlogPoKljucu(odo);
        if (odo == null) {
            throw new Exception("Nije pronadjen ski pas sa zadatim kljucem");
        }
        SkiPas skiPas = (SkiPas) odo;
        StavkaSkiPasa stavka = new StavkaSkiPasa();
        stavka.setSkiPas(skiPas);
        List<OpstiDomenskiObjekat> listaStavkiOdo = b.pronadjiSlogove(stavka);
        List<StavkaSkiPasa> stavkeSkiPasa = new ArrayList<>();
        for (OpstiDomenskiObjekat opstiDomenskiObjekat : listaStavkiOdo) {
            stavkeSkiPasa.add((StavkaSkiPasa) opstiDomenskiObjekat);
        }
        skiPas.setStavkeSkiPasa(stavkeSkiPasa);
    }

    @Override
    public void proveriPreduslove() {
    }

}
