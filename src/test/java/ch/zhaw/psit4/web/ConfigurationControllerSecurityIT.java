package ch.zhaw.psit4.web;

import ch.zhaw.psit4.security.auxiliary.AdminDetails;
import ch.zhaw.psit4.security.auxiliary.SecurityConstants;
import ch.zhaw.psit4.security.jwt.TokenHandler;
import ch.zhaw.psit4.testsupport.fixtures.database.BeanConfiguration;
import ch.zhaw.psit4.testsupport.fixtures.database.DatabaseFixtureBuilder;
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
public class ConfigurationControllerSecurityIT {
    @Autowired
    private TokenHandler tokenHandler;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    private DatabaseFixtureBuilder databaseFixtureBuilder;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        databaseFixtureBuilder = context.getBean(DatabaseFixtureBuilder.class);
    }

    @Test
    public void testUnauthenticated() throws Exception {
        mvc.perform(get("/v1/configuration/zip")).andExpect(
                status().isUnauthorized()
        );
    }

    @Test(expected = AccessDeniedException.class)
    public void testEndpointWithUnauthorizedUser() throws Exception {
        databaseFixtureBuilder.company(1).addAdministrator(1).build();
        String authToken = tokenHandler.createTokenForUser(new AdminDetails(databaseFixtureBuilder.getAdminList().get
                (1)));

        mvc.perform(
                get("/v1/configuration/zip")
                        .accept("application/zip")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header(SecurityConstants.AUTH_HEADER_NAME, authToken)
        );
    }

    @Test
    public void testEndpointWithAuthorizedUser() throws Exception {
        databaseFixtureBuilder.company(1)
                .addSipClient(1)
                .addOperator(1)
                .build();

        String authToken = tokenHandler.createTokenForUser(new AdminDetails(databaseFixtureBuilder.getOperatorList().get
                (1)));

        mvc.perform(
                get("/v1/configuration/zip")
                        .accept("application/zip")
                        .header(SecurityConstants.AUTH_HEADER_NAME, authToken)
        ).andExpect(
                status().isOk()
        );
    }


}
