package ch.zhaw.psit4.domain;

import ch.zhaw.psit4.domain.exceptions.ZipFileCreationException;
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
     * @throws ZipFileCreationException if there is an io error when creating the zip file.
     */
    public ByteArrayOutputStream writeConfigurationZipFile() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (ZipOutputStream zos = new ZipOutputStream(baos)) {

            ZipEntry sipClientConfEntry = new ZipEntry(SIP_CONFIG_FILE_NAME);
            ZipEntry dialPlanConfEntry = new ZipEntry(DIAL_PLAN_CONFIG_FILE_NAME);

            writeZipEntry(zos, sipClientConfEntry, sipClientConf.getBytes());
            writeZipEntry(zos, dialPlanConfEntry, dialPlanConf.getBytes());

        } catch (IOException e) {
            String errorMessage = "io error in zip file creation";
            LOGGER.error(errorMessage, e);
            throw new ZipFileCreationException(errorMessage, e);
        }
        return baos;
    }

    private void writeZipEntry(ZipOutputStream zos, ZipEntry sipClientConfEntry, byte[] bytes) throws IOException {
        zos.putNextEntry(sipClientConfEntry);
        zos.write(bytes);
        zos.closeEntry();
    }

}