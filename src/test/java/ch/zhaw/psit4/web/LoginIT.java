/*
 * Copyright 2017 Jona Braun, Benedikt Herzog, Rafael Ostertag,
 *                Marcel Schöni, Marco Studerus, Martin Wittwer
 *
 * Redistribution and  use in  source and binary  forms, with  or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * 1. Redistributions  of  source code  must retain  the above  copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in  binary form must reproduce  the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation   and/or   other    materials   provided   with   the
 *    distribution.
 *
 * THIS SOFTWARE  IS PROVIDED BY  THE COPYRIGHT HOLDERS  AND CONTRIBUTORS
 * "AS  IS" AND  ANY EXPRESS  OR IMPLIED  WARRANTIES, INCLUDING,  BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES  OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE  ARE DISCLAIMED. IN NO EVENT  SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL,  EXEMPLARY,  OR  CONSEQUENTIAL DAMAGES  (INCLUDING,  BUT  NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE  GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS  INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF  LIABILITY, WHETHER IN  CONTRACT, STRICT LIABILITY,  OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN  ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package ch.zhaw.psit4.web;

import ch.zhaw.psit4.security.dto.LoginDataDto;
import ch.zhaw.psit4.testsupport.convenience.Json;
import ch.zhaw.psit4.testsupport.fixtures.database.BeanConfiguration;
import ch.zhaw.psit4.testsupport.fixtures.database.DatabaseFixtureBuilder;
import ch.zhaw.psit4.testsupport.fixtures.general.AdminData;
import ch.zhaw.psit4.testsupport.fixtures.general.OperatorAdminData;
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

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
                .defaultRequest(
                        post("/")
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                )
                .build();

        databaseFixtureBuilder = context.getBean(DatabaseFixtureBuilder.class);
    }

    @Test
    public void emptyBody() throws Exception {
        mockMvc.perform(
                post("/v1/login")
        ).andExpect(
                status().isUnauthorized()
        );
    }

    @Test
    public void testUnknownUser() throws Exception {
        String loginJsonStream = makeAuthenticationJsonStream("doesnotexist", "bla");
        mockMvc.perform(
                post("/v1/login").content(loginJsonStream)
        ).andExpect(
                status().isUnauthorized()
        );
    }

    @Test
    public void testAdmin() throws Exception {
        databaseFixtureBuilder.addAdministrator(1).build();

        String loginJsonStream = makeAuthenticationJsonStream(AdminData.getAdminUsername(1), AdminData
                .getAdminPassword(1)
        );
        mockMvc.perform(
                post("/v1/login").content(loginJsonStream)
        ).andExpect(
                status().isOk()
        ).andExpect(
                jsonPath("$.isOperator").value(equalTo(false))
        );
    }

    @Test
    public void testOperator() throws Exception {
        databaseFixtureBuilder.addOperator(1).build();

        String loginJsonStream = makeAuthenticationJsonStream(OperatorAdminData.getOperatorAdminUsername(1),
                OperatorAdminData.getOperatorAdminPassword(1)
        );
        mockMvc.perform(
                post("/v1/login").content(loginJsonStream)
        ).andExpect(
                status().isOk()
        ).andExpect(
                jsonPath("$.isOperator").value(equalTo(true))
        );
    }

    private String makeAuthenticationJsonStream(String username, String password) throws Exception {
        LoginDataDto loginDataDto = new LoginDataDto();
        loginDataDto.setUsername(username);
        loginDataDto.setPassword(password);

        return Json.toJson(loginDataDto);
    }
}
