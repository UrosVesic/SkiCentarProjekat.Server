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
import rs.ac.bg.fon.np.sc.commonLib.domen.Zicara;
import rs.ac.bg.fon.np.sc.server.so.OpstaSOTest;

/**
 *
 * @author UrosVesic
 */
public class ZapamtiZicaruSOTest extends OpstaSOTest {

    @BeforeEach
    public void setUp() {
        testSO = new ZapamtiZicaruSO(brokerBP, null);
    }

    @AfterEach
    public void tearDown() {
        testSO = null;
    }

    @Override
    @Test
    public void testIzvrsiOperaciju() throws Exception {
        Zicara zicara = new Zicara();
        testSO.setOdo(zicara);
        testSO.izvrsiOperaciju();
        ArgumentCaptor<Zicara> captor
                = ArgumentCaptor.forClass(Zicara.class);
        Mockito.verify(brokerBP).zapamtiSlog(captor.capture());
        Assertions.assertThat(captor.getValue()).isEqualTo(zicara);
    }

    @Test
    public void testIzvrsiOperacijuBrokerException() throws Exception {
        Zicara zicara = new Zicara();
        Mockito.doThrow(Exception.class).when(brokerBP).zapamtiSlog(zicara);
        Assertions.assertThatThrownBy(() -> testSO.izvrsiOperaciju()).isInstanceOf(Exception.class);
    }
}
