package ch.zhaw.psit4.domain;

import ch.zhaw.psit4.tests.helper.ZipStreamReader;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.ZipInputStream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Test for ConfigZipWriter.
 *
 * @author Jona Braun
 */
public class ConfigZipWriterTest {

    private static final String SIP_CLIENT_CONF = "[sip-client1]\ncontent=test";
    private static final String DIAL_PLAN_CLIENT_CONF = "[company1]\nexten => 555,1,Dial(SIP/sip-client1,20)\n";
    private ConfigZipWriter configZipWriter;

    @Before
    public void setup() {
        configZipWriter = new ConfigZipWriter(SIP_CLIENT_CONF, DIAL_PLAN_CLIENT_CONF);
    }

    @Test
    public void writeConfigurationZipFile() throws Exception {

        ByteArrayOutputStream configZipFileBaos = configZipWriter.writeConfigurationZipFile();

        ZipInputStream zipConfigInputStream = new ZipInputStream(new ByteArrayInputStream(configZipFileBaos.toByteArray()));

        ZipStreamReader zipStreamReader = new ZipStreamReader(zipConfigInputStream);

        assertThat(zipStreamReader.hasFile(ConfigZipWriter.DIAL_PLAN_CONFIG_FILE_NAME), equalTo(true));
        assertThat(zipStreamReader.hasFile(ConfigZipWriter.SIP_CONFIG_FILE_NAME), equalTo(true));

        assertThat(zipStreamReader.getContent(ConfigZipWriter.DIAL_PLAN_CONFIG_FILE_NAME), equalTo
                (DIAL_PLAN_CLIENT_CONF));
        assertThat(zipStreamReader.getContent(ConfigZipWriter.SIP_CONFIG_FILE_NAME), equalTo
                (SIP_CLIENT_CONF));

    }

}