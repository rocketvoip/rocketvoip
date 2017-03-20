package ch.zhaw.psit4.domain;

import ch.zhaw.psit4.domain.exceptions.InvalidConfigurationException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Test for SipClientConfigurationV11.
 *
 * @author Jona Braun
 */
public class SipClientConfigurationV11Test {

    private SipClientConfigurationV11 sipClientConfigurationV11;

    @Before
    public void setUp() throws Exception {
        sipClientConfigurationV11 = new SipClientConfigurationV11();
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
        List<SipClient> sipClientList = new ArrayList<SipClient>();
        sipClientList.add(null);

        String actual = createConfigString(sipClientList);
        String expected = "";

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNullClientInBetween() throws Exception {
        List<SipClient> sipClientList = new ArrayList<SipClient>();
        sipClientList.add(generateSipClient(1, "acme"));
        sipClientList.add(null);
        sipClientList.add(generateSipClient(3, "acme"));

        String actual = createConfigString(sipClientList);
        String expected = "[User1]\n" +
                "type=friend\n" +
                "context=acme\n" +
                "host=dynamic\n" +
                "secret=Secret1\n\n" +
                "[User3]\n" +
                "type=friend\n" +
                "context=acme\n" +
                "host=dynamic\n" +
                "secret=Secret3\n\n";

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testClientWithNullValues() throws Exception {
        List<SipClient> sipClientList = new ArrayList<SipClient>();

        sipClientList.add(new SipClient());

        String actual = createConfigString(sipClientList);
        String expected = "";

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testClientWithNullUsername() throws Exception {
        List<SipClient> sipClientList = generateSipClientList(1, "acme");
        sipClientList.get(0).setUsername(null);

        String actual = createConfigString(sipClientList);
        String expected = "";

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testClientWithNullCompany() throws Exception {
        List<SipClient> sipClientList = generateSipClientList(1, "acme");
        sipClientList.get(0).setCompany(null);

        String actual = createConfigString(sipClientList);
        String expected = "";

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testClientWithNullSecret() throws Exception {
        List<SipClient> sipClientList = generateSipClientList(1, "acme");
        sipClientList.get(0).setSecret(null);

        String actual = createConfigString(sipClientList);
        String expected = "";

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testSingleClient() throws Exception {
        List<SipClient> sipClientList = generateSipClientList(1, "acme");

        String actual = createConfigString(sipClientList);
        String expected = generateSipClientConfig(1, "acme");

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testTwoClients() throws Exception {
        List<SipClient> sipClientList = generateSipClientList(2, "acme");

        String actual = createConfigString(sipClientList);
        String expected = generateSipClientConfig(2, "acme");

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testManyClients() throws Exception {
        List<SipClient> sipClientList = generateSipClientList(100, "acme");

        String actual = createConfigString(sipClientList);
        String expected = generateSipClientConfig(100, "acme");
        assertThat(actual, equalTo(expected));
    }

    private String createConfigString(List<SipClient> sipClientList) {
        return sipClientConfigurationV11.generateSipClientConfiguration(sipClientList);
    }

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
        return sipClient;
    }

    private String generateSipClientConfig(int number, String company) {
        String config = "";
        for (int i = 1; i <= number; i++) {
            config += "[User" + i + "]\n" +
                    "type=friend\n" +
                    "context=" + company + "\n" +
                    "host=dynamic\n" +
                    "secret=Secret" + i + "\n\n";
        }
        return config;
    }

}