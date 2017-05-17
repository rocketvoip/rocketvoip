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

package ch.zhaw.psit4.services.implementation;

import ch.zhaw.psit4.domain.ConfigZipWriter;
import ch.zhaw.psit4.domain.exceptions.InvalidConfigurationException;
import ch.zhaw.psit4.services.interfaces.ConfigServiceInterface;
import ch.zhaw.psit4.testsupport.convenience.InputStreamStringyfier;
import ch.zhaw.psit4.testsupport.convenience.ZipStreamReader;
import ch.zhaw.psit4.testsupport.fixtures.database.BeanConfiguration;
import ch.zhaw.psit4.testsupport.fixtures.database.DatabaseFixtureBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.zip.ZipInputStream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Jona Braun
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Import(BeanConfiguration.class)
public class ConfigServiceImplIT {
    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    private ConfigServiceInterface configServiceInterface;
    private DatabaseFixtureBuilder databaseFixtureBuilder;

    @Before
    public void setUp() throws Exception {
        databaseFixtureBuilder = applicationContext.getBean(DatabaseFixtureBuilder.class);
    }

    @Test(expected = InvalidConfigurationException.class)
    public void getAsteriskConfigurationWithNoSipClients() throws Exception {
        configServiceInterface.getAsteriskConfiguration();
    }

    @Test
    public void getAsteriskConfigurationWithOneSipClient() throws Exception {
        databaseFixtureBuilder.setCompany(1).addSipClient(1).build();

        ByteArrayOutputStream baos = configServiceInterface.getAsteriskConfiguration();
        ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(baos.toByteArray()));

        ZipStreamReader zipStreamReader = new ZipStreamReader(zipInputStream);

        assertThat(zipStreamReader.hasFile(ConfigZipWriter.SIP_CONFIG_FILE_NAME), equalTo(true));
        assertThat(zipStreamReader.hasFile(ConfigZipWriter.DIAL_PLAN_CONFIG_FILE_NAME), equalTo(true));

        String expected = InputStreamStringyfier.slurpStream(
                ConfigServiceImplIT.class.getResourceAsStream("/fixtures/oneCompanyOneClient" +
                        ".txt")
        );
        assertThat(zipStreamReader.getFileContent(ConfigZipWriter.SIP_CONFIG_FILE_NAME), equalTo(expected));

        expected = InputStreamStringyfier.slurpStream(
                ConfigServiceImplIT.class.getResourceAsStream("/fixtures/oneContextOneApp.txt")
        );
        assertThat(zipStreamReader.getFileContent(ConfigZipWriter.DIAL_PLAN_CONFIG_FILE_NAME), equalTo(expected));

    }

    @Test
    public void getAsteriskConfigurationWithMultipleSipClients() throws Exception {
        databaseFixtureBuilder.setCompany(1).addSipClient(1).addSipClient(2).build();

        ByteArrayOutputStream baos = configServiceInterface.getAsteriskConfiguration();
        ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(baos.toByteArray()));

        ZipStreamReader zipStreamReader = new ZipStreamReader(zipInputStream);

        assertThat(zipStreamReader.hasFile(ConfigZipWriter.SIP_CONFIG_FILE_NAME), equalTo(true));
        assertThat(zipStreamReader.hasFile(ConfigZipWriter.DIAL_PLAN_CONFIG_FILE_NAME), equalTo(true));

        String expected = InputStreamStringyfier.slurpStream(
                ConfigServiceImplIT.class.getResourceAsStream("/fixtures/oneCompanyTwoClients.txt")
        );
        assertThat(zipStreamReader.getFileContent(ConfigZipWriter.SIP_CONFIG_FILE_NAME), equalTo(expected));

        expected = InputStreamStringyfier.slurpStream(
                ConfigServiceImplIT.class.getResourceAsStream("/fixtures/oneContextTwoApps.txt")
        );
    }

    @Test
    public void getSimpleAsteriskConfigWithSayAlpha() throws Exception {
        databaseFixtureBuilder
                .setCompany(1)
                .addSipClient(1)
                .addSipClient(2)
                .addSipClient(3)
                .addDialPlan(1)
                .addSayAlpha(1, 1, 1)
                .build();

        ByteArrayOutputStream baos = configServiceInterface.getAsteriskConfiguration();
        ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(baos.toByteArray()));

        ZipStreamReader zipStreamReader = new ZipStreamReader(zipInputStream);

        assertThat(zipStreamReader.hasFile(ConfigZipWriter.SIP_CONFIG_FILE_NAME), equalTo(true));
        assertThat(zipStreamReader.hasFile(ConfigZipWriter.DIAL_PLAN_CONFIG_FILE_NAME), equalTo(true));

        String expected = InputStreamStringyfier.slurpStream(
                ConfigServiceImplIT.class.getResourceAsStream("/fixtures/oneCompanyThreeClients.txt")
        );
        assertThat(zipStreamReader.getFileContent(ConfigZipWriter.SIP_CONFIG_FILE_NAME), equalTo(expected));

        expected = InputStreamStringyfier.slurpStream(
                ConfigServiceImplIT.class.getResourceAsStream("/fixtures/simpleAsteriskConfigWithSayAlpha.txt")
        );

        assertThat(zipStreamReader.getFileContent("extensions.conf"), equalTo(expected));
    }

    @Test
    public void getSimpleAsteriskConfigWithDial() throws Exception {
        databaseFixtureBuilder
                .setCompany(1)
                .addSipClient(1)
                .addSipClient(2)
                .addSipClient(3)
                .addDialPlan(1)
                .addDial(1, 2, 1, new int[]{2, 3})
                .build();

        ByteArrayOutputStream baos = configServiceInterface.getAsteriskConfiguration();
        ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(baos.toByteArray()));

        ZipStreamReader zipStreamReader = new ZipStreamReader(zipInputStream);

        assertThat(zipStreamReader.hasFile(ConfigZipWriter.SIP_CONFIG_FILE_NAME), equalTo(true));
        assertThat(zipStreamReader.hasFile(ConfigZipWriter.DIAL_PLAN_CONFIG_FILE_NAME), equalTo(true));

        String expected = InputStreamStringyfier.slurpStream(
                ConfigServiceImplIT.class.getResourceAsStream("/fixtures/oneCompanyThreeClients.txt")
        );
        assertThat(zipStreamReader.getFileContent(ConfigZipWriter.SIP_CONFIG_FILE_NAME), equalTo(expected));

        expected = InputStreamStringyfier.slurpStream(
                ConfigServiceImplIT.class.getResourceAsStream("/fixtures/simpleAsteriskConfigWithDial.txt")
        );

        assertThat(zipStreamReader.getFileContent("extensions.conf"), equalTo(expected));
    }

    @Test
    public void getSimpleAsteriskConfigWithGoto() throws Exception {
        databaseFixtureBuilder
                .setCompany(1)
                .addSipClient(1)
                .addSipClient(2)
                .addSipClient(3)
                .addDialPlan(1)
                .addDialPlanNoPhoneNr(2)
                .addGoto(1, 1, 1, 2)
                .addDial(1, 1, 2, new int[]{1, 2, 3})
                .build();

        ByteArrayOutputStream baos = configServiceInterface.getAsteriskConfiguration();
        ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(baos.toByteArray()));

        ZipStreamReader zipStreamReader = new ZipStreamReader(zipInputStream);

        assertThat(zipStreamReader.hasFile(ConfigZipWriter.SIP_CONFIG_FILE_NAME), equalTo(true));
        assertThat(zipStreamReader.hasFile(ConfigZipWriter.DIAL_PLAN_CONFIG_FILE_NAME), equalTo(true));

        String expected = InputStreamStringyfier.slurpStream(
                ConfigServiceImplIT.class.getResourceAsStream("/fixtures/oneCompanyThreeClients.txt")
        );
        assertThat(zipStreamReader.getFileContent(ConfigZipWriter.SIP_CONFIG_FILE_NAME), equalTo(expected));

        expected = InputStreamStringyfier.slurpStream(
                ConfigServiceImplIT.class.getResourceAsStream("/fixtures/simpleAsteriskConfigWithGoto.txt")
        );

        assertThat(zipStreamReader.getFileContent("extensions.conf"), equalTo(expected));
    }

    @Test
    public void getSimpleAsteriskConfigWithSayAlphaAndDial() throws Exception {
        databaseFixtureBuilder
                .setCompany(1)
                .addSipClient(1)
                .addSipClient(2)
                .addSipClient(3)
                .addDialPlan(1)
                .addSayAlpha(1, 1, 1)
                .addDial(1, 2, 1, new int[]{2, 3})
                .build();

        ByteArrayOutputStream baos = configServiceInterface.getAsteriskConfiguration();
        ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(baos.toByteArray()));

        ZipStreamReader zipStreamReader = new ZipStreamReader(zipInputStream);

        assertThat(zipStreamReader.hasFile(ConfigZipWriter.SIP_CONFIG_FILE_NAME), equalTo(true));
        assertThat(zipStreamReader.hasFile(ConfigZipWriter.DIAL_PLAN_CONFIG_FILE_NAME), equalTo(true));

        String expected = InputStreamStringyfier.slurpStream(
                ConfigServiceImplIT.class.getResourceAsStream("/fixtures/oneCompanyThreeClients.txt")
        );
        assertThat(zipStreamReader.getFileContent(ConfigZipWriter.SIP_CONFIG_FILE_NAME), equalTo(expected));

        expected = InputStreamStringyfier.slurpStream(
                ConfigServiceImplIT.class.getResourceAsStream("/fixtures/simpleAsteriskConfigWithSayAlphaAndDial.txt")
        );

        assertThat(zipStreamReader.getFileContent("extensions.conf"), equalTo(expected));
    }

    @Test
    public void getSimpleAsteriskConfigurationWithBranch() throws Exception {
        databaseFixtureBuilder
                .setCompany(1)
                .addSipClient(1)
                .addSipClient(2)
                .addSipClient(3)
                .addDialPlan(1)
                .addDialPlanNoPhoneNr(2)
                .addDialPlanNoPhoneNr(3)
                .addBranchDialPlan(1, 2)
                .addBranchDialPlan(2, 3)
                .addBranch(1, 2, 1, Arrays.asList(1, 2))
                .addDial(1, 1, 2, new int[]{1})
                .addDial(2, 1, 3, new int[]{2})
                .build();

        ByteArrayOutputStream baos = configServiceInterface.getAsteriskConfiguration();
        ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(baos.toByteArray()));

        ZipStreamReader zipStreamReader = new ZipStreamReader(zipInputStream);

        assertThat(zipStreamReader.hasFile(ConfigZipWriter.SIP_CONFIG_FILE_NAME), equalTo(true));
        assertThat(zipStreamReader.hasFile(ConfigZipWriter.DIAL_PLAN_CONFIG_FILE_NAME), equalTo(true));

        String expected = InputStreamStringyfier.slurpStream(
                ConfigServiceImplIT.class.getResourceAsStream("/fixtures/oneCompanyThreeClients.txt")
        );
        assertThat(zipStreamReader.getFileContent(ConfigZipWriter.SIP_CONFIG_FILE_NAME), equalTo(expected));

        expected = InputStreamStringyfier.slurpStream(
                ConfigServiceImplIT.class.getResourceAsStream("/fixtures/simpleAsteriskConfigWithBranch.txt")
        );

        assertThat(zipStreamReader.getFileContent("extensions.conf"), equalTo(expected));
    }

}