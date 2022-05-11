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
import rs.ac.bg.fon.np.sc.commonlib.domen.Staza;
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
        Mockito.verify(brokerBP).zapamtiSlog(captor.capture());
        Assertions.assertThat(captor.getValue()).isEqualTo(staza);
    }

    @Test
    public void testIzvrsiOperacijuBrokerException() throws Exception {
        Staza staza = new Staza();
        Mockito.doThrow(Exception.class).when(brokerBP).zapamtiSlog(staza);
        Assertions.assertThatThrownBy(() -> testSO.izvrsiOperaciju()).isInstanceOf(Exception.class);
    }
}
