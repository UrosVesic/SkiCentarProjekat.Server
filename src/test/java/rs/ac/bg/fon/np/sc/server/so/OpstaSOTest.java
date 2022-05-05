/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.fon.np.sc.server.so;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.ac.bg.fon.np.sc.server.db.BrokerBP;

/**
 *
 * @author UrosVesic
 */
@ExtendWith(MockitoExtension.class)
public abstract class OpstaSOTest {

    @Mock
    protected BrokerBP brokerBP;
    protected OpstaSO testSO;

    public abstract void testIzvrsiOperaciju() throws Exception;

}
