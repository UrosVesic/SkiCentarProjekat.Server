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
import rs.ac.bg.fon.np.sc.commonlib.domen.Kupac;
import rs.ac.bg.fon.np.sc.commonlib.domen.OpstiDomenskiObjekat;
import rs.ac.bg.fon.np.sc.commonlib.domen.Staza;
import rs.ac.bg.fon.np.sc.server.so.OpstaSOTest;

/**
 *
 * @author UrosVesic
 */
public class UcitajListuKupacaSOTest extends OpstaSOTest {

    @BeforeEach
    public void setUp() {
        testSO = new UcitajListuKupacaSO(brokerBP, null);
    }

    @AfterEach
    public void tearDown() {
        testSO = null;
    }

    @Override
    @Test
    public void testIzvrsiOperaciju() throws Exception {
        Kupac k = new Kupac();
        testSO.setOdo(k);
        List<OpstiDomenskiObjekat> kupci = new ArrayList<>();
        kupci.add(k);
        BDDMockito.given(brokerBP.vratiSve(k)).willReturn(kupci);
        testSO.izvrsiOperaciju();

        ArgumentCaptor<Kupac> captor
                = ArgumentCaptor.forClass(Kupac.class);

        Mockito.verify(brokerBP).vratiSve(captor.capture());
        Assertions.assertThat(captor.getValue()).isEqualTo(k);

    }

    @Test
    public void testIzvrsiOperacijuNePostojeKupci() throws Exception {
        Assertions.assertThatThrownBy(() -> testSO.izvrsiOperaciju()).isInstanceOf(Exception.class).hasMessage("Ne postoje kupci u bazi");
    }

    @Test
    public void testIzvrsiOperacijuBrokerException() throws Exception {

        BDDMockito.given(brokerBP.vratiSve(new Kupac())).willThrow(Exception.class);
        Assertions.assertThatThrownBy(() -> testSO.izvrsiOperaciju()).isInstanceOf(Exception.class);
    }

}
