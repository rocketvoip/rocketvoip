package ch.zhaw.psit4.web;

import ch.zhaw.psit4.data.jpa.repositories.CompanyRepository;
import ch.zhaw.psit4.data.jpa.repositories.SipClientRepository;
import ch.zhaw.psit4.domain.helper.DialPlanTestHelper;
import ch.zhaw.psit4.domain.helper.SipClientTestHelper;
import ch.zhaw.psit4.dto.SipClientDto;
import ch.zhaw.psit4.helper.Json;
import ch.zhaw.psit4.helper.SipClientGenerator;
import ch.zhaw.psit4.helper.ZipStreamTestHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
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
public class ConfigurationControllerIT {
    private static final String COMPANY = "acme1";
    private final SipClientTestHelper sipClientTestHelper = new SipClientTestHelper();
    private final DialPlanTestHelper dialPlanTestHelper = new DialPlanTestHelper();
    private final ZipStreamTestHelper zipStreamTestHelper = new ZipStreamTestHelper();

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private SipClientRepository sipClientRepository;

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
        for (int i = 1; i <= 12; i++) {
            createSipClient(i);
        }

        MvcResult mvcResult = this.mockMvc.perform(get("/v1/configuration/zip"))
                .andReturn();

        ByteArrayInputStream bais = new ByteArrayInputStream(mvcResult.getResponse().getContentAsByteArray());

        assertNotNull("the Input stream was null", bais);

        ZipInputStream zipInputStream = new ZipInputStream(bais);

        String[] expectedNames = {"sip.conf", "extensions.conf"};
        String[] expectedContent = {sipClientTestHelper.generateSipClientConfig(12, "TestCompany"),
                dialPlanTestHelper.getSimpleDialPlanEntrySameCompany(12, "TestCompany")};

        zipStreamTestHelper.testZipEntryContent(zipInputStream, expectedNames, expectedContent);
    }


    // TODO clean duplicate code from SipClientControllerIT
    private void createSipClient(int number) throws Exception {
        SipClientDto sipClientDto = SipClientGenerator.createTestSipClientDto(number);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/v1/sipclients")
                        .content(Json.toJson(sipClientDto))
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isCreated()
        ).andExpect(
                jsonPath("$.id").value(not(equalTo(sipClientDto.getId())))
        ).andExpect(
                jsonPath("$.name").value(equalTo(sipClientDto.getName()))
        ).andExpect(
                jsonPath("$.phone").value(equalTo(sipClientDto.getPhone()))
        ).andExpect(
                jsonPath("$.secret").value(equalTo(sipClientDto.getSecret()))
        ).andReturn().getResponse().getContentAsString();
    }
}