/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.fon.np.sc.server.so.impl;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import rs.ac.bg.fon.np.sc.commonLib.domen.OpstiDomenskiObjekat;
import rs.ac.bg.fon.np.sc.commonLib.domen.SkiCentar;
import rs.ac.bg.fon.np.sc.commonLib.domen.Staza;
import rs.ac.bg.fon.np.sc.commonLib.domen.Zicara;
import rs.ac.bg.fon.np.sc.server.so.OpstaSOTest;

/**
 *
 * @author UrosVesic
 */
public class UcitajSkiCentarSOTest extends OpstaSOTest {

    @BeforeEach
    public void setUp() {
        testSO = new UcitajSkiCentarSO(brokerBP, null);
    }

    @AfterEach
    public void tearDown() {
        testSO = null;
    }

    @Override
    @Test
    public void testIzvrsiOperaciju() throws Exception {
        SkiCentar sc = new SkiCentar(1);
        Staza staza = new Staza(0, null, null, null, sc);
        Zicara zicara = new Zicara(0, null, null, 0, true, sc);
        testSO.setOdo(sc);
        testSO.opsteIzvrsenjeSo();

        ArgumentCaptor<SkiCentar> captor
                = ArgumentCaptor.forClass(SkiCentar.class);
        ArgumentCaptor<OpstiDomenskiObjekat> captorZicareStaze
                = ArgumentCaptor.forClass(OpstiDomenskiObjekat.class);

        Mockito.verify(brokerBP).pronadjiSlogPoKljucu(captor.capture());
        Assertions.assertThat(captor.getValue()).isEqualTo(sc);

        Mockito.verify(brokerBP, Mockito.times(2)).pronadjiSlogove(captorZicareStaze.capture());
        List<OpstiDomenskiObjekat> arguments = captorZicareStaze.getAllValues();
        Assertions.assertThat(arguments.get(0)).isEqualTo(staza);
        Assertions.assertThat(arguments.get(1)).isEqualTo(zicara);

    }

}
