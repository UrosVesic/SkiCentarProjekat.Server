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
import rs.ac.bg.fon.np.sc.commonlib.domen.OpstiDomenskiObjekat;
import rs.ac.bg.fon.np.sc.commonlib.domen.SkiCentar;
import rs.ac.bg.fon.np.sc.commonlib.domen.SkiKarta;
import rs.ac.bg.fon.np.sc.server.so.OpstaSOTest;

/**
 *
 * @author UrosVesic
 */
public class UcitajListuSkiCentaraSOTest extends OpstaSOTest {

    @BeforeEach
    public void setUp() {
        testSO = new UcitajListuSkiCentaraSO(brokerBP, null);
    }

    @AfterEach
    public void tearDown() {
        testSO = null;
    }

    @Override
    @Test
    public void testIzvrsiOperaciju() throws Exception {
        SkiCentar centar = new SkiCentar();
        testSO.setOdo(centar);
        List<OpstiDomenskiObjekat> centri = new ArrayList<>();
        centri.add(centar);
        BDDMockito.given(brokerBP.vratiSve(centar)).willReturn(centri);
        testSO.izvrsiOperaciju();

        ArgumentCaptor<SkiCentar> captor
                = ArgumentCaptor.forClass(SkiCentar.class);

        Mockito.verify(brokerBP).vratiSve(captor.capture());
        Assertions.assertThat(captor.getValue()).isEqualTo(centar);

    }

    @Test
    public void testIzvrsiOperacijuNePostojeSkiCentri() throws Exception {
        Assertions.assertThatThrownBy(() -> testSO.izvrsiOperaciju()).isInstanceOf(Exception.class).hasMessage("Ne postoji nijedan ski centar u bazi");
    }

    @Test
    public void testIzvrsiOperacijuBrokerException() throws Exception {

        BDDMockito.given(brokerBP.vratiSve(new SkiCentar())).willThrow(Exception.class);
        Assertions.assertThatThrownBy(() -> testSO.izvrsiOperaciju()).isInstanceOf(Exception.class);
    }

}
