package ch.zhaw.psit4.services.implementation;

import ch.zhaw.psit4.domain.ConfigZipWriter;
import ch.zhaw.psit4.domain.exceptions.InvalidConfigurationException;
import ch.zhaw.psit4.services.interfaces.ConfigServiceInterface;
import ch.zhaw.psit4.testsupport.fixtures.database.BeanConfiguration;
import ch.zhaw.psit4.testsupport.fixtures.database.DatabaseFixtureBuilder;
import ch.zhaw.psit4.testsupport.helper.InputStreamStringyfier;
import ch.zhaw.psit4.testsupport.helper.ZipStreamReader;
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
        databaseFixtureBuilder.company(1).addSipClient(1).build();

        ByteArrayOutputStream baos = configServiceInterface.getAsteriskConfiguration();
        ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(baos.toByteArray()));

        ZipStreamReader zipStreamReader = new ZipStreamReader(zipInputStream);

        assertThat(zipStreamReader.hasFile(ConfigZipWriter.SIP_CONFIG_FILE_NAME), equalTo(true));
        assertThat(zipStreamReader.hasFile(ConfigZipWriter.DIAL_PLAN_CONFIG_FILE_NAME), equalTo(true));

        String expected = InputStreamStringyfier.slurpStream(
                ConfigServiceImplIT.class.getResourceAsStream("/fixtures/oneCompanyOneClient" +
                        ".txt")
        );
        assertThat(zipStreamReader.getContent(ConfigZipWriter.SIP_CONFIG_FILE_NAME), equalTo(expected));

        expected = InputStreamStringyfier.slurpStream(
                ConfigServiceImplIT.class.getResourceAsStream("/fixtures/oneContextOneApp.txt")
        );
        assertThat(zipStreamReader.getContent(ConfigZipWriter.DIAL_PLAN_CONFIG_FILE_NAME), equalTo(expected));

    }

    @Test
    public void getAsteriskConfigurationWithMultipleSipClients() throws Exception {
        databaseFixtureBuilder.company(1).addSipClient(1).addSipClient(2).build();

        ByteArrayOutputStream baos = configServiceInterface.getAsteriskConfiguration();
        ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(baos.toByteArray()));

        ZipStreamReader zipStreamReader = new ZipStreamReader(zipInputStream);

        assertThat(zipStreamReader.hasFile(ConfigZipWriter.SIP_CONFIG_FILE_NAME), equalTo(true));
        assertThat(zipStreamReader.hasFile(ConfigZipWriter.DIAL_PLAN_CONFIG_FILE_NAME), equalTo(true));

        String expected = InputStreamStringyfier.slurpStream(
                ConfigServiceImplIT.class.getResourceAsStream("/fixtures/oneCompanyTwoClients.txt")
        );
        assertThat(zipStreamReader.getContent(ConfigZipWriter.SIP_CONFIG_FILE_NAME), equalTo(expected));

        expected = InputStreamStringyfier.slurpStream(
                ConfigServiceImplIT.class.getResourceAsStream("/fixtures/oneContextTwoApps.txt")
        );
    }

}