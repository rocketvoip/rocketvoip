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
 * Test for @{@link ConfigWriter}.
 *
 * @author braunjon
 */
public class ConfigWriterTest {

    private ConfigWriter configWriter;

    @Before
    public void setUp() throws Exception {
        configWriter = new ConfigWriter();
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
                "secret=Secret1\n" +
                "[User3]\n" +
                "type=friend\n" +
                "context=acme\n" +
                "host=dynamic\n" +
                "secret=Secret3\n";

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
    public void testSingleClient() throws Exception {
        List<SipClient> sipClientList = generateSipClientList(1, "acme");

        String actual = createConfigString(sipClientList);
        String expected = "[User1]\n" +
                "type=friend\n" +
                "context=acme\n" +
                "host=dynamic\n" +
                "secret=Secret1\n";

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testTwoClients() throws Exception {
        List<SipClient> sipClientList = generateSipClientList(2, "acme");

        String actual = createConfigString(sipClientList);
        String expected = "[User1]\n" +
                "type=friend\n" +
                "context=acme\n" +
                "host=dynamic\n" +
                "secret=Secret1\n" +
                "[User2]\n" +
                "type=friend\n" +
                "context=acme\n" +
                "host=dynamic\n" +
                "secret=Secret2\n";

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testFiveClients() throws Exception {
        List<SipClient> sipClientList = generateSipClientList(5, "acme");

        String actual = createConfigString(sipClientList);
        String expected = "[User1]\n" +
                "type=friend\n" +
                "context=acme\n" +
                "host=dynamic\n" +
                "secret=Secret1\n" +
                "[User2]\n" +
                "type=friend\n" +
                "context=acme\n" +
                "host=dynamic\n" +
                "secret=Secret2\n" +
                "[User3]\n" +
                "type=friend\n" +
                "context=acme\n" +
                "host=dynamic\n" +
                "secret=Secret3\n" +
                "[User4]\n" +
                "type=friend\n" +
                "context=acme\n" +
                "host=dynamic\n" +
                "secret=Secret4\n" +
                "[User5]\n" +
                "type=friend\n" +
                "context=acme\n" +
                "host=dynamic\n" +
                "secret=Secret5\n";

        assertThat(actual, equalTo(expected));
    }

    private String createConfigString(List<SipClient> sipClientList) {
        return configWriter.writeSipClientConfiguration(sipClientList);
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

}