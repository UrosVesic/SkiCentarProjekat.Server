/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.fon.np.sc.server.so.impl;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import rs.ac.bg.fon.np.sc.commonLib.domen.OpstiDomenskiObjekat;
import rs.ac.bg.fon.np.sc.commonLib.domen.Staza;
import rs.ac.bg.fon.np.sc.server.so.OpstaSOTest;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 *
 * @author UrosVesic
 */
public class PretraziStazeSOTest extends OpstaSOTest {

    @BeforeEach
    public void setUp() {
        testSO = new PretraziStazeSO(brokerBP, null);
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
        List<OpstiDomenskiObjekat> lista = new ArrayList<>();
        lista.add(staza);

        BDDMockito.given(brokerBP.pronadjiSlogove(staza)).willReturn(lista);
        testSO.izvrsiOperaciju();

        ArgumentCaptor<Staza> captor
                = ArgumentCaptor.forClass(Staza.class);
        Mockito.verify(brokerBP)
                .pronadjiSlogove(captor.capture());
        Staza uhvacena = captor.getValue();
        assertThat(uhvacena).isEqualTo(staza);
    }

    @Test
    public void testIzvrsiOperacijuNePostojiStaza() {
        assertThatThrownBy(() -> testSO.izvrsiOperaciju()).isInstanceOf(Exception.class).hasMessage("Nisu pronadjene staze");
    }

}
