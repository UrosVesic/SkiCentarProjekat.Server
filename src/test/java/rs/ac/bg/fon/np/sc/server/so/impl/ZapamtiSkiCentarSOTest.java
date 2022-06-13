/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.fon.np.sc.server.so.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import rs.ac.bg.fon.np.sc.commonLib.domen.SkiCentar;
import rs.ac.bg.fon.np.sc.commonLib.validator.ValidationException;
import rs.ac.bg.fon.np.sc.server.so.OpstaSOTest;

/**
 *
 * @author UrosVesic
 */
public class ZapamtiSkiCentarSOTest extends OpstaSOTest {

    @BeforeEach
    public void setUp() {
        testSO = new ZapamtiSkiCentarSO(brokerBP, null);
    }

    @AfterEach
    public void tearDown() {
        testSO = null;

    }

    @Override
    @Test
    public void testIzvrsiOperaciju() throws Exception {
        SkiCentar skiCentar = new SkiCentar();
        testSO.setOdo(skiCentar);
        testSO.izvrsiOperaciju();
        ArgumentCaptor<SkiCentar> captor
                = ArgumentCaptor.forClass(SkiCentar.class);
        Mockito.verify(brokerBP).zapamtiSlogGenerisiKljuc(captor.capture());
        Assertions.assertThat(captor.getValue()).isEqualTo(skiCentar);
    }

    @Test
    public void testIzvrsiOperacijuBrokerException() throws Exception {
        SkiCentar skiCentar = new SkiCentar();
        Mockito.doThrow(Exception.class).when(brokerBP).zapamtiSlogGenerisiKljuc(skiCentar);
        Assertions.assertThatThrownBy(() -> testSO.izvrsiOperaciju()).isInstanceOf(Exception.class);
    }

    @Test
    public void proveriPredusloveTest() throws ValidationException {
        SkiCentar sc = new SkiCentar(1, "Sc", "Pl", "10-12");
        testSO.setOdo(sc);
        testSO.proveriPreduslove();
    }

    @Test
    public void proveriPreduslovePogresanFormatRMTest() throws ValidationException {
        SkiCentar sc = new SkiCentar(1, "SC", "Pl", "10_12");
        testSO.setOdo(sc);
        Assertions.assertThatThrownBy(() -> testSO.proveriPreduslove()).isInstanceOf(ValidationException.class).hasMessage("Pogresan format radnog vremena");
    }

    @Test
    public void proveriPredusloveNullPraznaPolja() {
        SkiCentar sc = new SkiCentar();
        testSO.setOdo(sc);
        Assertions.assertThatThrownBy(() -> testSO.proveriPreduslove()).isInstanceOf(ValidationException.class).hasMessage("Polje nazivSkiCentra je obavezno\n"
                + "Polje nazivPlanine je obavezno\n"
                + "Polje radnoVreme je obavezno");
    }

}
