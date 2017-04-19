package ch.zhaw.psit4.domain.dialplan.applications;

import ch.zhaw.psit4.domain.beans.SipClient;
import ch.zhaw.psit4.domain.exceptions.ValidationException;
import ch.zhaw.psit4.testsupport.fixtures.domain.DialAppCallGenerator;
import ch.zhaw.psit4.testsupport.fixtures.domain.SipClientGenerator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Jona Braun
 */
public class DialAppTest {
    private List<SipClient> sipClientList;

    @Test(expected = ValidationException.class)
    public void nullTechnology() throws Exception {
        DialApp dialApp = new DialApp(null,
                SipClientGenerator.generateSipClientList(1, 1),
                "1");
        dialApp.validate();
    }

    @Test(expected = ValidationException.class)
    public void nullSipClientList() throws Exception {
        DialApp dialApp = new DialApp(DialApp.Technology.PSIP,
                null,
                "1");
        dialApp.validate();
    }

    @Test(expected = ValidationException.class)
    public void emptySipClientList() throws Exception {
        DialApp dialApp = new DialApp(DialApp.Technology.PSIP,
                new ArrayList<>(),
                "1");
        dialApp.validate();
    }

    @Test(expected = ValidationException.class)
    public void nullTimeout() throws Exception {
        DialApp dialApp = new DialApp(DialApp.Technology.PSIP,
                SipClientGenerator.generateSipClientList(1, 1),
                null);
        dialApp.validate();
    }

    @Test(expected = ValidationException.class)
    public void emptyTimeout() throws Exception {
        DialApp dialApp = new DialApp(DialApp.Technology.PSIP,
                SipClientGenerator.generateSipClientList(1, 1),
                "");
        dialApp.validate();
    }

    @Test
    public void validate() throws Exception {
        DialApp dialApp = new DialApp(DialApp.Technology.PSIP,
                SipClientGenerator.generateSipClientList(1, 1),
                "1");
        dialApp.validate();
    }

    @Test
    public void getApplicationCallOneSIPClient() throws Exception {

        sipClientList = SipClientGenerator.generateSipClientList(1, 1);

        DialApp dialApp = new DialApp(DialApp.Technology.SIP, sipClientList, DialAppCallGenerator.TIMEOUT);

        String dialAppCall = dialApp.toApplicationCall();
        String expected = DialAppCallGenerator.generateMultipleDialAppCalls(1, 1, "SIP");

        assertEquals(expected, dialAppCall);
    }

    @Test
    public void getApplicationCallMultipleSIPClients() throws Exception {

        sipClientList = SipClientGenerator.generateSipClientList(5, 1);

        DialApp dialApp = new DialApp(DialApp.Technology.SIP, sipClientList, DialAppCallGenerator.TIMEOUT);

        String dialAppCall = dialApp.toApplicationCall();
        String expected = DialAppCallGenerator.generateMultipleDialAppCalls(5, 1, "SIP");

        assertEquals(expected, dialAppCall);
    }

    @Test
    public void getApplicationCallMultiplePSIPClients() throws Exception {

        sipClientList = SipClientGenerator.generateSipClientList(2, 1);

        DialApp dialApp = new DialApp(DialApp.Technology.PSIP, sipClientList, DialAppCallGenerator.TIMEOUT);

        String dialAppCall = dialApp.toApplicationCall();
        String expected = DialAppCallGenerator.generateMultipleDialAppCalls(2, 1, "PSIP");

        assertEquals(expected, dialAppCall);
    }


}