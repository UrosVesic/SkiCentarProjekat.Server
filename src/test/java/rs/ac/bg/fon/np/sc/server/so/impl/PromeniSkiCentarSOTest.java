/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.fon.np.sc.server.so.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import rs.ac.bg.fon.np.sc.commonlib.domen.SkiCentar;
import rs.ac.bg.fon.np.sc.server.so.OpstaSOTest;
import static org.assertj.core.api.Assertions.*;

/**
 *
 * @author UrosVesic
 */
public class PromeniSkiCentarSOTest extends OpstaSOTest {

    @BeforeEach
    public void setUp() {
        testSO = new PromeniSkiCentarSO(brokerBP, null);
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

        ArgumentCaptor<SkiCentar> captor = ArgumentCaptor.forClass(SkiCentar.class);
        Mockito.verify(brokerBP).promeniSlog(captor.capture());
        SkiCentar uhvacen = captor.getValue();
        assertThat(uhvacen).isEqualTo(skiCentar);
    }

    @Test
    public void testIzvrsiOperacijuBrokerException() throws Exception {
        SkiCentar skiCentar = new SkiCentar();
        Mockito.doThrow(Exception.class).when(brokerBP).promeniSlog(skiCentar);
        assertThatThrownBy(() -> testSO.izvrsiOperaciju()).isInstanceOf(Exception.class);
    }
}