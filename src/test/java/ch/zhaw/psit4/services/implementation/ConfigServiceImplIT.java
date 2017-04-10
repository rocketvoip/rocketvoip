package ch.zhaw.psit4.services.implementation;

import ch.zhaw.psit4.domain.exceptions.InvalidConfigurationException;
import ch.zhaw.psit4.domain.helper.DialPlanTestHelper;
import ch.zhaw.psit4.domain.helper.SipClientTestHelper;
import ch.zhaw.psit4.fixtures.database.BeanConfiguration;
import ch.zhaw.psit4.fixtures.database.DatabaseFixtureBuilder;
import ch.zhaw.psit4.helper.ZipStreamTestHelper;
import ch.zhaw.psit4.services.interfaces.ConfigServiceInterface;
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

/**
 * @author Jona Braun
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Import(BeanConfiguration.class)
public class ConfigServiceImplIT {
    private final ZipStreamTestHelper zipStreamTestHelper = new ZipStreamTestHelper();
    private final SipClientTestHelper sipClientTestHelper = new SipClientTestHelper();
    @Autowired
    ApplicationContext applicationContext;
    private DialPlanTestHelper dialPlanTestHelper = new DialPlanTestHelper();
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

        String[] expectedNames = {"sip.conf", "extensions.conf"};
        String[] expectedContent = {sipClientTestHelper.generateSipClientConfig(1, 1),
                dialPlanTestHelper.getSimpleDialPlan(1, 1)};

        zipStreamTestHelper.testZipEntryContent(zipInputStream, expectedNames, expectedContent);

    }

    @Test
    public void getAsteriskConfigurationWithMultipleSipClients() throws Exception {
        databaseFixtureBuilder.company(1).addSipClient(1).addSipClient(2).build();

        ByteArrayOutputStream baos = configServiceInterface.getAsteriskConfiguration();
        ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(baos.toByteArray()));

        String[] expectedNames = {"sip.conf", "extensions.conf"};
        String[] expectedContent = {sipClientTestHelper.generateSipClientConfig(1, 2),
                dialPlanTestHelper.getSimpleDialPlan(1, 2)};

        zipStreamTestHelper.testZipEntryContent(zipInputStream, expectedNames, expectedContent);

    }

}