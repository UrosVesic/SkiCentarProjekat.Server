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
import rs.ac.bg.fon.np.sc.commonLib.domen.OpstiDomenskiObjekat;
import rs.ac.bg.fon.np.sc.commonLib.domen.SkiKarta;
import rs.ac.bg.fon.np.sc.server.so.OpstaSOTest;

/**
 *
 * @author UrosVesic
 */
public class UcitajListuSkiKarataSOTest extends OpstaSOTest {

    @BeforeEach
    public void setUp() {
        testSO = new UcitajListuSkiKarataSO(brokerBP, null);
    }

    @AfterEach
    public void tearDown() {
        testSO = null;
    }

    @Override
    @Test
    public void testIzvrsiOperaciju() throws Exception {
        SkiKarta karta = new SkiKarta();
        testSO.setOdo(karta);
        List<OpstiDomenskiObjekat> karte = new ArrayList<>();
        karte.add(karta);
        BDDMockito.given(brokerBP.vratiSve(karta)).willReturn(karte);
        testSO.izvrsiOperaciju();

        ArgumentCaptor<SkiKarta> captor
                = ArgumentCaptor.forClass(SkiKarta.class);

        Mockito.verify(brokerBP).vratiSve(captor.capture());
        Assertions.assertThat(captor.getValue()).isEqualTo(karta);

    }

    @Test
    public void testIzvrsiOperacijuNeSkiKarte() throws Exception {
        Assertions.assertThatThrownBy(() -> testSO.izvrsiOperaciju()).isInstanceOf(Exception.class).hasMessage("Ne postoje ski karte u bazi");
    }

    @Test
    public void testIzvrsiOperacijuBrokerException() throws Exception {

        BDDMockito.given(brokerBP.vratiSve(new SkiKarta())).willThrow(Exception.class);
        Assertions.assertThatThrownBy(() -> testSO.izvrsiOperaciju()).isInstanceOf(Exception.class);
    }

}
