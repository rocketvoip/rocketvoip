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

import ch.zhaw.psit4.dto.CompanyDto;
import ch.zhaw.psit4.dto.SipClientDto;
import ch.zhaw.psit4.services.implementation.CompanyServiceImpl;
import ch.zhaw.psit4.services.implementation.SipClientServiceImpl;
import ch.zhaw.psit4.testsupport.convenience.Json;
import ch.zhaw.psit4.testsupport.fixtures.database.BeanConfiguration;
import ch.zhaw.psit4.testsupport.fixtures.database.DatabaseFixtureBuilder;
import ch.zhaw.psit4.testsupport.fixtures.dto.SipClientDtoGenerator;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static ch.zhaw.psit4.testsupport.matchers.SipClientDtoEqualTo.sipClientDtoEqualTo;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Rafael Ostertag
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@Import(BeanConfiguration.class)
public class SipClientControllerIT {
    private static final int NON_EXISTING_USER_ID = 100;

    @Autowired
    private WebApplicationContext wac;

    private DatabaseFixtureBuilder databaseFixtureBuilder1;
    private DatabaseFixtureBuilder databaseFixtureBuilder2;
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        databaseFixtureBuilder1 = wac.getBean(DatabaseFixtureBuilder.class);
        databaseFixtureBuilder2 = wac.getBean(DatabaseFixtureBuilder.class);
    }

    @Test
    public void getAllSipClientEmpty() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/v1/sipclients")
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isOk()
        ).andExpect(
                jsonPath("$.length()").value(equalTo(0))
        );
    }

    @Test
    public void getNonExistingSipClient() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/v1/sipclients/{id}", NON_EXISTING_USER_ID)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isNotFound()
        ).andExpect(
                jsonPath("$.reason").value(equalTo("Could not find SIP Client with id " + NON_EXISTING_USER_ID))
        );
    }

    @Test
    public void deleteNonExistingSipClient() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/v1/sipclients/{id}", NON_EXISTING_USER_ID)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isNotFound()
        ).andExpect(
                jsonPath("$.reason").value(startsWith("Could not delete SIP Client with id " +
                        NON_EXISTING_USER_ID))
        );
    }

    @Test
    public void updateNonExistingSipClient() throws Exception {
        SipClientDto sipClientDto = SipClientDtoGenerator.createTestSipClientDto((CompanyDto) null, 1);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/v1/sipclients/{id}", NON_EXISTING_USER_ID)
                        .content(Json.toJson(sipClientDto))
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isBadRequest()
        );
    }

    @Test
    public void updateSipClient() throws Exception {
        databaseFixtureBuilder1.setCompany(1).addSipClient(1).build();
        SipClientDto existingSipClient = SipClientServiceImpl.sipClientEntityToSipClientDto
                (databaseFixtureBuilder1.getSipClientList().get(1));

        SipClientDto updatedSipClient = SipClientDtoGenerator.createTestSipClientDto(existingSipClient.getCompany(), 2);
        updatedSipClient.setId(existingSipClient.getId());

        String putResult = mockMvc.perform(
                MockMvcRequestBuilders.put("/v1/sipclients/{id}", existingSipClient.getId())
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(Json.toJson(updatedSipClient))
        ).andExpect(
                status().isOk()
        ).andReturn().getResponse().getContentAsString();

        SipClientDto actual = Json.toObjectTypeSafe(putResult, SipClientDto.class);
        assertThat(actual, sipClientDtoEqualTo(updatedSipClient));

        String response = mockMvc.perform(
                MockMvcRequestBuilders.get("/v1/sipclients/{id}", existingSipClient.getId())
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isOk()
        ).andReturn().getResponse().getContentAsString();

        actual = Json.toObjectTypeSafe(response, SipClientDto.class);

        assertThat(actual, sipClientDtoEqualTo(updatedSipClient));
    }

    @Test
    public void createInvalidSipClient() throws Exception {
        SipClientDto sipClientDto = new SipClientDto();
        mockMvc.perform(
                MockMvcRequestBuilders.post("/v1/sipclients")
                        .content(Json.toJson(sipClientDto))
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isBadRequest()
        );
    }

    @Test
    public void createSipClient() throws Exception {
        databaseFixtureBuilder2.setCompany(2).build();
        CompanyDto companyDto = CompanyServiceImpl.companyEntityToCompanyDto(
                databaseFixtureBuilder2.getFirstCompany()
        );

        SipClientDto sipClientDto = SipClientDtoGenerator.createTestSipClientDto(companyDto, 3);
        String creationResponse = mockMvc.perform(
                MockMvcRequestBuilders.post("/v1/sipclients")
                        .content(Json.toJson(sipClientDto))
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(CoreMatchers.not(CoreMatchers.equalTo(sipClientDto.getId
                        ())))
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(CoreMatchers.equalTo(sipClientDto.getName()))
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.phone").value(CoreMatchers.equalTo(sipClientDto.getPhone()))
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.secret").value(CoreMatchers.equalTo(sipClientDto.getSecret()))
        ).andReturn().getResponse().getContentAsString();

        SipClientDto createdSipClient = Json.toObjectTypeSafe(creationResponse, SipClientDto.class);

        String response = mockMvc.perform(
                MockMvcRequestBuilders.get("/v1/sipclients/{id}", createdSipClient.getId())
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isOk()
        ).andReturn().getResponse().getContentAsString();
        SipClientDto actual = Json.toObjectTypeSafe(response, SipClientDto.class);

        assertThat(createdSipClient, sipClientDtoEqualTo(actual));
    }

    @Test
    public void getAllSipClients() throws Exception {
        databaseFixtureBuilder1.setCompany(1).addSipClient(1).build();
        databaseFixtureBuilder2.setCompany(2).addSipClient(2).build();

        String response = mockMvc.perform(
                MockMvcRequestBuilders.get("/v1/sipclients")
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isOk()
        ).andReturn().getResponse().getContentAsString();

        SipClientDto createdSipClient1 = SipClientServiceImpl.sipClientEntityToSipClientDto(
                databaseFixtureBuilder1.getSipClientList().get(1)
        );
        SipClientDto createdSipClient2 = SipClientServiceImpl.sipClientEntityToSipClientDto(
                databaseFixtureBuilder2.getSipClientList().get(2)
        );

        SipClientDto[] actual = Json.toObjectTypeSafe(response, SipClientDto[].class);

        assertThat(actual, arrayContainingInAnyOrder(
                sipClientDtoEqualTo(createdSipClient1),
                sipClientDtoEqualTo(createdSipClient2)
        ));
    }

    @Test
    public void deleteSipClient() throws Exception {
        databaseFixtureBuilder1.setCompany(1).addSipClient(1).build();
        SipClientDto createdSipClient1 = SipClientServiceImpl.sipClientEntityToSipClientDto(
                databaseFixtureBuilder1.getSipClientList().get(1)
        );

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/v1/sipclients/{id}", createdSipClient1.getId())
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isNoContent()
        );

        mockMvc.perform(
                MockMvcRequestBuilders.get("/v1/sipclients/{id}", createdSipClient1.getId())
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isNotFound()
        );
    }
}