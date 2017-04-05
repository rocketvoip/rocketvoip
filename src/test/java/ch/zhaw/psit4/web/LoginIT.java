package ch.zhaw.psit4.web;

import ch.zhaw.psit4.helper.Json;
import ch.zhaw.psit4.security.auxiliary.LoginData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Rafael Ostertag
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class LoginIT {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testUnknownUser() throws Exception {
        String loginJsonStream = makeAuthenticationJsonStream("doesnotexist", "bla");
        mockMvc.perform(
                post("/v1/login").contentType(MediaType.APPLICATION_JSON_UTF8).content(loginJsonStream)
        ).andExpect(
                status().isUnauthorized()
        );
    }

    private String makeAuthenticationJsonStream(String username, String password) throws Exception {
        LoginData loginData = new LoginData();
        loginData.setUsername(username);
        loginData.setPassword(password);

        return Json.toJson(loginData);
    }
}
