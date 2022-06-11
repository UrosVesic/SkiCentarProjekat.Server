/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.fon.np.sc.server.so.impl;

import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import rs.ac.bg.fon.np.sc.commonLib.domen.OpstiDomenskiObjekat;
import rs.ac.bg.fon.np.sc.commonLib.domen.SkiCentar;
import rs.ac.bg.fon.np.sc.commonLib.domen.Staza;
import rs.ac.bg.fon.np.sc.commonLib.domen.Zicara;
import rs.ac.bg.fon.np.sc.commonLib.dto.SkiCentarDto;
import rs.ac.bg.fon.np.sc.server.so.OpstaSOTest;

/**
 *
 * @author UrosVesic
 */
public class ZapamtiSkiCentarDetaljnijeTest extends OpstaSOTest {

    @BeforeEach
    public void setUp() {
        testSO = new ZapamtiSkiCentarDetaljnije(brokerBP, (SkiCentarDto) null);
    }

    @AfterEach
    public void tearDown() {
        testSO = null;

    }

    @Override
    @Test
    public void testIzvrsiOperaciju() throws Exception {
        SkiCentar sc = new SkiCentar(123);

        Staza s1 = new Staza(1);
        Staza s2 = new Staza(2);
        Staza s3 = new Staza(3);
        List<Staza> staze = new ArrayList<>();
        List<OpstiDomenskiObjekat> stazeIzBaze = new ArrayList<>();
        stazeIzBaze.add(s3);
        staze.add(s1);
        staze.add(s2);

        Zicara z1 = new Zicara(1, "Z1");
        Zicara z2 = new Zicara(2, "Z2");
        Zicara z3 = new Zicara(3, "Z3");
        List<Zicara> zicare = new ArrayList<>();
        List<OpstiDomenskiObjekat> zicareIzBaze = new ArrayList<>();
        zicareIzBaze.add(z3);
        zicare.add(z1);
        zicare.add(z2);

        SkiCentarDto scDto = new SkiCentarDto(sc, staze, zicare);
        ((ZapamtiSkiCentarDetaljnije) testSO).setDto(scDto);

        BDDMockito.given(brokerBP.daLiPostojiSlog(s1)).willReturn(false);
        BDDMockito.given(brokerBP.daLiPostojiSlog(s2)).willReturn(true);
        BDDMockito.given(brokerBP.daLiPostojiSlog(z1)).willReturn(false);
        BDDMockito.given(brokerBP.daLiPostojiSlog(z2)).willReturn(true);
        BDDMockito.given(brokerBP.vratiSve(new Staza())).willReturn(stazeIzBaze);
        BDDMockito.given(brokerBP.vratiSve(new Zicara())).willReturn(zicareIzBaze);

        testSO.opsteIzvrsenjeSo();
        ArgumentCaptor<OpstiDomenskiObjekat> captorPromeniSlog
                = ArgumentCaptor.forClass(OpstiDomenskiObjekat.class);
        ArgumentCaptor<OpstiDomenskiObjekat> captorDaLiPostojiSlog
                = ArgumentCaptor.forClass(OpstiDomenskiObjekat.class);
        ArgumentCaptor<OpstiDomenskiObjekat> captorzapamtiSlogGenerisiKljuc
                = ArgumentCaptor.forClass(OpstiDomenskiObjekat.class);
        ArgumentCaptor<OpstiDomenskiObjekat> captorObrisiSlog
                = ArgumentCaptor.forClass(OpstiDomenskiObjekat.class);
        List<OpstiDomenskiObjekat> arguments;

        Mockito.verify(brokerBP, Mockito.times(4)).daLiPostojiSlog(captorDaLiPostojiSlog.capture());
        arguments = captorDaLiPostojiSlog.getAllValues();
        Assertions.assertThat(arguments.get(0)).isEqualTo(s1);
        Assertions.assertThat(arguments.get(1)).isEqualTo(s2);
        Assertions.assertThat(arguments.get(2)).isEqualTo(z1);
        Assertions.assertThat(arguments.get(3)).isEqualTo(z2);

        Mockito.verify(brokerBP, Mockito.times(3)).promeniSlog(captorPromeniSlog.capture());
        arguments = captorPromeniSlog.getAllValues();
        Assertions.assertThat(arguments.get(0)).isEqualTo(sc);
        Assertions.assertThat(arguments.get(1)).isEqualTo(s2);
        Assertions.assertThat(arguments.get(2)).isEqualTo(z2);

        Mockito.verify(brokerBP, Mockito.times(2)).zapamtiSlogGenerisiKljuc(captorzapamtiSlogGenerisiKljuc.capture());
        arguments = captorzapamtiSlogGenerisiKljuc.getAllValues();
        Assertions.assertThat(arguments.get(0)).isEqualTo(s1);
        Assertions.assertThat(arguments.get(1)).isEqualTo(z1);

        Mockito.verify(brokerBP, Mockito.times(2)).obrisiSlog(captorObrisiSlog.capture());
        arguments = captorObrisiSlog.getAllValues();
        Assertions.assertThat(arguments.get(0)).isEqualTo(s3);
        Assertions.assertThat(arguments.get(1)).isEqualTo(z3);
    }

    @Test
    public void testIzvrsiOperacijuBrokerException() throws Exception {
        Mockito.lenient().when(brokerBP.vratiSve(new Staza())).thenThrow(Exception.class);
        Assertions.assertThatThrownBy(() -> testSO.izvrsiOperaciju()).isInstanceOf(Exception.class);
    }

}
