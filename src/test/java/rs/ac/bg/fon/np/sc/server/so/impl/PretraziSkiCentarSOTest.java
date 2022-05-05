/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.fon.np.sc.server.so.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import rs.ac.bg.fon.np.sc.commonlib.domen.SkiCentar;
import rs.ac.bg.fon.np.sc.server.so.OpstaSOTest;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;

/**
 *
 * @author UrosVesic
 */
public class PretraziSkiCentarSOTest extends OpstaSOTest {

    @BeforeEach
    public void setUp() {
        testSO = new PretraziSkiCentarSO(brokerBP, null);
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

        ArgumentCaptor<SkiCentar> skiCentarCaptor = ArgumentCaptor.forClass(SkiCentar.class);
        Mockito.verify(brokerBP).pronadjiSlogUnique(skiCentarCaptor.capture());
        SkiCentar uhvacen = skiCentarCaptor.getValue();
        assertThat(uhvacen).isEqualTo(skiCentar);

    }

    @Test
    public void testIzvrsiOperacijuUhvacenException() throws Exception {

        SkiCentar skiCentar = new SkiCentar();

        Mockito.doThrow(Exception.class).when(brokerBP).pronadjiSlogUnique(skiCentar);

        assertThatThrownBy(() -> testSO.izvrsiOperaciju()).isInstanceOf(Exception.class);

    }

}
