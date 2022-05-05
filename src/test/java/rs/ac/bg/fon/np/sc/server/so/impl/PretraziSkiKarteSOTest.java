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
import rs.ac.bg.fon.np.sc.commonlib.domen.OpstiDomenskiObjekat;
import rs.ac.bg.fon.np.sc.commonlib.domen.SkiKarta;
import rs.ac.bg.fon.np.sc.server.so.OpstaSOTest;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 *
 * @author UrosVesic
 */
public class PretraziSkiKarteSOTest extends OpstaSOTest {

    @BeforeEach
    public void setUp() {
        testSO = new PretraziSkiKarteSO(brokerBP, null);
    }

    @AfterEach
    public void tearDown() {
        testSO = null;
    }

    @Override
    @Test
    public void testIzvrsiOperaciju() throws Exception {
        SkiKarta skiKarta = new SkiKarta();
        testSO.setOdo(skiKarta);
        List<OpstiDomenskiObjekat> lista = new ArrayList<>();
        lista.add(skiKarta);

        BDDMockito.given(brokerBP.pronadjiSlogove(skiKarta)).willReturn(lista);

        testSO.izvrsiOperaciju();

        ArgumentCaptor<SkiKarta> captor = ArgumentCaptor.forClass(SkiKarta.class);
        Mockito.verify(brokerBP).pronadjiSlogove(captor.capture());
        SkiKarta uhvacena = captor.getValue();
        assertThat(uhvacena).isEqualTo(skiKarta);

    }

    @Test
    public void testIzvrsiOperacijuNePostojiSkiKarta() {
        SkiKarta skiKarta = new SkiKarta();
        testSO.setOdo(skiKarta);

        assertThatThrownBy(() -> testSO.izvrsiOperaciju()).isInstanceOf(Exception.class)
                .hasMessage("Ne postoji nijedna ski karta koja ispunjava uslov");

    }

}
