package ch.zhaw.psit4.web;

import ch.zhaw.psit4.domain.ConfigZipWriter;
import ch.zhaw.psit4.domain.dialplan.DialPlanConfigurationChanSip;
import ch.zhaw.psit4.testsupport.fixtures.database.BeanConfiguration;
import ch.zhaw.psit4.testsupport.fixtures.database.DatabaseFixtureBuilder;
import ch.zhaw.psit4.testsupport.helper.InputStreamStringyfier;
import ch.zhaw.psit4.testsupport.helper.ZipStreamReader;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
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
    public static final int NUMBER_OF_TEST_COMPANIES = 5;
    public static final int NUMBER_CLIENTS_PER_COMPANY = 3;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
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
        for (int companyNumber = 1; companyNumber <= NUMBER_OF_TEST_COMPANIES; companyNumber++) {
            DatabaseFixtureBuilder databaseFixtureBuilder = wac.getBean(DatabaseFixtureBuilder.class);
            databaseFixtureBuilder.company(companyNumber);
            for (int i = 1; i <= NUMBER_CLIENTS_PER_COMPANY; i++) {
                databaseFixtureBuilder.addSipClient(i);
            }
            databaseFixtureBuilder.build();
        }

        MvcResult mvcResult = this.mockMvc.perform(get("/v1/configuration/zip"))
                .andReturn();

        ByteArrayInputStream bais = new ByteArrayInputStream(mvcResult.getResponse().getContentAsByteArray());

        assertNotNull("the Input stream was null", bais);

        ZipStreamReader zipStreamReader = new ZipStreamReader(new ZipInputStream(bais));

        assertThat(zipStreamReader.hasFile(ConfigZipWriter.SIP_CONFIG_FILE_NAME), equalTo(true));
        assertThat(zipStreamReader.hasFile(ConfigZipWriter.DIAL_PLAN_CONFIG_FILE_NAME), equalTo(true));

        String expected = expected = InputStreamStringyfier.slurpStream(
                ConfigurationControllerIT.class.getResourceAsStream("/fixtures/fiveCompaniesThreeClients.txt")
        );
        assertThat(zipStreamReader.getContent(ConfigZipWriter.SIP_CONFIG_FILE_NAME),
                equalTo(expected)
        );

        expected = InputStreamStringyfier.slurpStream(
                DialPlanConfigurationChanSip.class.getResourceAsStream("/fixtures/fiveContextsThreeApps.txt")
        );
        assertThat(zipStreamReader.getContent(ConfigZipWriter.DIAL_PLAN_CONFIG_FILE_NAME), equalTo(expected));
    }
}