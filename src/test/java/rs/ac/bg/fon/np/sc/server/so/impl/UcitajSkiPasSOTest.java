/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.fon.np.sc.server.so.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import rs.ac.bg.fon.np.sc.commonlib.domen.Kupac;
import rs.ac.bg.fon.np.sc.commonlib.domen.OpstiDomenskiObjekat;
import rs.ac.bg.fon.np.sc.commonlib.domen.SkiPas;
import rs.ac.bg.fon.np.sc.commonlib.domen.StavkaSkiPasa;
import rs.ac.bg.fon.np.sc.server.so.OpstaSOTest;

/**
 *
 * @author UrosVesic
 */
public class UcitajSkiPasSOTest extends OpstaSOTest {

    @BeforeEach
    public void setUp() {
        testSO = new UcitajSkiPasSO(brokerBP, null);
    }

    @AfterEach
    public void tearDown() {
        testSO = null;
    }

    @Override
    @Test
    public void testIzvrsiOperaciju() throws Exception {
        SkiPas skiPas = new SkiPas();
        List<OpstiDomenskiObjekat> stavke = new ArrayList<OpstiDomenskiObjekat>() {
            {
                add(new StavkaSkiPasa(1));
                add(new StavkaSkiPasa(2));

            }
        };
        SkiPas pronadjeni = new SkiPas(1, BigDecimal.ONE, new Kupac(), new Date(), "2021/2022", null);
        testSO.setOdo(skiPas);
        StavkaSkiPasa stavka = new StavkaSkiPasa();
        stavka.setSkiPas(pronadjeni);
        BDDMockito.given(brokerBP.pronadjiSlogPoKljucu(skiPas)).willReturn(pronadjeni);
        BDDMockito.given(brokerBP.pronadjiSlogove(stavka)).willReturn(stavke);
        testSO.izvrsiOperaciju();

        ArgumentCaptor<OpstiDomenskiObjekat> captor
                = ArgumentCaptor.forClass(OpstiDomenskiObjekat.class);
        Mockito.verify(brokerBP).pronadjiSlogPoKljucu(captor.capture());
        Assertions.assertThat(captor.getValue()).isEqualTo(skiPas);
        Mockito.verify(brokerBP).pronadjiSlogove(captor.capture());
        StavkaSkiPasa uhvacena = (StavkaSkiPasa) captor.getValue();
        Assertions.assertThat(uhvacena.getSkiPas()).isEqualTo(pronadjeni);
        Assertions.assertThat(testSO.getOdo()).isEqualTo(pronadjeni);
        pronadjeni = (SkiPas) testSO.getOdo();
        Assertions.assertThat(pronadjeni.getStavkeSkiPasa()).isEqualTo(stavke);

    }

    @Test
    public void testIzvrsiOperacijuNePostojiSkiPas() {
        Assertions.assertThatThrownBy(() -> testSO.izvrsiOperaciju()).isInstanceOf(Exception.class).hasMessage("Nije pronadjen ski pas sa zadatim kljucem");
    }

    @Test
    public void testIzvrsiOperacijuBrokerException() throws Exception {
        BDDMockito.given(brokerBP.pronadjiSlogPoKljucu(new SkiPas())).willThrow(Exception.class);
        Assertions.assertThatThrownBy(() -> testSO.opsteIzvrsenjeSo()).isInstanceOf(Exception.class);

    }

    @Test
    public void testIzvrsiOperacijuBrokerExceptionPronadjiSlogove() throws Exception {
        BDDMockito.given(brokerBP.pronadjiSlogPoKljucu(new StavkaSkiPasa())).willThrow(Exception.class);
        Assertions.assertThatThrownBy(() -> testSO.opsteIzvrsenjeSo()).isInstanceOf(Exception.class);

    }

}
