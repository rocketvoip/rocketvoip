package ch.zhaw.psit4.domain;

import ch.zhaw.psit4.domain.interfaces.SipClientConfigurationInterface;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.fail;

/**
 * Test for ConfigWriter.
 *
 * @author Jona Braun
 */
public class ConfigWriterTest {

    private ConfigWriter configWriter;
    @Mock
    private SipClientConfigurationInterface sipClientConfiguration;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        configWriter = new ConfigWriter();
    }

    @Test
    public void writeSIPConfiguration() throws Exception {
        fail();
    }
}