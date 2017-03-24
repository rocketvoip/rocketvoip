package ch.zhaw.psit4.domain;

import ch.zhaw.psit4.domain.sipclient.SipClient;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jona Braun
 */
public class SipClientTestHelper {

    public static List<SipClient> generateSipClientList(int number, String company) {
        List<SipClient> sipClientList = new ArrayList<>();
        for (int i = 1; i <= number; i++) {
            SipClient sipClient = generateSipClient(i, company);
            sipClientList.add(sipClient);
        }
        return sipClientList;
    }

    public static SipClient generateSipClient(int i, String company) {
        SipClient sipClient = new SipClient();
        sipClient.setCompany(company);
        sipClient.setUsername("User" + i);
        sipClient.setSecret("Secret" + i);
        sipClient.setPhoneNumber(Integer.toString(i));
        return sipClient;
    }
}
