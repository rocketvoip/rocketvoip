package ch.zhaw.psit4.domain.sipclient;

import ch.zhaw.psit4.domain.exceptions.InvalidConfigurationException;
import ch.zhaw.psit4.domain.helper.SipClientTestHelper;
import ch.zhaw.psit4.fixtures.domain.SipClientDomainGenerator;
import ch.zhaw.psit4.fixtures.general.CompanyData;
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

    private final SipClientTestHelper sipClientTestHelper = new SipClientTestHelper();
    private SipClientConfigurationChanSip sipClientConfigurationChanSip;
    private List<SipClient> sipClientList;

    @Before
    public void setUp() throws Exception {
        sipClientConfigurationChanSip = new SipClientConfigurationChanSip();
    }

    @Test(expected = InvalidConfigurationException.class)
    public void testNullConfiguration() throws Exception {
        createConfigString(null);
    }

    @Test(expected = InvalidConfigurationException.class)
    public void testEmptyConfiguration() throws Exception {
        createConfigString(Collections.emptyList());
    }

    @Test
    public void testNullClient() throws Exception {
        List<SipClient> sipClientList = new ArrayList<>();
        sipClientList.add(null);

        String actual = createConfigString(sipClientList);
        String expected = "";

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNullClientInBetween() throws Exception {
        List<SipClient> sipClientList = new ArrayList<>();
        sipClientList.add(SipClientDomainGenerator.getSipClientDomain(CompanyData.COMPANY_PREFIX, 1));
        sipClientList.add(null);
        sipClientList.add(SipClientDomainGenerator.getSipClientDomain(CompanyData.COMPANY_PREFIX, 2));

        String actual = createConfigString(sipClientList);
        String expected = sipClientTestHelper.generateSipClientConfig(2, CompanyData.COMPANY_PREFIX);

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testClientWithNullValues() throws Exception {
        sipClientList = new ArrayList<>();

        sipClientList.add(new SipClient());

        String actual = createConfigString(sipClientList);
        String expected = "";

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testClientWithNullUsername() throws Exception {
        generateOneSipClientInList();
        sipClientList.get(0).setUsername(null);

        String actual = createConfigString(sipClientList);
        String expected = "";

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testClientWithNullSecret() throws Exception {
        generateOneSipClientInList();
        sipClientList.get(0).setSecret(null);

        String actual = createConfigString(sipClientList);
        String expected = "";

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testClientWithNullPhoneNumber() throws Exception {
        generateOneSipClientInList();
        sipClientList.get(0).setPhoneNumber(null);

        String actual = createConfigString(sipClientList);
        String expected = "";

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testSingleClient() throws Exception {
        generateOneSipClientInList();

        String actual = createConfigString(sipClientList);
        String expected = sipClientTestHelper.generateSipClientConfig(1, CompanyData.COMPANY_PREFIX);

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testTwoClients() throws Exception {
        sipClientList = sipClientTestHelper.generateSipClientList(2, CompanyData.COMPANY_PREFIX);

        String actual = createConfigString(sipClientList);
        String expected = sipClientTestHelper.generateSipClientConfig(2, CompanyData.COMPANY_PREFIX);

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testManyClients() throws Exception {
        sipClientList = sipClientTestHelper.generateSipClientList(100, CompanyData.COMPANY_PREFIX);

        String actual = createConfigString(sipClientList);
        String expected = sipClientTestHelper.generateSipClientConfig(100, CompanyData.COMPANY_PREFIX);
        assertThat(actual, equalTo(expected));
    }

    private void generateOneSipClientInList() {
        sipClientList = sipClientTestHelper.generateSipClientList(1, CompanyData.COMPANY_PREFIX);
    }

    private String createConfigString(List<SipClient> sipClientList) {
        return sipClientConfigurationChanSip.generateSipClientConfiguration(sipClientList);
    }

}