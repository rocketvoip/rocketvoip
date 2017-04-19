package ch.zhaw.psit4.domain;

import ch.zhaw.psit4.domain.beans.SipClient;
import ch.zhaw.psit4.domain.exceptions.InvalidConfigurationException;
import ch.zhaw.psit4.domain.exceptions.ValidationException;
import ch.zhaw.psit4.domain.interfaces.SipClientConfigurationInterface;
import ch.zhaw.psit4.testsupport.fixtures.domain.SipClientGenerator;
import ch.zhaw.psit4.testsupport.matchers.SipClientEqualTo;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Rafael Ostertag
 */
public class SipClientConfigBuilderTest {
    private SipClientConfigBuilder sipClientConfigBuilder;

    @Before
    public void setUp() throws Exception {
        sipClientConfigBuilder = new SipClientConfigBuilder();
    }

    @Test
    public void addOneSipClient() throws Exception {
        SipClient sipClient = SipClientGenerator.getSipClient(1, 1);
        List<SipClientConfigurationInterface> actual = sipClientConfigBuilder.addSipClient(sipClient).build();

        assertThat(actual, is(not(nullValue())));
        assertThat(actual, hasSize(1));
        assertThat(actual, hasItem(SipClientEqualTo.sipClientEqualTo(sipClient)));

    }

    @Test
    public void addThreeSipClient() throws Exception {
        SipClient sipClient1 = SipClientGenerator.getSipClient(1, 1);
        SipClient sipClient2 = SipClientGenerator.getSipClient(1, 2);
        SipClient sipClient3 = SipClientGenerator.getSipClient(1, 3);
        List<SipClientConfigurationInterface> actual = sipClientConfigBuilder
                .addSipClient(sipClient1)
                .addSipClient(sipClient2)
                .addSipClient(sipClient3)
                .build();

        assertThat(actual, is(not(nullValue())));
        assertThat(actual, hasSize(3));
        assertThat(actual, hasItem(SipClientEqualTo.sipClientEqualTo(sipClient1)));
        assertThat(actual, hasItem(SipClientEqualTo.sipClientEqualTo(sipClient2)));
        assertThat(actual, hasItem(SipClientEqualTo.sipClientEqualTo(sipClient3)));
    }


    @Test
    public void callToSipClientValidate() throws Exception {
        SipClient sipClient = mock(SipClient.class);
        sipClientConfigBuilder.addSipClient(sipClient);

        verify(sipClient, times(1)).validate();
    }

    @Test(expected = InvalidConfigurationException.class)
    public void addNullSipClient() throws Exception {
        sipClientConfigBuilder.addSipClient(null);
    }

    @Test(expected = ValidationException.class)
    public void addInvalidSipClient() throws Exception {
        SipClient sipClient = SipClientGenerator.getSipClient(1, 1);
        sipClient.setUsername(null);

        sipClientConfigBuilder.addSipClient(sipClient);
    }

    @Test(expected = InvalidConfigurationException.class)
    public void emptyBuilder() throws Exception {
        sipClientConfigBuilder.build();
    }

}