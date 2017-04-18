package ch.zhaw.psit4.domain.sipclient;

import ch.zhaw.psit4.domain.beans.SipClient;
import ch.zhaw.psit4.domain.exceptions.InvalidConfigurationException;
import ch.zhaw.psit4.domain.exceptions.ValidationException;
import ch.zhaw.psit4.testsupport.convenience.InputStreamStringyfier;
import ch.zhaw.psit4.testsupport.fixtures.domain.SipClientGenerator;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Test for SipClientConfigurationChanSip.
 *
 * @author Jona Braun
 */
public class SipClientConfigurationChanSipTest {
    private SipClientConfigurationChanSip sipClientConfigurationChanSip;
    private List<SipClient> sipClientList;

    @Before
    public void setUp() throws Exception {
        sipClientConfigurationChanSip = new SipClientConfigurationChanSip();
        sipClientList = new ArrayList<>();
    }

    @Test(expected = InvalidConfigurationException.class)
    public void testNullConfiguration() throws Exception {
        sipClientConfigurationChanSip.toSipClientConfiguration(null);
    }

    @Test(expected = InvalidConfigurationException.class)
    public void testEmptyConfiguration() throws Exception {
        sipClientConfigurationChanSip.toSipClientConfiguration(Collections.emptyList());
    }

    @Test
    public void testNullClient() throws Exception {
        sipClientList.add(null);

        String actual = sipClientConfigurationChanSip.toSipClientConfiguration(sipClientList);
        String expected = "";

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNullClientInBetween() throws Exception {
        sipClientList.add(SipClientGenerator.getSipClient(1, 1));
        sipClientList.add(null);
        sipClientList.add(SipClientGenerator.getSipClient(1, 2));

        String actual = sipClientConfigurationChanSip.toSipClientConfiguration(sipClientList);
        String expected = InputStreamStringyfier.slurpStream(
                SipClientConfigurationChanSipTest.class.getResourceAsStream("/fixtures/oneCompanyTwoClients.txt")
        );

        assertThat(actual, equalTo(expected));
    }

    @Test(expected = ValidationException.class)
    public void testClientWithNullValues() throws Exception {
        sipClientList.add(new SipClient());

        String actual = sipClientConfigurationChanSip.toSipClientConfiguration(sipClientList);
        String expected = "";

        assertThat(actual, equalTo(expected));
    }

    @Test(expected = ValidationException.class)
    public void testClientWithNullUsername() throws Exception {
        sipClientList = SipClientGenerator.generateSipClientList(1, 1);
        sipClientList.get(0).setUsername(null);

        String actual = sipClientConfigurationChanSip.toSipClientConfiguration(sipClientList);
        String expected = "";

        assertThat(actual, equalTo(expected));
    }

    @Test(expected = ValidationException.class)
    public void testClientWithNullSecret() throws Exception {
        sipClientList = SipClientGenerator.generateSipClientList(1, 1);
        sipClientList.get(0).setSecret(null);

        String actual = sipClientConfigurationChanSip.toSipClientConfiguration(sipClientList);
        String expected = "";

        assertThat(actual, equalTo(expected));
    }

    @Test(expected = ValidationException.class)
    public void testClientWithNullPhoneNumber() throws Exception {
        sipClientList = SipClientGenerator.generateSipClientList(1, 1);
        sipClientList.get(0).setPhoneNumber(null);

        String actual = sipClientConfigurationChanSip.toSipClientConfiguration(sipClientList);
        String expected = "";

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testSingleClient() throws Exception {
        sipClientList = SipClientGenerator.generateSipClientList(1, 1);

        String actual = sipClientConfigurationChanSip.toSipClientConfiguration(sipClientList);
        String expected = InputStreamStringyfier.slurpStream(
                SipClientConfigurationChanSipTest.class.getResourceAsStream("/fixtures/oneCompanyOneClient.txt")
        );

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testTwoClients() throws Exception {
        sipClientList = SipClientGenerator.generateSipClientList(2, 1);

        String actual = sipClientConfigurationChanSip.toSipClientConfiguration(sipClientList);
        String expected = InputStreamStringyfier.slurpStream(
                SipClientConfigurationChanSipTest.class.getResourceAsStream("/fixtures/oneCompanyTwoClients.txt")
        );

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testManyClients() throws Exception {
        sipClientList = SipClientGenerator.generateSipClientList(3, 1);

        String actual = sipClientConfigurationChanSip.toSipClientConfiguration(sipClientList);
        String expected = InputStreamStringyfier.slurpStream(
                SipClientConfigurationChanSipTest.class.getResourceAsStream("/fixtures/oneCompanyThreeClients.txt")
        );
        assertThat(actual, equalTo(expected));
    }

}