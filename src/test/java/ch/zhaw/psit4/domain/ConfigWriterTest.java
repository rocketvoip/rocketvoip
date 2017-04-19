package ch.zhaw.psit4.domain;

import ch.zhaw.psit4.domain.exceptions.InvalidConfigurationException;
import ch.zhaw.psit4.domain.interfaces.DialPlanContextConfigurationInterface;
import ch.zhaw.psit4.domain.interfaces.SipClientConfigurationInterface;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

/**
 * Test for ConfigWriter.
 *
 * @author Jona Braun
 */
public class ConfigWriterTest {
    @Test(expected = InvalidConfigurationException.class)
    public void testNullSipClientConfigurationList() throws Exception {
        ConfigWriter.generateSipClientConfiguration(null);
    }

    @Test(expected = InvalidConfigurationException.class)
    public void testEmptySipClientConfigurationList() throws Exception {
        ConfigWriter.generateSipClientConfiguration(new ArrayList<>());
    }

    @Test(expected = InvalidConfigurationException.class)
    public void testNullDialPlanConfigurationList() throws Exception {
        ConfigWriter.generateDialPlanConfiguration(null);
    }

    @Test(expected = InvalidConfigurationException.class)
    public void testEmptyDialPlanConfigurationList() throws Exception {
        ConfigWriter.generateDialPlanConfiguration(new ArrayList<>());
    }

    @Test
    public void testSimpleSipClientConfigurationList() throws Exception {
        SipClientConfigurationInterface sipClient = mock(SipClientConfigurationInterface.class);
        when(sipClient.toSipClientConfiguration()).thenReturn("sip client mock");

        List<SipClientConfigurationInterface> list = new ArrayList<>();
        list.add(sipClient);

        String actual = ConfigWriter.generateSipClientConfiguration(list);
        assertThat(actual, equalTo("sip client mock"));

        verify(sipClient, atLeastOnce()).validate();
    }

    @Test
    public void testMultipleSipClientsConfigurationList() throws Exception {
        SipClientConfigurationInterface sipClient1 = mock(SipClientConfigurationInterface.class);
        when(sipClient1.toSipClientConfiguration()).thenReturn("sip client mock 1\n");

        SipClientConfigurationInterface sipClient2 = mock(SipClientConfigurationInterface.class);
        when(sipClient2.toSipClientConfiguration()).thenReturn("sip client mock 2\n");

        List<SipClientConfigurationInterface> list = new ArrayList<>();
        list.add(sipClient1);
        list.add(sipClient2);

        String actual = ConfigWriter.generateSipClientConfiguration(list);
        String expected = "sip client mock 1\nsip client mock 2\n";
        assertThat(actual, equalTo(expected));

        verify(sipClient1, atLeastOnce()).validate();
        verify(sipClient2, atLeastOnce()).validate();
    }

    @Test
    public void testMultipleSipClientsConfigurationListWithNullInterspersed() throws Exception {
        SipClientConfigurationInterface sipClient1 = mock(SipClientConfigurationInterface.class);
        when(sipClient1.toSipClientConfiguration()).thenReturn("sip client mock 1\n");

        SipClientConfigurationInterface sipClient2 = mock(SipClientConfigurationInterface.class);
        when(sipClient2.toSipClientConfiguration()).thenReturn("sip client mock 2\n");

        List<SipClientConfigurationInterface> list = new ArrayList<>();
        list.add(null);
        list.add(sipClient1);
        list.add(null);
        list.add(sipClient2);
        list.add(null);

        String actual = ConfigWriter.generateSipClientConfiguration(list);
        String expected = "sip client mock 1\nsip client mock 2\n";
        assertThat(actual, equalTo(expected));

        verify(sipClient1, atLeastOnce()).validate();
        verify(sipClient2, atLeastOnce()).validate();
    }

    @Test
    public void testSimpleDialPlanConfigurationList() throws Exception {
        DialPlanContextConfigurationInterface dialPlan = mock(DialPlanContextConfigurationInterface.class);
        when(dialPlan.toDialPlanContextConfiguration()).thenReturn("dialplan context mock");

        List<DialPlanContextConfigurationInterface> list = new ArrayList<>();
        list.add(dialPlan);

        String actual = ConfigWriter.generateDialPlanConfiguration(list);
        assertThat(actual, equalTo("dialplan context mock"));

        verify(dialPlan, atLeastOnce()).validate();
    }

    @Test
    public void testMultipleDialPlanConfigurationList() throws Exception {
        DialPlanContextConfigurationInterface dialPlan1 = mock(DialPlanContextConfigurationInterface.class);
        when(dialPlan1.toDialPlanContextConfiguration()).thenReturn("dialplan context mock 1\n");

        DialPlanContextConfigurationInterface dialPlan2 = mock(DialPlanContextConfigurationInterface.class);
        when(dialPlan2.toDialPlanContextConfiguration()).thenReturn("dialplan context mock 2\n");

        List<DialPlanContextConfigurationInterface> list = new ArrayList<>();
        list.add(dialPlan1);
        list.add(dialPlan2);

        String actual = ConfigWriter.generateDialPlanConfiguration(list);
        String expected = "dialplan context mock 1\ndialplan context mock 2\n";
        assertThat(actual, equalTo(expected));

        verify(dialPlan1, atLeastOnce()).validate();
        verify(dialPlan2, atLeastOnce()).validate();
    }

    @Test
    public void testMultipleDialPlanConfigurationListWithNulInterspersed() throws Exception {
        DialPlanContextConfigurationInterface dialPlan1 = mock(DialPlanContextConfigurationInterface.class);
        when(dialPlan1.toDialPlanContextConfiguration()).thenReturn("dialplan context mock 1\n");

        DialPlanContextConfigurationInterface dialPlan2 = mock(DialPlanContextConfigurationInterface.class);
        when(dialPlan2.toDialPlanContextConfiguration()).thenReturn("dialplan context mock 2\n");

        List<DialPlanContextConfigurationInterface> list = new ArrayList<>();
        list.add(null);
        list.add(dialPlan1);
        list.add(null);
        list.add(dialPlan2);
        list.add(null);

        String actual = ConfigWriter.generateDialPlanConfiguration(list);
        String expected = "dialplan context mock 1\ndialplan context mock 2\n";
        assertThat(actual, equalTo(expected));

        verify(dialPlan1, atLeastOnce()).validate();
        verify(dialPlan2, atLeastOnce()).validate();
    }
}