/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.fon.np.sc.server.so.impl;

import java.math.BigDecimal;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import rs.ac.bg.fon.np.sc.commonLib.domen.SkiCentar;
import rs.ac.bg.fon.np.sc.commonLib.domen.SkiKarta;
import rs.ac.bg.fon.np.sc.commonLib.domen.VrstaSkiKarte;
import rs.ac.bg.fon.np.sc.commonLib.validator.ValidationException;
import rs.ac.bg.fon.np.sc.server.so.OpstaSOTest;

/**
 *
 * @author UrosVesic
 */
public class ZapamtiSkiKartuSOTest extends OpstaSOTest {

    @BeforeEach
    public void setUp() {
        testSO = new ZapamtiSkiKartuSO(brokerBP, null);
    }

    @AfterEach
    public void tearDown() {
        testSO = null;
    }

    @Override
    @Test
    public void testIzvrsiOperaciju() throws Exception {
        SkiKarta skiKarta = new SkiKarta();
        testSO.setOdo(skiKarta);
        testSO.izvrsiOperaciju();
        ArgumentCaptor<SkiKarta> captor
                = ArgumentCaptor.forClass(SkiKarta.class);
        Mockito.verify(brokerBP).zapamtiSlogGenerisiKljuc(captor.capture());
        Assertions.assertThat(captor.getValue()).isEqualTo(skiKarta);
    }

    @Test
    public void testIzvrsiOperacijuBrokerException() throws Exception {
        SkiKarta skiKarta = new SkiKarta();
        Mockito.doThrow(Exception.class).when(brokerBP).zapamtiSlogGenerisiKljuc(skiKarta);
        Assertions.assertThatThrownBy(() -> testSO.izvrsiOperaciju()).isInstanceOf(Exception.class);
    }

    @Test
    public void proveriPredusloveTest() throws ValidationException {
        SkiKarta sk = new SkiKarta(1, VrstaSkiKarte.TRODNEVNA, BigDecimal.TEN, new SkiCentar());
        testSO.setOdo(sk);
        testSO.proveriPreduslove();
    }

    @Test
    public void proveriPreduslovePogresanFormatSezoneTest() throws ValidationException {
        SkiKarta sk = new SkiKarta(1, VrstaSkiKarte.TRODNEVNA, BigDecimal.ZERO, new SkiCentar());
        testSO.setOdo(sk);
        Assertions.assertThatThrownBy(() -> testSO.proveriPreduslove()).isInstanceOf(ValidationException.class).hasMessage("Cena mora biti veca od 0");
    }
}
