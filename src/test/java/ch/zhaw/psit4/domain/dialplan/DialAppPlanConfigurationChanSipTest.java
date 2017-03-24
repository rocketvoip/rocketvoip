package ch.zhaw.psit4.domain.dialplan;

import ch.zhaw.psit4.domain.SipClientTestHelper;
import ch.zhaw.psit4.domain.interfaces.DialPlanConfigurationInterface;
import ch.zhaw.psit4.domain.sipclient.SipClient;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Jona Braun
 */
public class DialAppPlanConfigurationChanSipTest {

    @Test
    public void generateSimpleDialPlanConfigurationOneEntry() throws Exception {
        List<SipClient> sipClientList = SipClientTestHelper.generateSipClientList(1, "acme");
        DialPlanConfigurationInterface dialPlanConfigurationChanSip = new DialPlanConfigurationChanSip();

        String extensionConf = dialPlanConfigurationChanSip.generateDialPlanConfiguration(sipClientList, null);
        String expected = getSimpleDialPlan(1);

        assertEquals(expected, extensionConf);

    }

    @Test
    public void generateSimpleDialPlanConfigurationMultipleEntries() throws Exception {
        List<SipClient> sipClientList = SipClientTestHelper.generateSipClientList(10, "acme");
        DialPlanConfigurationInterface dialPlanConfigurationChanSip = new DialPlanConfigurationChanSip();

        String extensionConf = dialPlanConfigurationChanSip.generateDialPlanConfiguration(sipClientList, null);
        String expected = getSimpleDialPlan(10);

        assertEquals(expected, extensionConf);

    }

    private String getSimpleDialPlan(int number) {
        String simpleDialPlan = "[simple-dial-plan]\n";
        for (int i = 1; i <= number; i++) {

            simpleDialPlan += "exten=> " +
                    i + ", 1, Dial(SIP/User" +
                    i + "-acme, 30)\n";
        }
        return simpleDialPlan + "\n";
    }
}