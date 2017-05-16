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
import ch.zhaw.psit4.testsupport.convenience.ZipStreamReader;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipInputStream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;

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

        assertThat(zipStreamReader.getFileContent(ConfigZipWriter.DIAL_PLAN_CONFIG_FILE_NAME), equalTo
                (DIAL_PLAN_CLIENT_CONF));
        assertThat(zipStreamReader.getFileContent(ConfigZipWriter.SIP_CONFIG_FILE_NAME), equalTo
                (SIP_CLIENT_CONF));

    }

    @Test(expected = ZipFileCreationException.class)
    public void testExceptionHandling() throws Exception {
        ConfigZipWriter configZipWriterSpy = spy(configZipWriter);

        Mockito.doThrow(new IOException("Test io exception")).when(configZipWriterSpy).writeZipEntry(any(), any(),
                any());

        configZipWriterSpy.writeConfigurationZipFile();
    }

}