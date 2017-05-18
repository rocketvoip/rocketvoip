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
import ch.zhaw.psit4.services.implementation.CompanyServiceImpl;
import ch.zhaw.psit4.testsupport.convenience.Json;
import ch.zhaw.psit4.testsupport.fixtures.database.BeanConfiguration;
import ch.zhaw.psit4.testsupport.fixtures.database.DatabaseFixtureBuilder;
import ch.zhaw.psit4.testsupport.fixtures.dto.CompanyDtoGenerator;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static ch.zhaw.psit4.testsupport.matchers.CompanyDtoEqualTo.companyDtoEqualTo;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Jona Braun
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@Import(BeanConfiguration.class)
public class CompanyControllerOperatorIT {
    private static final String V1_COMPANIES_PATH = "/v1/companies";
    private static final int NON_EXISTING_COMPANY_ID = 100;

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
    public void getAllCompaniesEmpty() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(V1_COMPANIES_PATH)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isOk()
        ).andExpect(
                jsonPath("$.length()").value(equalTo(0))
        );
    }

    @Test
    public void getNonExistingCompany() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(V1_COMPANIES_PATH + "/{id}", NON_EXISTING_COMPANY_ID)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isNotFound()
        ).andExpect(
                jsonPath("$.reason").value(equalTo("Could not find company with id " + NON_EXISTING_COMPANY_ID))
        );
    }

    @Test
    public void deleteNonExistingCompany() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete(V1_COMPANIES_PATH + "/{id}", NON_EXISTING_COMPANY_ID)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isNotFound()
        ).andExpect(
                jsonPath("$.reason").value(startsWith("Could not delete company with id " +
                        NON_EXISTING_COMPANY_ID))
        );
    }

    @Test
    public void updateNonExistingCompany() throws Exception {
        CompanyDto companyDto = CompanyDtoGenerator.getCompanyDto(1);

        mockMvc.perform(
                MockMvcRequestBuilders.put(V1_COMPANIES_PATH + "/{id}", NON_EXISTING_COMPANY_ID)
                        .content(Json.toJson(companyDto))
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isBadRequest()
        );
    }

    @Test
    public void createInvalidCompany() throws Exception {
        CompanyDto companyDto = new CompanyDto();
        mockMvc.perform(
                MockMvcRequestBuilders.post(V1_COMPANIES_PATH)
                        .content(Json.toJson(companyDto))
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isBadRequest()
        );
    }

    @Test
    public void createCompany() throws Exception {
        CompanyDto companyDto = CompanyDtoGenerator.getCompanyDto(1);

        String creationResponse = mockMvc.perform(
                MockMvcRequestBuilders.post(V1_COMPANIES_PATH)
                        .content(Json.toJson(companyDto))
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isCreated()
        ).andExpect(
                jsonPath("$.id").value(not(equalTo(companyDto.getId())))
        ).andExpect(
                jsonPath("$.name").value(equalTo(companyDto.getName()))
        ).andReturn().getResponse().getContentAsString();

        CompanyDto createdCompanyDto = Json.toObjectTypeSafe(creationResponse, CompanyDto.class);

        String response = mockMvc.perform(
                MockMvcRequestBuilders.get(V1_COMPANIES_PATH + "/{id}", createdCompanyDto.getId())
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isOk()
        ).andReturn().getResponse().getContentAsString();
        CompanyDto actual = Json.toObjectTypeSafe(response, CompanyDto.class);

        assertThat(createdCompanyDto, companyDtoEqualTo(actual));
    }

    @Test
    public void getAllCompanies() throws Exception {
        databaseFixtureBuilder1.setCompany(1).build();
        databaseFixtureBuilder2.setCompany(2).build();

        String response = mockMvc.perform(
                MockMvcRequestBuilders.get(V1_COMPANIES_PATH)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isOk()
        ).andExpect(
                jsonPath("$.length()").value(equalTo(2))
        ).andReturn().getResponse().getContentAsString();

        CompanyDto createdCompanyDto1 = CompanyServiceImpl.companyEntityToCompanyDto(
                databaseFixtureBuilder1.getFirstCompany()
        );
        CompanyDto createdCompanyDto2 = CompanyServiceImpl.companyEntityToCompanyDto(
                databaseFixtureBuilder2.getFirstCompany()
        );
        CompanyDto[] actual = Json.toObjectTypeSafe(response, CompanyDto[].class);

        assertThat(actual, arrayContainingInAnyOrder(
                companyDtoEqualTo(createdCompanyDto1),
                companyDtoEqualTo(createdCompanyDto2)
        ));
    }

    @Test
    public void updateCompany() throws Exception {
        databaseFixtureBuilder1.setCompany(1).build();
        CompanyDto existingCompany = CompanyServiceImpl.companyEntityToCompanyDto(
                databaseFixtureBuilder1.getFirstCompany()
        );

        CompanyDto updatedCompany = CompanyDtoGenerator.getCompanyDto(2);
        updatedCompany.setId(existingCompany.getId());

        String putResult = mockMvc.perform(
                MockMvcRequestBuilders.put(V1_COMPANIES_PATH + "/{id}", existingCompany.getId())
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(Json.toJson(updatedCompany))
        ).andExpect(
                status().isOk()
        ).andReturn().getResponse().getContentAsString();

        CompanyDto actual = Json.toObjectTypeSafe(putResult, CompanyDto.class);
        assertThat(actual, companyDtoEqualTo(updatedCompany));

        String response = mockMvc.perform(
                MockMvcRequestBuilders.get(V1_COMPANIES_PATH + "/{id}", existingCompany.getId())
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isOk()
        ).andReturn().getResponse().getContentAsString();

        actual = Json.toObjectTypeSafe(response, CompanyDto.class);

        assertThat(actual, companyDtoEqualTo(updatedCompany));
    }

    @Test
    public void deleteCompany() throws Exception {
        databaseFixtureBuilder1.setCompany(1).build();
        CompanyDto existingCompany = CompanyServiceImpl.companyEntityToCompanyDto(
                databaseFixtureBuilder1.getFirstCompany()
        );

        mockMvc.perform(
                MockMvcRequestBuilders.delete(V1_COMPANIES_PATH + "/{id}", existingCompany.getId())
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isNoContent()
        );

        mockMvc.perform(
                MockMvcRequestBuilders.get(V1_COMPANIES_PATH + "/{id}", existingCompany.getId())
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isNotFound()
        );
    }
}