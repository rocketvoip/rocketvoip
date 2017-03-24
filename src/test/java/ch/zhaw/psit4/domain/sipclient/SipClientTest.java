package ch.zhaw.psit4.domain.sipclient;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Jona Braun
 */
public class SipClientTest {
    private SipClient sipClient;

    @Before
    public void setUp() throws Exception {
        sipClient = new SipClient();
    }

    @Test
    public void getLabel() throws Exception {
        String user = "userxy";
        String company = "acme";
        String expected = user + "-" + company;
        sipClient.setUsername(user);
        sipClient.setCompany(company);
        assertEquals(expected, sipClient.getLabel());
    }

}