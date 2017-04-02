package ch.zhaw.psit4.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Jona Braun
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class CompanyControllerIT {
    private static final int NON_EXISTING_USER_ID = 100;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void getAllCompanies() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/v1/companies")
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isOk()
        ).andExpect(
                jsonPath("$.length()").value(equalTo(1))
        );
    }

    @Test
    public void getCompany() throws Exception {
    }

    @Test
    public void deleteCompany() throws Exception {
    }

    @Test
    public void updateCompany() throws Exception {
    }

    @Test
    public void createCompany() throws Exception {
    }

    @Test
    public void handleCompanyRetrievalException() throws Exception {
    }

    @Test
    public void handleCompanyDeletionException() throws Exception {
    }

    @Test
    public void handleException() throws Exception {
    }

}