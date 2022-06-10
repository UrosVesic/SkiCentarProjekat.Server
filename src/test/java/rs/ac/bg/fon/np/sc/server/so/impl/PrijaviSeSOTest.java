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
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import rs.ac.bg.fon.np.sc.commonLib.domen.Korisnik;
import rs.ac.bg.fon.np.sc.server.so.OpstaSOTest;
import static org.assertj.core.api.Assertions.*;

/**
 *
 * @author UrosVesic
 */
public class PrijaviSeSOTest extends OpstaSOTest {

    public PrijaviSeSOTest() {
    }

    @BeforeEach
    public void setUp() {
        testSO = new PrijaviSeSO(brokerBP, null);
    }

    @AfterEach
    public void tearDown() {
        testSO = null;
    }

    @Override
    @Test
    public void testIzvrsiOperaciju() throws Exception {
        Korisnik k = new Korisnik();
        testSO.setOdo(k);

        BDDMockito.given(brokerBP.pronadjiSlogPoKljucu(k)).willReturn(new Korisnik());
        testSO.izvrsiOperaciju();

        ArgumentCaptor<Korisnik> captor= ArgumentCaptor.forClass(Korisnik.class);
        Mockito.verify(brokerBP).pronadjiSlogPoKljucu(captor.capture());
        Korisnik uhvacen = captor.getValue();
        assertThat(uhvacen).isEqualTo(k);

    }

    @Test
    public void testIzvrsiOperacijuNePostojiKorisnik() throws Exception {
        assertThatThrownBy(() -> testSO.izvrsiOperaciju()).isInstanceOf(Exception.class).hasMessage("Ne postoji korisnik u bazi");
    }

}
