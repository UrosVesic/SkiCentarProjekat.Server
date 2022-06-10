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
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import rs.ac.bg.fon.np.sc.commonLib.domen.SkiCentar;
import rs.ac.bg.fon.np.sc.commonLib.domen.Staza;
import rs.ac.bg.fon.np.sc.server.so.OpstaSOTest;

/**
 *
 * @author UrosVesic
 */
public class UcitajStazuSOTest extends OpstaSOTest {

    @BeforeEach
    public void setUp() {
        testSO = new UcitajStazuSO(brokerBP, null);

    }

    @AfterEach
    public void tearDown() {
        testSO = null;
    }

    @Override
    @Test
    public void testIzvrsiOperaciju() throws Exception {
        Staza staza = new Staza(3, null, null, null, null);
        Staza zaVracanje = new Staza(3, "4a", "Krst", "Laka", new SkiCentar());
        testSO.setOdo(staza);

        BDDMockito.given(brokerBP.pronadjiSlogPoKljucu(staza)).willReturn(zaVracanje);
        ArgumentCaptor<Staza> captor = ArgumentCaptor.forClass(Staza.class);
        testSO.izvrsiOperaciju();
        Mockito.verify(brokerBP).pronadjiSlogPoKljucu(captor.capture());
        Assertions.assertThat(captor.getValue()).isEqualTo(staza);
        Assertions.assertThat(testSO.getOdo()).isEqualTo(zaVracanje);
    }

    @Test
    public void testIzvrsiOperacijuNijePronadjenaStaza() throws Exception {
        Staza staza = new Staza(3, null, null, null, null);
        testSO.setOdo(staza);

        Assertions.assertThatThrownBy(() -> testSO.izvrsiOperaciju()).isInstanceOf(Exception.class).hasMessage("Ne postoji staza sa zadatim kljucem");
        Assertions.assertThat(testSO.getOdo()).isEqualTo(null);
    }

    @Test
    public void testIzvrsiOperacijuBrokerException() throws Exception {
        Staza staza = new Staza();
        Mockito.lenient().doThrow(Exception.class).when(brokerBP).zapamtiSlog(staza);
        Assertions.assertThatThrownBy(() -> testSO.izvrsiOperaciju()).isInstanceOf(Exception.class);
    }

}
