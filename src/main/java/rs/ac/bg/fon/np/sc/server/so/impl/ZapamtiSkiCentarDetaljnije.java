/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.fon.np.sc.server.so.impl;

import java.util.List;
import rs.ac.bg.fon.np.sc.commonlib.dto.SkiCentarDto;
import rs.ac.bg.fon.np.sc.commonlib.domen.OpstiDomenskiObjekat;
import rs.ac.bg.fon.np.sc.commonlib.domen.SkiCentar;
import rs.ac.bg.fon.np.sc.commonlib.domen.Staza;
import rs.ac.bg.fon.np.sc.commonlib.domen.Zicara;
import rs.ac.bg.fon.np.sc.commonlib.validator.ValidationException;
import rs.ac.bg.fon.np.sc.server.db.BrokerBP;
import rs.ac.bg.fon.np.sc.server.so.OpstaSO;

/**
 *
 * @author UrosVesic
 */
public class ZapamtiSkiCentarDetaljnije extends OpstaSO {

    SkiCentarDto dto;

    public ZapamtiSkiCentarDetaljnije(BrokerBP b, SkiCentarDto dto) {
        super(b, null);
        this.dto = dto;
    }

    public ZapamtiSkiCentarDetaljnije(BrokerBP b, OpstiDomenskiObjekat odo) {
        super(b, odo);
    }

    public SkiCentarDto getDto() {
        return dto;
    }

    @Override
    public void izvrsiOperaciju() throws Exception {
        SkiCentar sc = dto.getSkiCentar();
        List<OpstiDomenskiObjekat> stazeIzBaze = b.vratiSve(new Staza());
        List<OpstiDomenskiObjekat> zicareIzBaze = b.vratiSve(new Zicara());
        List<Staza> staze = dto.getStaze();
        List<Zicara> zicare = dto.getZicare();

        b.zapamtiSlog(sc);
        for (Staza staza : staze) {
            if (b.daLiPostojiSlog(staza)) {
                b.promeniSlog(staza);
            } else {
                b.zapamtiSlog(staza);
            }
        }
        for (OpstiDomenskiObjekat stazaIzBaze : stazeIzBaze) {
            if (!staze.contains(stazaIzBaze)) {
                b.obrisiSlog(stazaIzBaze);
            }
        }
        for (Zicara zicara : zicare) {
            if (b.daLiPostojiSlog(zicara)) {
                b.promeniSlog(zicara);
            } else {
                b.zapamtiSlog(zicara);
            }
        }
        for (OpstiDomenskiObjekat zicaraIzBaze : zicareIzBaze) {
            if (!zicare.contains(zicaraIzBaze)) {
                b.obrisiSlog(zicaraIzBaze);
            }
        }
    }

    @Override
    protected void proveriPreduslove() throws ValidationException {
    }

}
