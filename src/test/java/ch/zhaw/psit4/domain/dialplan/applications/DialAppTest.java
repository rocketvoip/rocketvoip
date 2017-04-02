package ch.zhaw.psit4.domain.dialplan.applications;

import ch.zhaw.psit4.domain.helper.SipClientTestHelper;
import ch.zhaw.psit4.domain.sipclient.SipClient;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Jona Braun
 */
public class DialAppTest {

    private static final String TIMEOUT = "30";
    private final SipClientTestHelper sipClientTestHelper = new SipClientTestHelper();
    private List<SipClient> sipClientList;

    @Test
    public void getApplicationCallOneSIPClient() throws Exception {

        sipClientList = sipClientTestHelper.generateSipClientList(1, "acme");

        DialApp dialApp = new DialApp(DialApp.Technology.SIP, sipClientList, TIMEOUT);

        String dialAppCall = dialApp.getApplicationCall();
        String expected = generateDialAppCall(1, "SIP");

        assertEquals(expected, dialAppCall);
    }

    @Test
    public void getApplicationCallMultipleSIPClients() throws Exception {

        sipClientList = sipClientTestHelper.generateSipClientList(5, "acme");

        DialApp dialApp = new DialApp(DialApp.Technology.SIP, sipClientList, TIMEOUT);

        String dialAppCall = dialApp.getApplicationCall();
        String expected = generateDialAppCall(5, "SIP");

        assertEquals(expected, dialAppCall);
    }

    @Test
    public void getApplicationCallMultiplePSIPClients() throws Exception {

        sipClientList = sipClientTestHelper.generateSipClientList(2, "acme");

        DialApp dialApp = new DialApp(DialApp.Technology.PSIP, sipClientList, TIMEOUT);

        String dialAppCall = dialApp.getApplicationCall();
        String expected = generateDialAppCall(2, "PSIP");

        assertEquals(expected, dialAppCall);
    }


    private String generateDialAppCall(int number, String technology) {
        String dialAppCall = "Dial(";
        for (int i = 1; i <= number; i++) {
            dialAppCall += technology
                    + "/Name" + i + "-acme";
            if (number > i) {
                dialAppCall += "&";
            }
        }
        dialAppCall += ", " + TIMEOUT + ")";
        return dialAppCall;
    }

}