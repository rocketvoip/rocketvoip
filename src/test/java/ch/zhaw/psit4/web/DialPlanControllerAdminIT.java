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

import ch.zhaw.psit4.data.jpa.entities.Dial;
import ch.zhaw.psit4.data.jpa.entities.SayAlpha;
import ch.zhaw.psit4.data.jpa.entities.SipClient;
import ch.zhaw.psit4.dto.ActionDto;
import ch.zhaw.psit4.dto.CompanyDto;
import ch.zhaw.psit4.dto.DialPlanDto;
import ch.zhaw.psit4.dto.actions.DialActionDto;
import ch.zhaw.psit4.dto.actions.SayAlphaActionDto;
import ch.zhaw.psit4.security.auxiliary.AdminDetails;
import ch.zhaw.psit4.security.auxiliary.SecurityConstants;
import ch.zhaw.psit4.security.jwt.TokenHandler;
import ch.zhaw.psit4.services.implementation.CompanyServiceImpl;
import ch.zhaw.psit4.services.implementation.DialPlanServiceImpl;
import ch.zhaw.psit4.testsupport.convenience.Json;
import ch.zhaw.psit4.testsupport.fixtures.database.BeanConfiguration;
import ch.zhaw.psit4.testsupport.fixtures.database.DatabaseFixtureBuilder;
import ch.zhaw.psit4.testsupport.fixtures.dto.ActionDtoGenerator;
import ch.zhaw.psit4.testsupport.fixtures.dto.DialActionDtoGenerator;
import ch.zhaw.psit4.testsupport.fixtures.dto.DialPlanDtoGenerator;
import ch.zhaw.psit4.testsupport.fixtures.dto.SayAlphaActionDtoGenerator;
import ch.zhaw.psit4.testsupport.fixtures.general.CompanyData;
import ch.zhaw.psit4.testsupport.fixtures.general.DialPlanData;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ch.zhaw.psit4.testsupport.matchers.ActionDtoPartialMatcher.actionDtoAlmostEqualTo;
import static ch.zhaw.psit4.testsupport.matchers.DialActionEqualTo.dialActionEqualTo;
import static ch.zhaw.psit4.testsupport.matchers.DialPlanDtoEqualTo.dialPlanDtoEqualTo;
import static ch.zhaw.psit4.testsupport.matchers.SayAlphaActionEqualTo.sayAlphaActionEqualTo;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Jona Braun
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@Import(BeanConfiguration.class)
public class DialPlanControllerAdminIT {
    private static final int NON_EXISTING_DIAL_PLAN_ID = 100;
    private static final String V1_DIAL_PLANS_PATH = "/v1/dialplans";

    @Autowired
    private TokenHandler tokenHandler;

    @Autowired
    private WebApplicationContext wac;

    private DatabaseFixtureBuilder databaseFixtureBuilder1;
    private DatabaseFixtureBuilder databaseFixtureBuilder2;
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.wac)
                .apply(springSecurity())
                .defaultRequest(MockMvcRequestBuilders
                        .get("/")
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                )
                .build();

        databaseFixtureBuilder1 = wac.getBean(DatabaseFixtureBuilder.class);
        databaseFixtureBuilder2 = wac.getBean(DatabaseFixtureBuilder.class);
    }

    @Test
    public void getAllDialPlansEmpty() throws Exception {
        databaseFixtureBuilder1
                .setCompany(1)
                .addAdministrator(1)
                .build();
        databaseFixtureBuilder2
                .setCompany(2)
                .addAdministrator(2)
                .build();
        String authToken = getTokenForAdministrator1Company1();

        mockMvc.perform(
                MockMvcRequestBuilders.get(V1_DIAL_PLANS_PATH)
                        .header(SecurityConstants.AUTH_HEADER_NAME, authToken)
        ).andExpect(
                status().isOk()
        ).andExpect(
                jsonPath("$.length()").value(equalTo(0))
        );
    }

    @Test
    public void getAllDialPlansCompany1() throws Exception {
        databaseFixtureBuilder1
                .setCompany(1)
                .addAdministrator(1)
                // DialPlan 1
                .addSipClient(1)
                .addDialPlan(1)
                .addDial(1, 1, 1, new int[]{1})
                .addSayAlpha(1, 2, 1)
                // DialPlan 2
                .addSipClient(2)
                .addDialPlan(2)
                .addDial(2, 1, 2, new int[]{2})
                .addSayAlpha(2, 2, 2)
                .build();
        databaseFixtureBuilder2
                .setCompany(2)
                .addAdministrator(2)
                // DialPlan 3
                .addSipClient(3)
                .addDialPlan(3)
                .addDial(3, 1, 3, new int[]{1})
                .addSayAlpha(3, 2, 3)
                // DialPlan 2
                .addSipClient(4)
                .addDialPlan(4)
                .addDial(4, 1, 4, new int[]{2})
                .addSayAlpha(4, 2, 4)
                .build();

        String authToken = getTokenForAdministrator1Company1();
        mockMvc.perform(
                MockMvcRequestBuilders.get(V1_DIAL_PLANS_PATH)
                        .header(SecurityConstants.AUTH_HEADER_NAME, authToken)
        ).andExpect(
                status().isOk()
        ).andExpect(
                jsonPath("$.length()").value(equalTo(2))
        ).andExpect(
                jsonPath("$[0].company.name").value(equalTo(CompanyData.getCompanyName(1)))
        ).andExpect(
                jsonPath("$[1].company.name").value(equalTo(CompanyData.getCompanyName(1)))
        ).andExpect(
                jsonPath("$[0].name").value(equalTo(DialPlanData.getTitle(1)))
        ).andExpect(
                jsonPath("$[1].name").value(equalTo(DialPlanData.getTitle(2)))
        ).andExpect(
                jsonPath("$[0].actions.length()").value(equalTo(2))
        ).andExpect(
                jsonPath("$[1].actions.length()").value(equalTo(2))
        );
    }

    @Test
    public void getAllDialPlansCompany2() throws Exception {
        databaseFixtureBuilder1
                .setCompany(1)
                .addAdministrator(1)
                // DialPlan 1
                .addSipClient(1)
                .addDialPlan(1)
                .addDial(1, 1, 1, new int[]{1})
                .addSayAlpha(1, 2, 1)
                // DialPlan 2
                .addSipClient(2)
                .addDialPlan(2)
                .addDial(2, 1, 2, new int[]{2})
                .addSayAlpha(2, 2, 2)
                .build();
        databaseFixtureBuilder2
                .setCompany(2)
                .addAdministrator(2)
                // DialPlan 3
                .addSipClient(3)
                .addDialPlan(3)
                .addDial(3, 1, 3, new int[]{3})
                .addSayAlpha(3, 2, 3)
                // DialPlan 2
                .addSipClient(4)
                .addDialPlan(4)
                .addDial(4, 1, 4, new int[]{4})
                .addSayAlpha(4, 2, 4)
                .build();

        String authToken = getTokenForAdministrator2Company2();
        mockMvc.perform(
                MockMvcRequestBuilders.get(V1_DIAL_PLANS_PATH)
                        .header(SecurityConstants.AUTH_HEADER_NAME, authToken)
        ).andExpect(
                status().isOk()
        ).andExpect(
                jsonPath("$.length()").value(equalTo(2))
        ).andExpect(
                jsonPath("$[0].company.name").value(equalTo(CompanyData.getCompanyName(2)))
        ).andExpect(
                jsonPath("$[1].company.name").value(equalTo(CompanyData.getCompanyName(2)))
        ).andExpect(
                jsonPath("$[0].name").value(equalTo(DialPlanData.getTitle(3)))
        ).andExpect(
                jsonPath("$[1].name").value(equalTo(DialPlanData.getTitle(4)))
        ).andExpect(
                jsonPath("$[0].actions.length()").value(equalTo(2))
        ).andExpect(
                jsonPath("$[1].actions.length()").value(equalTo(2))
        );
    }

    @Test
    public void getNonExistingDialPlan() throws Exception {
        databaseFixtureBuilder1
                .setCompany(1)
                .addAdministrator(1)
                .build();
        databaseFixtureBuilder2
                .setCompany(2)
                .addAdministrator(2)
                .build();

        String authToken = getTokenForAdministrator1Company1();

        mockMvc.perform(
                MockMvcRequestBuilders.get(V1_DIAL_PLANS_PATH + "/{id}", NON_EXISTING_DIAL_PLAN_ID)
                        .header(SecurityConstants.AUTH_HEADER_NAME, authToken)
        ).andExpect(
                status().isNotFound()
        ).andExpect(
                jsonPath("$.reason").value(equalTo("Could not find dial plan with id " + NON_EXISTING_DIAL_PLAN_ID))
        );
    }

    @Test
    public void deleteNonExistingDialPlan() throws Exception {
        databaseFixtureBuilder1
                .setCompany(1)
                .addAdministrator(1)
                .build();
        databaseFixtureBuilder2
                .setCompany(2)
                .addAdministrator(2)
                .build();
        String authToken = getTokenForAdministrator1Company1();

        mockMvc.perform(
                MockMvcRequestBuilders.delete(V1_DIAL_PLANS_PATH + "/{id}", NON_EXISTING_DIAL_PLAN_ID)
                        .header(SecurityConstants.AUTH_HEADER_NAME, authToken)
        ).andExpect(
                status().isNotFound()
        ).andExpect(
                jsonPath("$.reason").value(startsWith("Could not find dial plan with id " +
                        NON_EXISTING_DIAL_PLAN_ID))
        );
    }

    @Test
    public void updateDialPlanWithNullCompany() throws Exception {
        databaseFixtureBuilder1
                .setCompany(1)
                .addAdministrator(1)
                .build();
        databaseFixtureBuilder2
                .setCompany(2)
                .addAdministrator(2)
                .build();
        String authToken = getTokenForAdministrator1Company1();

        DialPlanDto dialPlanDto = DialPlanDtoGenerator.createTestDialPlanDto((CompanyDto) null, 1);
        mockMvc.perform(
                MockMvcRequestBuilders.put(V1_DIAL_PLANS_PATH + "/{id}", NON_EXISTING_DIAL_PLAN_ID)
                        .content(Json.toJson(dialPlanDto))
                        .header(SecurityConstants.AUTH_HEADER_NAME, authToken)
        ).andExpect(
                status().isBadRequest()
        );
    }

    @Test
    public void updateNonExistingDialPlan() throws Exception {
        databaseFixtureBuilder1
                .setCompany(1)
                .addAdministrator(1)
                .build();
        databaseFixtureBuilder2
                .setCompany(2)
                .addAdministrator(2)
                .build();
        String authToken = getTokenForAdministrator1Company1();

        DialPlanDto dialPlanDto = DialPlanDtoGenerator.createTestDialPlanDto(databaseFixtureBuilder1.getFirstCompany
                (), 1);
        mockMvc.perform(
                MockMvcRequestBuilders.put(V1_DIAL_PLANS_PATH + "/{id}", NON_EXISTING_DIAL_PLAN_ID)
                        .content(Json.toJson(dialPlanDto))
                        .header(SecurityConstants.AUTH_HEADER_NAME, authToken)
        ).andExpect(
                status().isNotFound()
        );
    }


    @Test
    public void updateDialPlan() throws Exception {
        databaseFixtureBuilder1
                .setCompany(1)
                .addAdministrator(1)
                .addSipClient(1)
                .addDialPlan(1)
                .addDial(1, 1, 1, new int[]{1})
                .addSayAlpha(2, 2, 1)
                .build();
        databaseFixtureBuilder2
                .setCompany(2)
                .addAdministrator(2)
                .addSipClient(1)
                .addDialPlan(1)
                .addDial(1, 1, 1, new int[]{1})
                .addSayAlpha(2, 2, 1)
                .build();

        String authToken = getTokenForAdministrator1Company1();

        DialPlanDto existingDialPlan = DialPlanServiceImpl.dialPlanEntityToDialPlanDtoIgnoreActions(
                databaseFixtureBuilder1.getDialPlanList().get(1)
        );

        DialPlanDto updatedDialPlan = DialPlanDtoGenerator.createTestDialPlanDto(existingDialPlan.getCompany(), 2);
        updatedDialPlan.setId(existingDialPlan.getId());

        // create some new actions
        List<ActionDto> actionDtos = createActions(databaseFixtureBuilder1);

        // add the actions to the updated dial plan
        updatedDialPlan.setActions(actionDtos);

        String putResult = mockMvc.perform(
                MockMvcRequestBuilders.put(V1_DIAL_PLANS_PATH + "/{id}", existingDialPlan.getId())
                        .content(Json.toJson(updatedDialPlan))
                        .header(SecurityConstants.AUTH_HEADER_NAME, authToken)
        ).andExpect(
                status().isOk()
        ).andReturn().getResponse().getContentAsString();

        DialPlanDto actualDialPlan = Json.toObjectTypeSafe(putResult, DialPlanDto.class);
        assertThat(actualDialPlan, dialPlanDtoEqualTo(updatedDialPlan));

        String response = mockMvc.perform(
                MockMvcRequestBuilders.get(V1_DIAL_PLANS_PATH + "/{id}", existingDialPlan.getId())
                        .header(SecurityConstants.AUTH_HEADER_NAME, authToken)
        ).andExpect(
                status().isOk()
        ).andReturn().getResponse().getContentAsString();

        actualDialPlan = Json.toObjectTypeSafe(response, DialPlanDto.class);

        assertThat(actualDialPlan, dialPlanDtoEqualTo(updatedDialPlan));

        assertActions(actionDtos, actualDialPlan.getActions());

    }

    @Test
    public void updateForbiddenDialPlan() throws Exception {
        databaseFixtureBuilder1
                .setCompany(1)
                .addAdministrator(1)
                .addSipClient(1)
                .addDialPlan(1)
                .addDial(1, 1, 1, new int[]{1})
                .addSayAlpha(2, 2, 1)
                .build();
        databaseFixtureBuilder2
                .setCompany(2)
                .addAdministrator(2)
                .addSipClient(1)
                .addDialPlan(1)
                .addDial(1, 1, 1, new int[]{1})
                .addSayAlpha(2, 2, 1)
                .build();

        String authToken = getTokenForAdministrator1Company1();

        DialPlanDto existingDialPlan = DialPlanServiceImpl.dialPlanEntityToDialPlanDtoIgnoreActions(
                databaseFixtureBuilder2.getDialPlanList().get(1)
        );

        DialPlanDto updatedDialPlan = DialPlanDtoGenerator.createTestDialPlanDto(existingDialPlan.getCompany(), 2);
        updatedDialPlan.setId(existingDialPlan.getId());

        // create some new actions
        List<ActionDto> actionDtos = createActions(databaseFixtureBuilder1);

        // add the actions to the updated dial plan
        updatedDialPlan.setActions(actionDtos);

        mockMvc.perform(
                MockMvcRequestBuilders.put(V1_DIAL_PLANS_PATH + "/{id}", existingDialPlan.getId())
                        .content(Json.toJson(updatedDialPlan))
                        .header(SecurityConstants.AUTH_HEADER_NAME, authToken)
        ).andExpect(
                status().isForbidden()
        );
    }

    @Test
    public void createInvalidDialPlan() throws Exception {
        databaseFixtureBuilder1
                .setCompany(1)
                .addAdministrator(1)
                .build();
        String authToken = getTokenForAdministrator1Company1();

        DialPlanDto dialPlanDto = new DialPlanDto();
        mockMvc.perform(
                MockMvcRequestBuilders.post(V1_DIAL_PLANS_PATH)
                        .content(Json.toJson(dialPlanDto))
                        .header(SecurityConstants.AUTH_HEADER_NAME, authToken)
        ).andExpect(
                status().isBadRequest()
        );
    }

    @Test
    public void createDialPlan() throws Exception {
        databaseFixtureBuilder1
                .setCompany(1)
                .addAdministrator(1)
                .build();

        databaseFixtureBuilder2
                .setCompany(2)
                .addAdministrator(2)
                .addSipClient(1)
                .addSipClient(2)
                .build();
        String authToken = getTokenForAdministrator2Company2();

        CompanyDto companyDto = CompanyServiceImpl.companyEntityToCompanyDto(
                databaseFixtureBuilder2.getFirstCompany()
        );

        DialPlanDto testDialPlanDto = DialPlanDtoGenerator.createTestDialPlanDto(companyDto, 3);

        List<SipClient> sipClientList = new ArrayList<>(databaseFixtureBuilder2.getSipClientList().values());


        List<ActionDto> actionDtos = createActions(databaseFixtureBuilder2);

        testDialPlanDto.setActions(actionDtos);

        String creationResponse = mockMvc.perform(
                MockMvcRequestBuilders.post(V1_DIAL_PLANS_PATH)
                        .content(Json.toJson(testDialPlanDto))
                        .header(SecurityConstants.AUTH_HEADER_NAME, authToken)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(CoreMatchers.not(
                        CoreMatchers.equalTo(testDialPlanDto.getId())))
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(CoreMatchers.equalTo(testDialPlanDto.getName()))
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.phone").value(CoreMatchers.equalTo(testDialPlanDto.getPhone()))
        ).andReturn().getResponse().getContentAsString();

        DialPlanDto createdDialPlanDto = Json.toObjectTypeSafe(creationResponse, DialPlanDto.class);

        String response = mockMvc.perform(
                MockMvcRequestBuilders.get(V1_DIAL_PLANS_PATH + "/{id}", createdDialPlanDto.getId())
                        .header(SecurityConstants.AUTH_HEADER_NAME, authToken)
        ).andExpect(
                status().isOk()
        ).andReturn().getResponse().getContentAsString();

        // assert DialPlan
        DialPlanDto actualDialPlan = Json.toObjectTypeSafe(response, DialPlanDto.class);

        assertThat(createdDialPlanDto, dialPlanDtoEqualTo(actualDialPlan));

        assertActions(actionDtos, actualDialPlan.getActions());
    }

    @Test
    public void createDialPlanInForbiddenCompany() throws Exception {
        databaseFixtureBuilder1
                .setCompany(1)
                .addAdministrator(1)
                .build();

        databaseFixtureBuilder2
                .setCompany(2)
                .addAdministrator(2)
                .addSipClient(1)
                .addSipClient(2)
                .build();
        String authToken = getTokenForAdministrator1Company1();

        CompanyDto companyDto = CompanyServiceImpl.companyEntityToCompanyDto(
                databaseFixtureBuilder2.getFirstCompany()
        );

        DialPlanDto testDialPlanDto = DialPlanDtoGenerator.createTestDialPlanDto(companyDto, 3);

        List<SipClient> sipClientList = new ArrayList<>(databaseFixtureBuilder2.getSipClientList().values());


        List<ActionDto> actionDtos = createActions(databaseFixtureBuilder2);

        testDialPlanDto.setActions(actionDtos);

        mockMvc.perform(
                MockMvcRequestBuilders.post(V1_DIAL_PLANS_PATH)
                        .content(Json.toJson(testDialPlanDto))
                        .header(SecurityConstants.AUTH_HEADER_NAME, authToken)
        ).andExpect(
                status().isForbidden()
        );
    }

    @Test
    public void deleteDialPlanWithNoActions() throws Exception {
        databaseFixtureBuilder1
                .setCompany(1)
                .addAdministrator(1)
                .addSipClient(1)
                .addDialPlan(1)
                .build();
        databaseFixtureBuilder2
                .setCompany(2)
                .addAdministrator(2)
                .build();

        String authToken = getTokenForAdministrator1Company1();

        DialPlanDto createdDialPlan = DialPlanServiceImpl.dialPlanEntityToDialPlanDtoIgnoreActions(
                databaseFixtureBuilder1.getDialPlanList().get(1)
        );

        mockMvc.perform(
                MockMvcRequestBuilders.delete(V1_DIAL_PLANS_PATH + "/{id}", createdDialPlan.getId())
                        .header(SecurityConstants.AUTH_HEADER_NAME, authToken)
        ).andExpect(
                status().isNoContent()
        );

        mockMvc.perform(
                MockMvcRequestBuilders.get(V1_DIAL_PLANS_PATH + "/{id}", createdDialPlan.getId())
                        .header(SecurityConstants.AUTH_HEADER_NAME, authToken)
        ).andExpect(
                status().isNotFound()
        );
    }

    @Test
    public void deleteDialPlanWithNoActionsInForbiddenCompany() throws Exception {
        databaseFixtureBuilder1
                .setCompany(1)
                .addAdministrator(1)
                .addSipClient(1)
                .addDialPlan(1)
                .build();
        databaseFixtureBuilder2
                .setCompany(2)
                .addAdministrator(2)
                .build();

        String authToken = getTokenForAdministrator2Company2();

        DialPlanDto createdDialPlan = DialPlanServiceImpl.dialPlanEntityToDialPlanDtoIgnoreActions(
                databaseFixtureBuilder1.getDialPlanList().get(1)
        );

        mockMvc.perform(
                MockMvcRequestBuilders.delete(V1_DIAL_PLANS_PATH + "/{id}", createdDialPlan.getId())
                        .header(SecurityConstants.AUTH_HEADER_NAME, authToken)
        ).andExpect(
                status().isForbidden()
        );
    }

    @Test
    public void deleteDialPlanWithActions() throws Exception {
        int dialPriority = 1;
        int sayAlphaPriority = 2;
        databaseFixtureBuilder1
                .setCompany(1)
                .addAdministrator(1)
                .addSipClient(1)
                .addDialPlan(1)
                .addDial(1, dialPriority, 1, new int[]{1})
                .addSayAlpha(1, sayAlphaPriority, 1)
                .build();
        databaseFixtureBuilder2
                .setCompany(2)
                .addAdministrator(2)
                .build();

        String authToken = getTokenForAdministrator1Company1();

        DialPlanDto createdDialPlan = DialPlanServiceImpl.dialPlanEntityToDialPlanDtoIgnoreActions(
                databaseFixtureBuilder1.getDialPlanList().get(1)
        );
        Long dialPlanId = databaseFixtureBuilder1.getDialPlanList().get(1).getId();
        assertThat(dialPlanId, is(not(equalTo(0))));

        Long dialId = databaseFixtureBuilder1.getDialList().get(1).getId();
        assertThat(dialId, is(not(equalTo(0))));

        Long sayAlphaId = databaseFixtureBuilder1.getSayAlphaList().get(1).getId();
        assertThat(sayAlphaId, is(not(equalTo(0))));

        mockMvc.perform(
                MockMvcRequestBuilders.delete(V1_DIAL_PLANS_PATH + "/{id}", createdDialPlan.getId())
                        .header(SecurityConstants.AUTH_HEADER_NAME, authToken)
        ).andExpect(
                status().isNoContent()
        );

        mockMvc.perform(
                MockMvcRequestBuilders.get(V1_DIAL_PLANS_PATH + "/{id}", createdDialPlan.getId())
                        .header(SecurityConstants.AUTH_HEADER_NAME, authToken)
        ).andExpect(
                status().isNotFound()
        );

        Dial dial = databaseFixtureBuilder1.getDialRepository().findOne(dialId);
        assertNull(dial);

        SayAlpha sayAlpha = databaseFixtureBuilder1.getSayAlphaRepository().findOne(sayAlphaId);
        assertNull(sayAlpha);
    }

    private List<ActionDto> createActions(DatabaseFixtureBuilder dbBuilder) {
        List<SipClient> sipClientList = new ArrayList<>(dbBuilder.getSipClientList().values());
        DialActionDto expectedDialActionDto = DialActionDtoGenerator.createTestDialActionDtoFormSipClientEntities(11,
                sipClientList);

        SayAlphaActionDto expectedSayAlphaActionDto = SayAlphaActionDtoGenerator.createTestDialActionDto(21);

        ObjectMapper objectMapper = new ObjectMapper();

        ActionDto expectedActionDtoDial = ActionDtoGenerator.createTestActionDto(11, "Dial",
                objectMapper.convertValue(expectedDialActionDto, Map.class));
        ActionDto expectedActionDtoSayAlpha = ActionDtoGenerator.createTestActionDto(21, "SayAlpha",
                objectMapper.convertValue(expectedSayAlphaActionDto, Map.class));

        List<ActionDto> actionDtos = new ArrayList<>();
        actionDtos.add(expectedActionDtoDial);
        actionDtos.add(expectedActionDtoSayAlpha);

        return actionDtos;
    }

    private void assertActions(List<ActionDto> expectedActionDtos, List<ActionDto> actualActionDtoList) {
        ObjectMapper objectMapper = new ObjectMapper();

        // assert ActionDtos
        // -----------------

        // expected ActionsDtos
        ActionDto expectedActionDtoDial = expectedActionDtos.get(0);
        ActionDto expectedActionDtoSayAlpha = expectedActionDtos.get(1);

        // actual ActionDtos
        ActionDto actualActionDtoDial = actualActionDtoList.get(0);
        ActionDto actualActionDtoSayAlpha = actualActionDtoList.get(1);

        assertThat(expectedActionDtoDial, actionDtoAlmostEqualTo(actualActionDtoDial));
        assertThat(expectedActionDtoSayAlpha, actionDtoAlmostEqualTo(actualActionDtoSayAlpha));

        // assert actions
        // --------------

        // expected actions
        DialActionDto expectedDialActionDto = objectMapper.convertValue(
                expectedActionDtoDial.getTypeSpecific(), DialActionDto.class);
        SayAlphaActionDto expectedSayAlphaActionDto = objectMapper.convertValue(
                expectedActionDtoSayAlpha.getTypeSpecific(), SayAlphaActionDto.class);

        // actual actions
        DialActionDto actualDialActionDto = objectMapper.convertValue(
                actualActionDtoDial.getTypeSpecific(), DialActionDto.class);
        SayAlphaActionDto actualSayAlphaActionDto = objectMapper.convertValue(
                actualActionDtoSayAlpha.getTypeSpecific(), SayAlphaActionDto.class);

        assertThat(expectedDialActionDto, dialActionEqualTo(actualDialActionDto));
        assertThat(expectedSayAlphaActionDto, sayAlphaActionEqualTo(actualSayAlphaActionDto));
    }

    private String getTokenForAdministrator1Company1() {
        return tokenHandler.createTokenForUser(new AdminDetails(databaseFixtureBuilder1.getAdminList()
                .get(1)));
    }

    private String getTokenForAdministrator2Company2() {
        return tokenHandler.createTokenForUser(new AdminDetails(databaseFixtureBuilder2.getAdminList()
                .get(2)));
    }

}