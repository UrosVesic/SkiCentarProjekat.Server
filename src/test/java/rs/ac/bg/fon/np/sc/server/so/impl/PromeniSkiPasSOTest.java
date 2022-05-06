/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.fon.np.sc.server.so.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
import rs.ac.bg.fon.np.sc.commonlib.domen.StavkaSkiPasa;
import rs.ac.bg.fon.np.sc.server.so.OpstaSOTest;
import static org.assertj.core.api.Assertions.*;

/**
 *
 * @author UrosVesic
 */
@ExtendWith(MockitoExtension.class)
public class PromeniSkiPasSOTest extends OpstaSOTest {

    @Mock
    private SkiPas skiPas;

    @BeforeEach
    public void setUp() {
        testSO = new PromeniSkiPasSO(brokerBP, null);
    }

    @AfterEach
    public void tearDown() {
        testSO = null;
    }

    @Override
    @Test
    public void testIzvrsiOperaciju() throws Exception {
        StavkaSkiPasa stavka = new StavkaSkiPasa();
        stavka.setSkiPas(skiPas);
        StavkaSkiPasa stavkaIzBaze = new StavkaSkiPasa(null, 123, BigDecimal.ZERO, null, null, null);
        stavkaIzBaze.setSkiPas(skiPas);

        List<OpstiDomenskiObjekat> stavkeIzBaze = new ArrayList<>();
        List<StavkaSkiPasa> listaStavki = new ArrayList<>();
        stavkeIzBaze.add(stavkaIzBaze);
        listaStavki.add(stavka);

        BDDMockito.given(skiPas.getStavkeSkiPasa()).willReturn(listaStavki);
        BDDMockito.given(brokerBP.pronadjiSlogove(stavka)).willReturn(stavkeIzBaze);

        testSO.setOdo(skiPas);
        testSO.izvrsiOperaciju();

        Mockito.verify(brokerBP).promeniSlog(skiPas);
        Mockito.verify(brokerBP).pronadjiSlogove(stavka);
        Mockito.verify(brokerBP).zapamtiSlog(stavka);

    }

    @Test
    public void testIzvrsiOperacijuPostojecaStavka() throws Exception {
        List<OpstiDomenskiObjekat> stavkeIzBaze = new ArrayList<>();
        List<StavkaSkiPasa> listaStavki = new ArrayList<>();

        StavkaSkiPasa stavka = new StavkaSkiPasa();
        stavka.setSkiPas(skiPas);
        StavkaSkiPasa stavkaIzBaze = stavka;
        stavkaIzBaze.setSkiPas(skiPas);

        stavkeIzBaze.add(stavkaIzBaze);
        listaStavki.add(stavka);

        BDDMockito.given(skiPas.getStavkeSkiPasa()).willReturn(listaStavki);
        BDDMockito.given(brokerBP.pronadjiSlogove(stavka)).willReturn(stavkeIzBaze);
        BDDMockito.given(brokerBP.daLiPostojiSlog(stavka)).willReturn(true);

        testSO.setOdo(skiPas);
        testSO.izvrsiOperaciju();

        ArgumentCaptor<OpstiDomenskiObjekat> captor
                = ArgumentCaptor.forClass(OpstiDomenskiObjekat.class);

        Mockito.verify(brokerBP, Mockito.times(2)).promeniSlog(captor.capture());
        List<OpstiDomenskiObjekat> uhvaceni = captor.getAllValues();
        assertThat(uhvaceni.get(0)).isEqualTo(skiPas);
        assertThat(uhvaceni.get(1)).isEqualTo(stavka);

        Mockito.verify(brokerBP).pronadjiSlogove(captor.capture());

        assertThat(captor.getValue()).isEqualTo(stavka);
    }

    @Test
    public void testIzvrsiOperacijuBrisanjeStavke() throws Exception {
        List<OpstiDomenskiObjekat> stavkeIzBaze = new ArrayList<>();
        List<StavkaSkiPasa> listaStavki = new ArrayList<>();

        StavkaSkiPasa stavka = new StavkaSkiPasa();
        stavka.setSkiPas(skiPas);
        StavkaSkiPasa stavkaIzBaze1 = stavka;
        stavkaIzBaze1.setSkiPas(skiPas);
        StavkaSkiPasa stavkaIzBaze2 = new StavkaSkiPasa(2);
        stavkaIzBaze2.setSkiPas(skiPas);

        listaStavki.add(stavka);
        stavkeIzBaze.add(stavkaIzBaze1);
        stavkeIzBaze.add(stavkaIzBaze2);

        BDDMockito.given(brokerBP.pronadjiSlogove(stavka)).willReturn(stavkeIzBaze);
        BDDMockito.given(skiPas.getStavkeSkiPasa()).willReturn(listaStavki);

        testSO.setOdo(skiPas);
        testSO.izvrsiOperaciju();

        Mockito.verify(brokerBP).obrisiSlog(stavkaIzBaze2);

    }

    @Test
    public void testIzvrsiOperacijuBrokerException() throws Exception {

        StavkaSkiPasa stavka = new StavkaSkiPasa();
        stavka.setSkiPas(skiPas);

        Mockito.lenient().when(brokerBP.pronadjiSlogove(stavka)).thenThrow(Exception.class);

        assertThatThrownBy(() -> testSO.izvrsiOperaciju()).isInstanceOf(Exception.class);
    }

}
