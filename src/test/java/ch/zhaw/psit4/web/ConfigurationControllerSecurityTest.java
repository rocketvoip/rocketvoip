package ch.zhaw.psit4.web;

import ch.zhaw.psit4.security.auxiliary.SecurityConstants;
import ch.zhaw.psit4.services.interfaces.ConfigServiceInterface;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Rafael Ostertag
 */
@RunWith(SpringRunner.class)
@WebMvcTest(ConfigurationController.class)
public class ConfigurationControllerSecurityTest {
    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;


    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void testUnauthenticated() throws Exception {
        mvc.perform(get("/v1/configuration/zip")).andExpect(
                status().isUnauthorized()
        );
    }

    @Test
    @WithMockUser(username = "test", roles = {})
    public void noRoles() throws Exception {
        mvc.perform(get("/v1/configuration/zip")).andExpect(
                status().isForbidden()
        );
    }

    @Test
    @WithMockUser(username = "test", roles = {SecurityConstants.COMPANY_ADMIN_ROLE_NAME})
    public void testConfigurationEndpointWithUnauthorizedUser() throws Exception {
        mvc.perform(get("/v1/configuration/zip")).andExpect(
                status().isForbidden()
        );
    }

    @Test
    public void testConfigurationEndpointWithAuthorizedUser() throws Exception {
        mvc.perform(get("/v1/configuration/zip")
                .with(user("admin").roles(SecurityConstants.CONFIG_ADMIN_ROLE_NAME))
                .accept("application/zip")
        ).andExpect(
                status().isOk()
        );
    }

    @TestConfiguration
    public static class Configuration {
        @Bean
        public static ConfigServiceInterface configServiceInterface() {
            return mock(ConfigServiceInterface.class);
        }
    }
}
