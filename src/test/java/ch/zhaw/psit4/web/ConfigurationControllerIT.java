package ch.zhaw.psit4.web;

import ch.zhaw.psit4.domain.helper.DialPlanTestHelper;
import ch.zhaw.psit4.domain.helper.SipClientTestHelper;
import ch.zhaw.psit4.fixtures.database.BeanConfiguration;
import ch.zhaw.psit4.fixtures.database.DatabaseFixtureBuilder;
import ch.zhaw.psit4.fixtures.general.CompanyData;
import ch.zhaw.psit4.helper.ZipStreamTestHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.io.ByteArrayInputStream;
import java.util.zip.ZipInputStream;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test for ConfigurationController.
 *
 * @author Jona Braun
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@Import(BeanConfiguration.class)
public class ConfigurationControllerIT {
    private final SipClientTestHelper sipClientTestHelper = new SipClientTestHelper();
    private final DialPlanTestHelper dialPlanTestHelper = new DialPlanTestHelper();
    private final ZipStreamTestHelper zipStreamTestHelper = new ZipStreamTestHelper();

    @Autowired
    private WebApplicationContext wac;

    private DatabaseFixtureBuilder databaseFixtureBuilder;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        databaseFixtureBuilder = wac.getBean(DatabaseFixtureBuilder.class);
    }

    @Test
    public void testNoSipClients() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/v1/configuration/zip")
                        .accept(MediaType.ALL)
        ).andExpect(
                status().isInternalServerError()
        ).andExpect(
                jsonPath("$.reason").value("sipClientList is empty")
        );
    }

    @Test
    public void getAsteriskConfigurationTestZipAttachment() throws Exception {
        databaseFixtureBuilder.company(1);
        for (int i = 1; i <= 12; i++) {
            databaseFixtureBuilder.addSipClient(i);
        }
        databaseFixtureBuilder.build();

        MvcResult mvcResult = this.mockMvc.perform(get("/v1/configuration/zip"))
                .andReturn();

        ByteArrayInputStream bais = new ByteArrayInputStream(mvcResult.getResponse().getContentAsByteArray());

        assertNotNull("the Input stream was null", bais);

        ZipInputStream zipInputStream = new ZipInputStream(bais);

        String[] expectedNames = {"sip.conf", "extensions.conf"};
        String[] expectedContent = {sipClientTestHelper.generateSipClientConfig(12, CompanyData.getCompanyName(1)),
                dialPlanTestHelper.getSimpleDialPlanEntrySameCompany(12, CompanyData.getCompanyName(1))};

        zipStreamTestHelper.testZipEntryContent(zipInputStream, expectedNames, expectedContent);
    }
}