/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.fon.np.sc.server.so.impl;

import java.util.List;
import rs.ac.bg.fon.np.sc.commonLib.dto.SkiCentarDto;
import rs.ac.bg.fon.np.sc.commonLib.domen.OpstiDomenskiObjekat;
import rs.ac.bg.fon.np.sc.commonLib.domen.SkiCentar;
import rs.ac.bg.fon.np.sc.commonLib.domen.Staza;
import rs.ac.bg.fon.np.sc.commonLib.domen.Zicara;
import rs.ac.bg.fon.np.sc.commonLib.validator.ValidationException;
import rs.ac.bg.fon.np.sc.commonLib.validator.ValidationRuntimeException;
import rs.ac.bg.fon.np.sc.commonLib.validator.Validator;
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

    public void setDto(SkiCentarDto dto) {
        this.dto = dto;
    }

    @Override
    public void izvrsiOperaciju() throws Exception {
        SkiCentar sc = dto.getSkiCentar();
        List<OpstiDomenskiObjekat> stazeIzBaze = b.vratiSve(new Staza());
        List<OpstiDomenskiObjekat> zicareIzBaze = b.vratiSve(new Zicara());
        List<Staza> staze = dto.getStaze();
        List<Zicara> zicare = dto.getZicare();

        b.promeniSlog(sc);
        for (Staza staza : staze) {
            if (b.daLiPostojiSlog(staza)) {
                b.promeniSlog(staza);
            } else {
                b.zapamtiSlogGenerisiKljuc(staza);
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
                b.zapamtiSlogGenerisiKljuc(zicara);
            }
        }
        for (OpstiDomenskiObjekat zicaraIzBaze : zicareIzBaze) {
            if (!zicare.contains(zicaraIzBaze)) {
                b.obrisiSlog(zicaraIzBaze);
            }
        }
    }

    @Override
    public void proveriPreduslove() throws ValidationException, ValidationRuntimeException {
        Validator.startValidation().validateWorkingHoursFormat(dto.getSkiCentar().getRadnoVreme(), "Pogresan format radnog vremena ski centra").validateFieldsNotNullOrEmpty(dto.getSkiCentar());
        dto.getZicare().forEach((zicara) -> {
            Validator.startValidation()
                    .validateWorkingHoursFormat(zicara.getRadnoVreme(), "Pogresan format radnog vremena kod zicare " + zicara.getNazivZicare())
                    .validateGreaterThanZero(zicara.getKapacitet(), "Kapacitet mora biti veci od 0 kod zicare " + zicara.getNazivZicare()).throwIfInvalideRuntime();
        });
    }

}
