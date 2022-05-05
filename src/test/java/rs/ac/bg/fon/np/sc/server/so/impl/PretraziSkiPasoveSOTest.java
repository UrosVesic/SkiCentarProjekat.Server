/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.fon.np.sc.server.so.impl;

import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.ac.bg.fon.np.sc.commonlib.domen.OpstiDomenskiObjekat;
import rs.ac.bg.fon.np.sc.commonlib.domen.SkiPas;
import rs.ac.bg.fon.np.sc.server.db.BrokerBP;

/**
 *
 * @author UrosVesic
 */
@ExtendWith(MockitoExtension.class)
public class PretraziSkiPasoveSOTest {

    @Mock
    private BrokerBP brokerBP;
    private PretraziSkiPasoveSO testSO;

    @BeforeEach
    public void setUp() {
        testSO = new PretraziSkiPasoveSO(brokerBP, null);

    }

    @AfterEach
    public void tearDown() throws Exception {
        testSO = null;
    }

    @Test
    public void izvrsiOperacijuTest() throws Exception {

        OpstiDomenskiObjekat odo = new SkiPas();
        testSO.setOdo(odo);
        List<OpstiDomenskiObjekat> lista = new ArrayList<>();
        lista.add(odo);

        BDDMockito.given(brokerBP.pronadjiSlogove(odo)).willReturn(lista);

        testSO.izvrsiOperaciju();

        Mockito.verify(brokerBP).pronadjiSlogove(odo);

        ArgumentCaptor<SkiPas> skiPasArgumentCaptor
                = ArgumentCaptor.forClass(SkiPas.class);
        Mockito.verify(brokerBP)
                .pronadjiSlogove(skiPasArgumentCaptor.capture());

        SkiPas uhvacen = skiPasArgumentCaptor.getValue();
        assertThat(uhvacen).isEqualTo(odo);
    }

    @Test
    public void testIzvrsiOperacijuNePostojiSkiPas() throws Exception {

        OpstiDomenskiObjekat odo = new SkiPas();
        testSO.setOdo(odo);

        assertThatThrownBy(() -> testSO.izvrsiOperaciju())
                .isInstanceOf(Exception.class)
                .hasMessage("Ne postoji ski pas za zadatog kupca");

    }

}
