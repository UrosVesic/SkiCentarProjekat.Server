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
import rs.ac.bg.fon.np.sc.commonLib.domen.Staza;
import rs.ac.bg.fon.np.sc.commonLib.domen.Zicara;
import rs.ac.bg.fon.np.sc.commonLib.validator.ValidationException;
import rs.ac.bg.fon.np.sc.server.so.OpstaSOTest;

/**
 *
 * @author UrosVesic
 */
public class ZapamtiStazuSOTest extends OpstaSOTest {

    @BeforeEach
    public void setUp() {
        testSO = new ZapamtiStazuSO(brokerBP, null);

    }

    @AfterEach
    public void tearDown() {
        testSO = null;
    }

    @Override
    @Test
    public void testIzvrsiOperaciju() throws Exception {
        Staza staza = new Staza();
        testSO.setOdo(staza);
        testSO.izvrsiOperaciju();
        ArgumentCaptor<Staza> captor
                = ArgumentCaptor.forClass(Staza.class);
        Mockito.verify(brokerBP).zapamtiSlogGenerisiKljuc(captor.capture());
        Assertions.assertThat(captor.getValue()).isEqualTo(staza);
    }

    @Test
    public void testIzvrsiOperacijuBrokerException() throws Exception {
        Staza staza = new Staza();
        Mockito.doThrow(Exception.class).when(brokerBP).zapamtiSlogGenerisiKljuc(staza);
        Assertions.assertThatThrownBy(() -> testSO.izvrsiOperaciju()).isInstanceOf(Exception.class);
    }

    @Test
    public void proveriPredusloveNullPraznaPolja() {
        Staza s = new Staza();
        testSO.setOdo(s);
        Assertions.assertThatThrownBy(() -> testSO.proveriPreduslove()).isInstanceOf(ValidationException.class).hasMessage("Polje brojStaze je obavezno\n"
                + "Polje nazivStaze je obavezno\n"
                + "Polje tipStaze je obavezno\n"
                + "Polje skiCentar je obavezno");
    }
}
