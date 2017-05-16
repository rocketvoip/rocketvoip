/*
 * Copyright 2017 Jona Braun, Benedikt Herzog, Rafael Ostertag,
 *                Marcel Schöni, Marco Studerus, Martin Wittwer
 *
 * Redistribution and  use in  source and binary  forms, with  or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * 1. Redistributions  of  source code  must retain  the above  copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in  binary form must reproduce  the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation   and/or   other    materials   provided   with   the
 *    distribution.
 *
 * THIS SOFTWARE  IS PROVIDED BY  THE COPYRIGHT HOLDERS  AND CONTRIBUTORS
 * "AS  IS" AND  ANY EXPRESS  OR IMPLIED  WARRANTIES, INCLUDING,  BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES  OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE  ARE DISCLAIMED. IN NO EVENT  SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL,  EXEMPLARY,  OR  CONSEQUENTIAL DAMAGES  (INCLUDING,  BUT  NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE  GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS  INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF  LIABILITY, WHETHER IN  CONTRACT, STRICT LIABILITY,  OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN  ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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
    public static final String SIP_CONFIG_FILE_NAME = "sip.conf";
    public static final String DIAL_PLAN_CONFIG_FILE_NAME = "extensions.conf";
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

    // This method is package private in order to make it mockable
    void writeZipEntry(ZipOutputStream zos, ZipEntry sipClientConfEntry, byte[] bytes) throws IOException {
        zos.putNextEntry(sipClientConfEntry);
        zos.write(bytes);
        zos.closeEntry();
    }

}