package ch.zhaw.psit4.domain.dialplan;

import ch.zhaw.psit4.domain.sipclient.SipClient;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Jona Braun
 */
public class DialPlanConfigurationChanSipTest {

    @Test
    public void generateDialPlanConfiguration() throws Exception {
        List<SipClient> sipClientList = generateSipClientList(1, "acme");
        DialPlanConfigurationChanSip dialPlanConfigurationChanSip = new DialPlanConfigurationChanSip();

        String extensionConf = dialPlanConfigurationChanSip.generateDialPlanConfiguration(sipClientList, null);
        String expected = "[simple-dial-plan]\n" +
                "exten=> 1, 1, Dial(SIP/User1-acme, 30)\n\n";

        assertEquals(expected, extensionConf);

    }

    //TODO remove duplicated code
    private List<SipClient> generateSipClientList(int number, String company) {
        List<SipClient> sipClientList = new ArrayList<>();
        for (int i = 1; i <= number; i++) {
            SipClient sipClient = generateSipClient(i, company);
            sipClientList.add(sipClient);
        }
        return sipClientList;
    }

    private SipClient generateSipClient(int i, String company) {
        SipClient sipClient = new SipClient();
        sipClient.setCompany(company);
        sipClient.setUsername("User" + i);
        sipClient.setSecret("Secret" + i);
        sipClient.setPhoneNumber("" + i);
        return sipClient;
    }

}