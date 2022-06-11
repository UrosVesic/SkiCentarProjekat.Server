/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.fon.np.sc.server.so.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import rs.ac.bg.fon.np.sc.commonLib.domen.OpstiDomenskiObjekat;
import rs.ac.bg.fon.np.sc.commonLib.domen.SkiPas;
import rs.ac.bg.fon.np.sc.commonLib.domen.StavkaSkiPasa;
import rs.ac.bg.fon.np.sc.commonLib.validator.ValidationException;
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
        ArgumentCaptor<OpstiDomenskiObjekat> captorStavke
                = ArgumentCaptor.forClass(OpstiDomenskiObjekat.class);
        BDDMockito.given(skiPas.getStavkeSkiPasa()).willReturn(stavke);
        testSO.izvrsiOperaciju();
        Mockito.verify(brokerBP).zapamtiSlogGenerisiKljuc(captor.capture());
        Assertions.assertThat(captor.getValue()).isEqualTo(skiPas);
        Mockito.verify(brokerBP, Mockito.times(stavke.size())).zapamtiSlog(captorStavke.capture());
        List<OpstiDomenskiObjekat> uhvaceni = captorStavke.getAllValues();
        Assertions.assertThat(uhvaceni.get(0)).isEqualTo(stavka1);
        Assertions.assertThat(uhvaceni.get(1)).isEqualTo(stavka2);

    }

    @Test
    public void testIzvrsiOperacijuBrokerException() throws Exception {
        Mockito.lenient().doThrow(Exception.class).when(brokerBP).zapamtiSlog(skiPas);
        Assertions.assertThatThrownBy(() -> testSO.izvrsiOperaciju()).isInstanceOf(Exception.class);
    }

    @Test
    public void proveriPredusloveTest() throws ValidationException {
        skiPas = new SkiPas(1, BigDecimal.ONE, null, new Date(), "2021/2022", null);
        List<StavkaSkiPasa> stavke = new ArrayList<>();
        stavke.add(new StavkaSkiPasa(skiPas, 1, new Date()));
        skiPas.setStavkeSkiPasa(stavke);
        testSO.setOdo(skiPas);
        testSO.proveriPreduslove();
    }

    @Test
    public void proveriPreduslovePogresanFormatSezoneTest() throws ValidationException {
        skiPas = new SkiPas(1, BigDecimal.ONE, null, new Date(), "2021-2022", null);
        List<StavkaSkiPasa> stavke = new ArrayList<>();
        stavke.add(new StavkaSkiPasa(skiPas, 1, new Date()));
        skiPas.setStavkeSkiPasa(stavke);
        testSO.setOdo(skiPas);
        Assertions.assertThatThrownBy(() -> testSO.proveriPreduslove()).isInstanceOf(ValidationException.class).hasMessage("Nepravilan format sezone");
    }

    @Test
    public void proveriPredusloveNemaStavki() {
        skiPas = new SkiPas(1, BigDecimal.ONE, null, new Date(), "2021/2022", null);
        testSO.setOdo(skiPas);
        Assertions.assertThatThrownBy(() -> testSO.proveriPreduslove()).isInstanceOf(ValidationException.class).hasMessage("Ne moze se sacuvati ski pas bez stavki");
    }

    @Test
    public void proveriPreduslovePogresnaSezona() {
        skiPas = new SkiPas(1, BigDecimal.ONE, null, new Date(), "2020/2021", null);
        List<StavkaSkiPasa> stavke = new ArrayList<>();
        stavke.add(new StavkaSkiPasa(skiPas, 1, new Date()));
        skiPas.setStavkeSkiPasa(stavke);
        testSO.setOdo(skiPas);
        Assertions.assertThatThrownBy(() -> testSO.proveriPreduslove()).isInstanceOf(ValidationException.class).hasMessage("Datum izdavanja ski pasa nije u navedenoj sezoni");
    }

    @Test
    public void proveriPreduslovePogresanDatumStavke() {
        skiPas = new SkiPas(1, BigDecimal.ONE, null, new Date(), "2021/2022", null);
        List<StavkaSkiPasa> stavke = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.set(2019, 1, 1);
        StavkaSkiPasa stavka = new StavkaSkiPasa(skiPas, 1, cal.getTime());
        stavke.add(stavka);
        skiPas.setStavkeSkiPasa(stavke);
        testSO.setOdo(skiPas);
        Assertions.assertThatThrownBy(() -> testSO.proveriPreduslove()).isInstanceOf(ValidationException.class).hasMessage("Stavka " + stavka.getRedniBroj() + ". nije u sezoni za koju se izdaje ski pas");
    }

}
