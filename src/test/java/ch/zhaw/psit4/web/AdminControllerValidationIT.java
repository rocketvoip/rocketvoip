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

import ch.zhaw.psit4.dto.AdminDto;
import ch.zhaw.psit4.dto.AdminWithPasswordDto;
import ch.zhaw.psit4.services.interfaces.AdminServiceInterface;
import ch.zhaw.psit4.testsupport.convenience.Json;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test validation of AdminDTO.
 *
 * @author Rafael Ostertag
 */
@RunWith(SpringRunner.class)
@WebMvcTest(AdminController.class)
public class AdminControllerValidationIT {
    private static final String V1_ADMINS_PATH = "/v1/admins";

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @MockBean
    private AdminServiceInterface adminService;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void validPost() throws Exception {
        AdminWithPasswordDto adminWithPasswordDto = new AdminWithPasswordDto();
        adminWithPasswordDto.setCompanyDtoList(Collections.emptyList());
        adminWithPasswordDto.setPassword("12345678");
        adminWithPasswordDto.setFirstName("ab");
        adminWithPasswordDto.setLastName("ab");
        adminWithPasswordDto.setUserName("ab");

        mockMvc.perform(
                MockMvcRequestBuilders.post(V1_ADMINS_PATH)
                        .content(Json.toJson(adminWithPasswordDto))
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isCreated()
        );
    }

    @Test
    public void validPut() throws Exception {
        AdminDto adminDto = new AdminDto();
        adminDto.setCompanyDtoList(Collections.emptyList());
        adminDto.setFirstName("ab");
        adminDto.setLastName("ab");
        adminDto.setUserName("ab");

        mockMvc.perform(
                MockMvcRequestBuilders.put(V1_ADMINS_PATH + "/{id}", 1)
                        .content(Json.toJson(adminDto))
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isOk()
        );
    }

    @Test
    public void postNullCompanies() throws Exception {
        AdminWithPasswordDto adminWithPasswordDto = new AdminWithPasswordDto();
        adminWithPasswordDto.setCompanyDtoList(null);
        adminWithPasswordDto.setPassword("12345678");
        adminWithPasswordDto.setFirstName("ab");
        adminWithPasswordDto.setLastName("ab");
        adminWithPasswordDto.setUserName("ab");

        mockMvc.perform(
                MockMvcRequestBuilders.post(V1_ADMINS_PATH)
                        .content(Json.toJson(adminWithPasswordDto))
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isBadRequest()
        );
    }

   /* TODO: Uncomment once method parameter validation works.
   @Test
    public void postNullPassword() throws Exception {
        AdminWithPasswordDto adminWithPasswordDto = new AdminWithPasswordDto();
        adminWithPasswordDto.setCompanyDtoList(Collections.emptyList());
        adminWithPasswordDto.setPassword(null);
        adminWithPasswordDto.setFirstName("ab");
        adminWithPasswordDto.setLastName("ab");
        adminWithPasswordDto.setUserName("ab");

        mockMvc.perform(
                MockMvcRequestBuilders.post(V1_ADMINS_PATH)
                        .content(Json.toJson(adminWithPasswordDto))
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isBadRequest()
        );
    }

    @Test
    public void postShortPassword() throws Exception {
        AdminWithPasswordDto adminWithPasswordDto = new AdminWithPasswordDto();
        adminWithPasswordDto.setCompanyDtoList(Collections.emptyList());
        adminWithPasswordDto.setPassword("1234567");
        adminWithPasswordDto.setFirstName("ab");
        adminWithPasswordDto.setLastName("ab");
        adminWithPasswordDto.setUserName("ab");

        mockMvc.perform(
                MockMvcRequestBuilders.post(V1_ADMINS_PATH)
                        .content(Json.toJson(adminWithPasswordDto))
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isBadRequest()
        );
    }*/

    @Test
    public void postNullFirstName() throws Exception {
        AdminWithPasswordDto adminWithPasswordDto = new AdminWithPasswordDto();
        adminWithPasswordDto.setCompanyDtoList(Collections.emptyList());
        adminWithPasswordDto.setPassword("12345678");
        adminWithPasswordDto.setFirstName(null);
        adminWithPasswordDto.setLastName("ab");
        adminWithPasswordDto.setUserName("ab");

        mockMvc.perform(
                MockMvcRequestBuilders.post(V1_ADMINS_PATH)
                        .content(Json.toJson(adminWithPasswordDto))
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isBadRequest()
        );
    }

    @Test
    public void postShortFirstName() throws Exception {
        AdminWithPasswordDto adminWithPasswordDto = new AdminWithPasswordDto();
        adminWithPasswordDto.setCompanyDtoList(Collections.emptyList());
        adminWithPasswordDto.setPassword("12345678");
        adminWithPasswordDto.setFirstName("a");
        adminWithPasswordDto.setLastName("ab");
        adminWithPasswordDto.setUserName("ab");

        mockMvc.perform(
                MockMvcRequestBuilders.post(V1_ADMINS_PATH)
                        .content(Json.toJson(adminWithPasswordDto))
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isBadRequest()
        );
    }

    @Test
    public void postNullLastName() throws Exception {
        AdminWithPasswordDto adminWithPasswordDto = new AdminWithPasswordDto();
        adminWithPasswordDto.setCompanyDtoList(Collections.emptyList());
        adminWithPasswordDto.setPassword("12345678");
        adminWithPasswordDto.setFirstName("ab");
        adminWithPasswordDto.setLastName(null);
        adminWithPasswordDto.setUserName("ab");

        mockMvc.perform(
                MockMvcRequestBuilders.post(V1_ADMINS_PATH)
                        .content(Json.toJson(adminWithPasswordDto))
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isBadRequest()
        );
    }

    @Test
    public void postShortLastName() throws Exception {
        AdminWithPasswordDto adminWithPasswordDto = new AdminWithPasswordDto();
        adminWithPasswordDto.setCompanyDtoList(Collections.emptyList());
        adminWithPasswordDto.setPassword("12345678");
        adminWithPasswordDto.setFirstName("ab");
        adminWithPasswordDto.setLastName("a");
        adminWithPasswordDto.setUserName("ab");

        mockMvc.perform(
                MockMvcRequestBuilders.post(V1_ADMINS_PATH)
                        .content(Json.toJson(adminWithPasswordDto))
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isBadRequest()
        );
    }

    @Test
    public void postNullUserName() throws Exception {
        AdminWithPasswordDto adminWithPasswordDto = new AdminWithPasswordDto();
        adminWithPasswordDto.setCompanyDtoList(Collections.emptyList());
        adminWithPasswordDto.setPassword("12345678");
        adminWithPasswordDto.setFirstName("ab");
        adminWithPasswordDto.setLastName("ab");
        adminWithPasswordDto.setUserName(null);

        mockMvc.perform(
                MockMvcRequestBuilders.post(V1_ADMINS_PATH)
                        .content(Json.toJson(adminWithPasswordDto))
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isBadRequest()
        );
    }

    @Test
    public void postShortUserName() throws Exception {
        AdminWithPasswordDto adminWithPasswordDto = new AdminWithPasswordDto();
        adminWithPasswordDto.setCompanyDtoList(Collections.emptyList());
        adminWithPasswordDto.setPassword("12345678");
        adminWithPasswordDto.setFirstName("ab");
        adminWithPasswordDto.setLastName("ab");
        adminWithPasswordDto.setUserName("a");

        mockMvc.perform(
                MockMvcRequestBuilders.post(V1_ADMINS_PATH)
                        .content(Json.toJson(adminWithPasswordDto))
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isBadRequest()
        );
    }

    /*
     * P U T
     */

    @Test
    public void putNullCompanies() throws Exception {
        AdminDto adminDto = new AdminDto();
        adminDto.setCompanyDtoList(null);
        adminDto.setFirstName("ab");
        adminDto.setLastName("ab");
        adminDto.setUserName("ab");

        mockMvc.perform(
                MockMvcRequestBuilders.put(V1_ADMINS_PATH + "/{id}", 1)
                        .content(Json.toJson(adminDto))
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isBadRequest()
        );
    }

   /* TODO: Uncomment once method parameter validation works.
   @Test
    public void putNullPassword() throws Exception {
        AdminDto adminWithPasswordDto = new AdminDto();
        adminDto.setCompanyDtoList(Collections.emptyList());
        adminDto.setFirstName("ab");
        adminDto.setLastName("ab");
        adminDto.setUserName("ab");

        mockMvc.perform(
                MockMvcRequestBuilders.put(V1_ADMINS_PATH + "/{id}", 1)
                        .content(Json.toJson(adminWithPasswordDto))
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isBadRequest()
        );
    }

    @Test
    public void putShortPassword() throws Exception {
        AdminDto adminWithPasswordDto = new AdminDto();
        adminDto.setCompanyDtoList(Collections.emptyList());
        adminDto.setFirstName("ab");
        adminDto.setLastName("ab");
        adminDto.setUserName("ab");

        mockMvc.perform(
                MockMvcRequestBuilders.put(V1_ADMINS_PATH+"/{id}", 1)
                        .content(Json.toJson(adminWithPasswordDto))
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isBadRequest()
        );
    }*/

    @Test
    public void putNullFirstName() throws Exception {
        AdminDto adminDto = new AdminDto();
        adminDto.setCompanyDtoList(Collections.emptyList());
        adminDto.setFirstName(null);
        adminDto.setLastName("ab");
        adminDto.setUserName("ab");

        mockMvc.perform(
                MockMvcRequestBuilders.put(V1_ADMINS_PATH + "/{id}", 1)
                        .content(Json.toJson(adminDto))
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isBadRequest()
        );
    }

    @Test
    public void putShortFirstName() throws Exception {
        AdminDto adminDto = new AdminDto();
        adminDto.setCompanyDtoList(Collections.emptyList());
        adminDto.setFirstName("a");
        adminDto.setLastName("ab");
        adminDto.setUserName("ab");

        mockMvc.perform(
                MockMvcRequestBuilders.put(V1_ADMINS_PATH + "/{id}", 1)
                        .content(Json.toJson(adminDto))
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isBadRequest()
        );
    }

    @Test
    public void putNullLastName() throws Exception {
        AdminDto adminDto = new AdminDto();
        adminDto.setCompanyDtoList(Collections.emptyList());
        adminDto.setFirstName("ab");
        adminDto.setLastName(null);
        adminDto.setUserName("ab");

        mockMvc.perform(
                MockMvcRequestBuilders.put(V1_ADMINS_PATH + "/{id}", 1)
                        .content(Json.toJson(adminDto))
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isBadRequest()
        );
    }

    @Test
    public void putShortLastName() throws Exception {
        AdminDto adminDto = new AdminDto();
        adminDto.setCompanyDtoList(Collections.emptyList());
        adminDto.setFirstName("ab");
        adminDto.setLastName("a");
        adminDto.setUserName("ab");

        mockMvc.perform(
                MockMvcRequestBuilders.put(V1_ADMINS_PATH + "/{id}", 1)
                        .content(Json.toJson(adminDto))
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isBadRequest()
        );
    }

    @Test
    public void putNullUserName() throws Exception {
        AdminDto adminDto = new AdminDto();
        adminDto.setCompanyDtoList(Collections.emptyList());
        adminDto.setFirstName("ab");
        adminDto.setLastName("ab");
        adminDto.setUserName(null);

        mockMvc.perform(
                MockMvcRequestBuilders.put(V1_ADMINS_PATH + "/{id}", 1)
                        .content(Json.toJson(adminDto))
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isBadRequest()
        );
    }

    @Test
    public void putShortUserName() throws Exception {
        AdminDto adminDto = new AdminDto();
        adminDto.setCompanyDtoList(Collections.emptyList());
        adminDto.setFirstName("ab");
        adminDto.setLastName("ab");
        adminDto.setUserName("a");

        mockMvc.perform(
                MockMvcRequestBuilders.put(V1_ADMINS_PATH + "/{id}", 1)
                        .content(Json.toJson(adminDto))
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isBadRequest()
        );
    }
}
