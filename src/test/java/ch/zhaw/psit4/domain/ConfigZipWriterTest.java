package ch.zhaw.psit4.domain;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.junit.Assert.assertArrayEquals;

/**
 * Test for ConfigZipWriter.
 *
 * @author Jona Braun
 */
public class ConfigZipWriterTest {

    private static final String SIP_CONF_FILE_NAME = "sip.conf";
    private static final String DIAL_PLAN_CONF_FILE_NAME = "extension.conf";
    private static final int NUMBER_OF_FILES = 2;

    private static final String SIP_CLIENT_CONF = "[sip-client1]\ncontent=test";
    private static final String DIAL_PLAN_CLIENT_CONF = "[company1]\nextern => 555,1,Dial(SIP/sip-client1,20)\n";

    private ConfigZipWriter configZipWriter;

    @Before
    public void setup() {
        configZipWriter = new ConfigZipWriter(SIP_CLIENT_CONF, DIAL_PLAN_CLIENT_CONF);
    }

    @Test
    public void writeConfigurationZipFile() throws Exception {

        ByteArrayOutputStream configZipFileBaos = configZipWriter.writeConfigurationZipFile();

        ZipInputStream zipConfigInputStream = new ZipInputStream(new ByteArrayInputStream(configZipFileBaos.toByteArray()));

        String[] expectedFileName = {SIP_CONF_FILE_NAME, DIAL_PLAN_CONF_FILE_NAME};
        String[] expectedFileContent = {SIP_CLIENT_CONF, DIAL_PLAN_CLIENT_CONF};

        String[] fileName = new String[NUMBER_OF_FILES];
        String[] fileContent = new String[NUMBER_OF_FILES];

        readZipEntries(zipConfigInputStream, fileName, fileContent);

        assertArrayEquals(expectedFileName, fileName);
        assertArrayEquals(expectedFileContent, fileContent);

    }

    private void readZipEntries(ZipInputStream zipInputStream, String[] fileName, String[] fileContent) throws IOException {
        ZipEntry zipEntry;
        int iteration = 0;
        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
            if (iteration >= NUMBER_OF_FILES) {
                new AssertionError("the zip contains more then two files");
            }
            fileName[iteration] = zipEntry.getName();

            ByteArrayOutputStream fileContentBaos = new ByteArrayOutputStream();

            byte[] byteBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = zipInputStream.read(byteBuffer)) != -1) {
                fileContentBaos.write(byteBuffer, 0, bytesRead);
            }

            fileContent[iteration] = fileContentBaos.toString();

            fileContentBaos.close();

            zipInputStream.closeEntry();

            iteration++;
        }
        zipInputStream.close();
    }

}