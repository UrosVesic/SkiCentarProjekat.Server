/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.fon.np.sc.server.so.impl;

import java.util.List;
import java.util.stream.Collectors;
import rs.ac.bg.fon.np.sc.commonLib.dto.SkiCentarDto;
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
public class UcitajSkiCentarSO extends OpstaSO {

    private SkiCentarDto dto;

    public UcitajSkiCentarSO(BrokerBP b, OpstiDomenskiObjekat odo) {
        super(b, odo);
    }

    public SkiCentarDto getDto() {
        return dto;
    }

    @Override
    public void izvrsiOperaciju() throws Exception {
        odo = b.pronadjiSlogPoKljucu(odo);

        Staza staza = new Staza(0, null, null, null, (SkiCentar) odo);
        List<OpstiDomenskiObjekat> listaStazaOdo = b.pronadjiSlogove(staza);

        Zicara zicara = new Zicara(0, null, null, 0, true, (SkiCentar) odo);
        List<OpstiDomenskiObjekat> listaZicaraOdo = b.pronadjiSlogove(zicara);

        List<Staza> staze = listaStazaOdo.stream().map(e -> (Staza) e).collect(Collectors.toList());
        List<Zicara> zicare = listaZicaraOdo.stream().map(e -> (Zicara) e).collect(Collectors.toList());

        dto = new SkiCentarDto();
        dto.setSkiCentar((SkiCentar) odo);
        dto.setStaze(staze);
        dto.setZicare(zicare);
    }

    @Override
    protected void proveriPreduslove() throws ValidationException {
    }

}
