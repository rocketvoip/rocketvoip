package ch.zhaw.psit4.domain.sipclient;

import ch.zhaw.psit4.domain.beans.SipClient;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
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

    // This test is simply done to improve coverage
    @Test
    public void setId() throws Exception {
        assertThat(sipClient.getId(), equalTo(0L));

        sipClient.setId(1);
        assertThat(sipClient.getId(), equalTo(1L));
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

    @Test
    public void getLabel1() throws Exception {
        String user = "userxy";
        String company = "The Company AG";
        String expected = user + "-" + "The-Company-AG";
        sipClient.setUsername(user);
        sipClient.setCompany(company);
        assertEquals(expected, sipClient.getLabel());
    }

}