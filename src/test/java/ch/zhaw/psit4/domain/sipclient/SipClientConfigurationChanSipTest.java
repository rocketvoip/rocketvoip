package ch.zhaw.psit4.domain.sipclient;

import ch.zhaw.psit4.domain.exceptions.InvalidConfigurationException;
import ch.zhaw.psit4.testsupport.fixtures.domain.SipClientDomainGenerator;
import ch.zhaw.psit4.testsupport.fixtures.domain.SipClientGenerator;
import ch.zhaw.psit4.testsupport.fixtures.general.CompanyData;
import ch.zhaw.psit4.testsupport.helper.InputStreamStringyfier;
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

    private final SipClientGenerator sipClientGenerator = new SipClientGenerator();
    private SipClientConfigurationChanSip sipClientConfigurationChanSip;
    private List<SipClient> sipClientList;

    @Before
    public void setUp() throws Exception {
        sipClientConfigurationChanSip = new SipClientConfigurationChanSip();
        sipClientList = new ArrayList<>();
    }

    @Test(expected = InvalidConfigurationException.class)
    public void testNullConfiguration() throws Exception {
        sipClientConfigurationChanSip.generateSipClientConfiguration(null);
    }

    @Test(expected = InvalidConfigurationException.class)
    public void testEmptyConfiguration() throws Exception {
        sipClientConfigurationChanSip.generateSipClientConfiguration(Collections.emptyList());
    }

    @Test
    public void testNullClient() throws Exception {
        sipClientList.add(null);

        String actual = sipClientConfigurationChanSip.generateSipClientConfiguration(sipClientList);
        String expected = "";

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNullClientInBetween() throws Exception {
        sipClientList.add(SipClientDomainGenerator.getSipClientDomain(CompanyData.getCompanyName(1), 1));
        sipClientList.add(null);
        sipClientList.add(SipClientDomainGenerator.getSipClientDomain(CompanyData.getCompanyName(1), 2));

        String actual = sipClientConfigurationChanSip.generateSipClientConfiguration(sipClientList);
        String expected = InputStreamStringyfier.slurpStream(
                SipClientConfigurationChanSipTest.class.getResourceAsStream("/fixtures/oneCompanyTwoClients.txt")
        );

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testClientWithNullValues() throws Exception {
        sipClientList.add(new SipClient());

        String actual = sipClientConfigurationChanSip.generateSipClientConfiguration(sipClientList);
        String expected = "";

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testClientWithNullUsername() throws Exception {
        sipClientList = sipClientGenerator.generateSipClientList(1, CompanyData.getCompanyName(1));
        sipClientList.get(0).setUsername(null);

        String actual = sipClientConfigurationChanSip.generateSipClientConfiguration(sipClientList);
        String expected = "";

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testClientWithNullSecret() throws Exception {
        sipClientList = sipClientGenerator.generateSipClientList(1, CompanyData.getCompanyName(1));
        sipClientList.get(0).setSecret(null);

        String actual = sipClientConfigurationChanSip.generateSipClientConfiguration(sipClientList);
        String expected = "";

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testClientWithNullPhoneNumber() throws Exception {
        sipClientList = sipClientGenerator.generateSipClientList(1, CompanyData.getCompanyName(1));
        sipClientList.get(0).setPhoneNumber(null);

        String actual = sipClientConfigurationChanSip.generateSipClientConfiguration(sipClientList);
        String expected = "";

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testSingleClient() throws Exception {
        sipClientList = sipClientGenerator.generateSipClientList(1, CompanyData.getCompanyName(1));

        String actual = sipClientConfigurationChanSip.generateSipClientConfiguration(sipClientList);
        String expected = InputStreamStringyfier.slurpStream(
                SipClientConfigurationChanSipTest.class.getResourceAsStream("/fixtures/oneCompanyOneClient.txt")
        );

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testTwoClients() throws Exception {
        sipClientList = sipClientGenerator.generateSipClientList(2, CompanyData.getCompanyName(1));

        String actual = sipClientConfigurationChanSip.generateSipClientConfiguration(sipClientList);
        String expected = InputStreamStringyfier.slurpStream(
                SipClientConfigurationChanSipTest.class.getResourceAsStream("/fixtures/oneCompanyTwoClients.txt")
        );

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testManyClients() throws Exception {
        sipClientList = sipClientGenerator.generateSipClientList(3, CompanyData.getCompanyName(1));

        String actual = sipClientConfigurationChanSip.generateSipClientConfiguration(sipClientList);
        String expected = InputStreamStringyfier.slurpStream(
                SipClientConfigurationChanSipTest.class.getResourceAsStream("/fixtures/oneCompanyThreeClients.txt")
        );
        assertThat(actual, equalTo(expected));
    }

}