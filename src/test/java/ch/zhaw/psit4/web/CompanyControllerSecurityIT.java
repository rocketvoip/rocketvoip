package ch.zhaw.psit4.web;

import ch.zhaw.psit4.fixtures.database.BeanConfiguration;
import ch.zhaw.psit4.fixtures.database.DatabaseFixtureBuilder;
import ch.zhaw.psit4.helper.security.AuthenticationToken;
import ch.zhaw.psit4.security.auxiliary.SecurityConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Rafael Ostertag
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@Import(BeanConfiguration.class)
public class CompanyControllerSecurityIT {
    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    private DatabaseFixtureBuilder databaseFixtureBuilder;


    @Before
    public void setup() throws Exception {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        databaseFixtureBuilder = context.getBean(DatabaseFixtureBuilder.class);
    }

    @Test
    public void testUnauthenticated() throws Exception {
        mvc.perform(get("/v1/companies/")).andExpect(
                status().isUnauthorized()
        );
    }

    @Test(expected = AccessDeniedException.class)
    public void testConfigurationEndpointWithUnauthorizedUser() throws Exception {
        databaseFixtureBuilder.company(1).addAdministrator(1).build();
        String authToken = AuthenticationToken.createTokenFor(
                databaseFixtureBuilder.getAdminList().get(1)
        );

        mvc.perform(
                get("/v1/companies")
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header(SecurityConstants.AUTH_HEADER_NAME, authToken)
        ).andExpect(
                status().isForbidden()
        );
    }

    @Test()
    public void testConfigurationEndpointWithAuthorizedUser() throws Exception {
        databaseFixtureBuilder.company(1).addOperator(1).build();

        String authToken = AuthenticationToken.createTokenFor(
                databaseFixtureBuilder.getOperatorList().get(1)
        );

        mvc.perform(
                get("/v1/companies")
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header(SecurityConstants.AUTH_HEADER_NAME, authToken)
        ).andExpect(
                status().isOk()
        );
    }
}
