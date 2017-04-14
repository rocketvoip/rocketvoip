package ch.zhaw.psit4.domain;

import ch.zhaw.psit4.domain.exceptions.ZipFileCreationException;
import ch.zhaw.psit4.helper.ZipStreamTestHelper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipInputStream;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;

/**
 * Test for ConfigZipWriter.
 *
 * @author Jona Braun
 */
public class ConfigZipWriterTest {

    private static final String SIP_CONF_FILE_NAME = "sip.conf";
    private static final String DIAL_PLAN_CONF_FILE_NAME = "extensions.conf";
    private static final int NUMBER_OF_FILES = 2;

    private static final String SIP_CLIENT_CONF = "[sip-client1]\ncontent=test";
    private static final String DIAL_PLAN_CLIENT_CONF = "[company1]\nexten => 555,1,Dial(SIP/sip-client1,20)\n";
    private final ZipStreamTestHelper zipStreamTestHelper = new ZipStreamTestHelper();
    private ConfigZipWriter configZipWriter;

    @Before
    public void setup() {
        configZipWriter = new ConfigZipWriter(SIP_CLIENT_CONF, DIAL_PLAN_CLIENT_CONF);
    }

    @Test
    public void writeConfigurationZipFile() throws Exception {

        ByteArrayOutputStream configZipFileBaos = configZipWriter.writeConfigurationZipFile();

        ZipInputStream zipConfigInputStream = new ZipInputStream(new ByteArrayInputStream(configZipFileBaos.toByteArray()));

        String[] expectedFileNames = {SIP_CONF_FILE_NAME, DIAL_PLAN_CONF_FILE_NAME};
        String[] expectedFileContent = {SIP_CLIENT_CONF, DIAL_PLAN_CLIENT_CONF};

        zipStreamTestHelper.testZipEntryContent(zipConfigInputStream, expectedFileNames, expectedFileContent);

    }

    @Test(expected = ZipFileCreationException.class)
    public void testExceptionHandling() throws Exception {
        ConfigZipWriter configZipWriterSpy = spy(configZipWriter);

        Mockito.doThrow(new IOException("Test io exception")).when(configZipWriterSpy).writeZipEntry(any(), any(),
                any());

        configZipWriterSpy.writeConfigurationZipFile();
    }

}