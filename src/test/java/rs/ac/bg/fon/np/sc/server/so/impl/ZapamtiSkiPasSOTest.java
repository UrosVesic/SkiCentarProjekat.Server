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
import org.mockito.Mock;
import org.mockito.Mockito;
import rs.ac.bg.fon.np.sc.commonlib.domen.OpstiDomenskiObjekat;
import rs.ac.bg.fon.np.sc.commonlib.domen.SkiPas;
import rs.ac.bg.fon.np.sc.commonlib.domen.StavkaSkiPasa;
import rs.ac.bg.fon.np.sc.server.so.OpstaSOTest;

/**
 *
 * @author UrosVesic
 */
public class ZapamtiSkiPasSOTest extends OpstaSOTest {

    @Mock
    private SkiPas skiPas;

    @BeforeEach
    public void setUp() {
        testSO = new ZapamtiSkiPasSO(brokerBP, null);

    }

    @AfterEach
    public void tearDown() {
        testSO = null;
    }

    @Override
    @Test
    public void testIzvrsiOperaciju() throws Exception {
        testSO.setOdo(skiPas);
        StavkaSkiPasa stavka1 = new StavkaSkiPasa(1);
        StavkaSkiPasa stavka2 = new StavkaSkiPasa(2);
        List<StavkaSkiPasa> stavke = new ArrayList<StavkaSkiPasa>() {
            {
                add(stavka1);
                add(stavka2);
            }
        };

        ArgumentCaptor<OpstiDomenskiObjekat> captor
                = ArgumentCaptor.forClass(OpstiDomenskiObjekat.class);
        BDDMockito.given(skiPas.getStavkeSkiPasa()).willReturn(stavke);
        testSO.izvrsiOperaciju();
        Mockito.verify(brokerBP, Mockito.times(stavke.size() + 1)).zapamtiSlog(captor.capture());
        List<OpstiDomenskiObjekat> uhvaceni = captor.getAllValues();
        Assertions.assertThat(uhvaceni.get(0)).isEqualTo(skiPas);
        Assertions.assertThat(uhvaceni.get(1)).isEqualTo(stavka1);
        Assertions.assertThat(uhvaceni.get(2)).isEqualTo(stavka2);

    }

    @Test
    public void testIzvrsiOperacijuBrokerException() throws Exception {
        Mockito.doThrow(Exception.class).when(brokerBP).zapamtiSlog(skiPas);
        Assertions.assertThatThrownBy(() -> testSO.izvrsiOperaciju()).isInstanceOf(Exception.class);
    }

}
