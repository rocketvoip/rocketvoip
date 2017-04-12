package ch.zhaw.psit4.web;

import ch.zhaw.psit4.fixtures.database.BeanConfiguration;
import ch.zhaw.psit4.fixtures.database.DatabaseFixtureBuilder;
import ch.zhaw.psit4.fixtures.general.AdminData;
import ch.zhaw.psit4.helper.Json;
import ch.zhaw.psit4.security.auxiliary.LoginData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Rafael Ostertag
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
@Import(BeanConfiguration.class)
public class LoginIT {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    private DatabaseFixtureBuilder databaseFixtureBuilder;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        databaseFixtureBuilder = context.getBean(DatabaseFixtureBuilder.class);
    }

    @Test
    public void testUnknownUser() throws Exception {
        String loginJsonStream = makeAuthenticationJsonStream("doesnotexist", "bla");
        mockMvc.perform(
                post("/v1/login").contentType(MediaType.APPLICATION_JSON_UTF8).content(loginJsonStream)
        ).andExpect(
                status().isUnauthorized()
        );
    }

    @Test
    public void testKnownUser() throws Exception {
        databaseFixtureBuilder.addAdministrator(1).build();

        String loginJsonStream = makeAuthenticationJsonStream(AdminData.getAdminUsername(1), AdminData
                .getAdminPassword(1)
        );
        mockMvc.perform(
                post("/v1/login").contentType(MediaType.APPLICATION_JSON_UTF8).content(loginJsonStream)
        ).andExpect(
                status().isOk()
        );
    }

    private String makeAuthenticationJsonStream(String username, String password) throws Exception {
        LoginData loginData = new LoginData();
        loginData.setUsername(username);
        loginData.setPassword(password);

        return Json.toJson(loginData);
    }
}
