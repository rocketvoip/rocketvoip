package ch.zhaw.psit4.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Zips the asterisk configuration.
 *
 * @author Jona Braun
 */
public class ConfigZipWriter {
    private static final String SIP_CONFIG_FILE_NAME = "sip.conf";
    private static final String DIAL_PLAN_CONFIG_FILE_NAME = "extension.conf";
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigZipWriter.class);
    private String sipClientConf;
    private String dialPlanConf;

    public ConfigZipWriter(String sipClientConf, String dialPlanConf) {
        this.sipClientConf = sipClientConf;
        this.dialPlanConf = dialPlanConf;

    }

    /**
     * Creates a OutputStream containing the zipped configuration.
     *
     * @return the ByteArrayOutputStream containing the zipped configuration files
     */
    public ByteArrayOutputStream writeConfigurationZipFile() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);

        ZipEntry sipClientConfEntry = new ZipEntry(SIP_CONFIG_FILE_NAME);
        ZipEntry dialPlanConfEntry = new ZipEntry(DIAL_PLAN_CONFIG_FILE_NAME);

        try {
            writeZipEntry(zos, sipClientConfEntry, sipClientConf.getBytes());
            writeZipEntry(zos, dialPlanConfEntry, dialPlanConf.getBytes());
            zos.close();
        } catch (IOException e) {
            LOGGER.error("io error in zip file creation", e);
            // throw new Exception
        }
        return baos;
    }

    private void writeZipEntry(ZipOutputStream zos, ZipEntry sipClientConfEntry, byte[] bytes) throws IOException {
        zos.putNextEntry(sipClientConfEntry);
        zos.write(bytes);
        zos.closeEntry();
    }

}
