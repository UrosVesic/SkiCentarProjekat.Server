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
import rs.ac.bg.fon.np.sc.commonLib.domen.Kupac;
import rs.ac.bg.fon.np.sc.commonLib.domen.OpstiDomenskiObjekat;
import rs.ac.bg.fon.np.sc.server.so.OpstaSOTest;

/**
 *
 * @author UrosVesic
 */
public class ZapamtiKupcaSOTest extends OpstaSOTest {

    @BeforeEach
    public void setUp() {
        testSO = new ZapamtiKupcaSO(brokerBP, null);
    }

    @AfterEach
    public void tearDown() {
        testSO = null;
    }

    @Override
    @Test
    public void testIzvrsiOperaciju() throws Exception {
        Kupac k = new Kupac(1, "1234", "Uros", "Vesic");
        testSO.setOdo(k);
        ArgumentCaptor<OpstiDomenskiObjekat> captor
                = ArgumentCaptor.forClass(OpstiDomenskiObjekat.class);
        testSO.opsteIzvrsenjeSo();
        Mockito.verify(brokerBP).zapamtiSlogGenerisiKljuc(captor.capture());
        Assertions.assertThat(captor.getValue()).isEqualTo(k);
    }

    @Test
    public void testIzvrsiOperacijuBrokerException() throws Exception {
        Mockito.doThrow(Exception.class).when(brokerBP).zapamtiSlogGenerisiKljuc(new Kupac(1));
        Assertions.assertThatThrownBy(() -> testSO.izvrsiOperaciju()).isInstanceOf(Exception.class);
    }

}
