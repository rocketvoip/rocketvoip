package ch.zhaw.psit4.web;

import ch.zhaw.psit4.data.jpa.entities.Dial;
import ch.zhaw.psit4.data.jpa.entities.SayAlpha;
import ch.zhaw.psit4.data.jpa.entities.SipClient;
import ch.zhaw.psit4.dto.ActionDto;
import ch.zhaw.psit4.dto.CompanyDto;
import ch.zhaw.psit4.dto.DialPlanDto;
import ch.zhaw.psit4.dto.actions.DialAction;
import ch.zhaw.psit4.dto.actions.SayAlphaAction;
import ch.zhaw.psit4.services.implementation.CompanyServiceImpl;
import ch.zhaw.psit4.services.implementation.DialPlanServiceImpl;
import ch.zhaw.psit4.testsupport.convenience.Json;
import ch.zhaw.psit4.testsupport.fixtures.database.BeanConfiguration;
import ch.zhaw.psit4.testsupport.fixtures.database.DatabaseFixtureBuilder;
import ch.zhaw.psit4.testsupport.fixtures.dto.ActionDtoGenerator;
import ch.zhaw.psit4.testsupport.fixtures.dto.DialActionGenerator;
import ch.zhaw.psit4.testsupport.fixtures.dto.DialPlanDtoGenerator;
import ch.zhaw.psit4.testsupport.fixtures.dto.SayAlphaActionGenerator;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Jona Braun
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@Import(BeanConfiguration.class)
public class DialPlanControllerIT {
    private static final int NON_EXISTING_DIAL_PLAN_ID = 100;
    private static final String V1_DIAL_PLANS_PATH = "/v1/dialplans";

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
    public void getAllDialPlansEmpty() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(V1_DIAL_PLANS_PATH)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isOk()
        ).andExpect(
                jsonPath("$.length()").value(equalTo(0))
        );
    }

    @Test
    public void getNonExistingDialPlan() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(V1_DIAL_PLANS_PATH + "/{id}", NON_EXISTING_DIAL_PLAN_ID)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isNotFound()
        ).andExpect(
                jsonPath("$.reason").value(equalTo("Could not find dial plan with id " + NON_EXISTING_DIAL_PLAN_ID))
        );
    }

    @Test
    public void deleteNonExistingDialPlan() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete(V1_DIAL_PLANS_PATH + "/{id}", NON_EXISTING_DIAL_PLAN_ID)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isNotFound()
        ).andExpect(
                jsonPath("$.reason").value(startsWith("Could not delete dial plan with id " +
                        NON_EXISTING_DIAL_PLAN_ID))
        );
    }

    @Test
    public void updateNonExistingDialPlan() throws Exception {
        DialPlanDto dialPlanDto = DialPlanDtoGenerator.createTestDialPlanDto((CompanyDto) null, 1);
        mockMvc.perform(
                MockMvcRequestBuilders.put(V1_DIAL_PLANS_PATH + "/{id}", NON_EXISTING_DIAL_PLAN_ID)
                        .content(Json.toJson(dialPlanDto))
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isBadRequest()
        );
    }

    @Test
    public void createInvalidDialPlan() throws Exception {
        DialPlanDto dialPlanDto = new DialPlanDto();
        mockMvc.perform(
                MockMvcRequestBuilders.post(V1_DIAL_PLANS_PATH)
                        .content(Json.toJson(dialPlanDto))
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isBadRequest()
        );
    }

    @Test
    public void createDialPlan() throws Exception {
        databaseFixtureBuilder2.company(2).addSipClient(1).addSipClient(2).build();
        CompanyDto companyDto = CompanyServiceImpl.companyEntityToCompanyDto(
                databaseFixtureBuilder2.getCompany()
        );

        DialPlanDto testDialPlanDto = DialPlanDtoGenerator.createTestDialPlanDto(companyDto, 3);

        List<SipClient> sipClientList = new ArrayList<>(databaseFixtureBuilder2.getSipClientList().values());


        List<ActionDto> actionDtos = createActions(databaseFixtureBuilder2);

        testDialPlanDto.setActions(actionDtos);

        String creationResponse = mockMvc.perform(
                MockMvcRequestBuilders.post(V1_DIAL_PLANS_PATH)
                        .content(Json.toJson(testDialPlanDto))
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
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
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isOk()
        ).andReturn().getResponse().getContentAsString();

        // assert DialPlan
        DialPlanDto actualDialPlan = Json.toObjectTypeSafe(response, DialPlanDto.class);

        assertThat(createdDialPlanDto, dialPlanDtoEqualTo(actualDialPlan));

        assertActions(actionDtos, actualDialPlan.getActions());


    }

    @Test
    public void deleteDialPlanWithNoActions() throws Exception {
        databaseFixtureBuilder1.company(1).addSipClient(1).addDialPlan(1).build();
        DialPlanDto createdDialPlan = DialPlanServiceImpl.dialPlanEntityToDialPlanDtoIgnoreActions(
                databaseFixtureBuilder1.getDialPlanList().get(1)
        );

        mockMvc.perform(
                MockMvcRequestBuilders.delete(V1_DIAL_PLANS_PATH + "/{id}", createdDialPlan.getId())
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isNoContent()
        );

        mockMvc.perform(
                MockMvcRequestBuilders.get(V1_DIAL_PLANS_PATH + "/{id}", createdDialPlan.getId())
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isNotFound()
        );
    }

    @Test
    public void deleteDialPlanWithActions() throws Exception {
        int dialPriority = 1;
        int sayAlphaPriority = 2;
        databaseFixtureBuilder1
                .company(1)
                .addSipClient(1)
                .addDialPlan(1)
                .addDial(1, dialPriority, 1, new int[]{1})
                .addSayAlpha(1, sayAlphaPriority, 1)
                .build();

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
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isNoContent()
        );

        mockMvc.perform(
                MockMvcRequestBuilders.get(V1_DIAL_PLANS_PATH + "/{id}", createdDialPlan.getId())
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isNotFound()
        );

        Dial dial = databaseFixtureBuilder1.getDialRepository().findOne(dialId);
        assertNull(dial);

        SayAlpha sayAlpha = databaseFixtureBuilder1.getSayAlphaRepository().findOne(sayAlphaId);
        assertNull(sayAlpha);
    }

    @Test
    public void updateDialPlan() throws Exception {
        databaseFixtureBuilder1
                .company(1)
                .addSipClient(1)
                .addDialPlan(1)
                .addDial(1, 1, 1, new int[]{1})
                .addSayAlpha(2, 2, 1)
                .build();

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
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(Json.toJson(updatedDialPlan))
        ).andExpect(
                status().isOk()
        ).andReturn().getResponse().getContentAsString();

        DialPlanDto actualDialPlan = Json.toObjectTypeSafe(putResult, DialPlanDto.class);
        assertThat(actualDialPlan, dialPlanDtoEqualTo(updatedDialPlan));

        String response = mockMvc.perform(
                MockMvcRequestBuilders.get(V1_DIAL_PLANS_PATH + "/{id}", existingDialPlan.getId())
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isOk()
        ).andReturn().getResponse().getContentAsString();

        actualDialPlan = Json.toObjectTypeSafe(response, DialPlanDto.class);

        assertThat(actualDialPlan, dialPlanDtoEqualTo(updatedDialPlan));

        assertActions(actionDtos, actualDialPlan.getActions());

    }

    private List<ActionDto> createActions(DatabaseFixtureBuilder dbBuilder) {
        List<SipClient> sipClientList = new ArrayList<>(dbBuilder.getSipClientList().values());
        DialAction expectedDialAction = DialActionGenerator.createTestDialActionDtoFormSipClientEntities(11,
                sipClientList);

        SayAlphaAction expectedSayAlphaAction = SayAlphaActionGenerator.createTestDialActionDto(21);

        ObjectMapper objectMapper = new ObjectMapper();

        ActionDto expectedActionDtoDial = ActionDtoGenerator.createTestActionDto(11, "Dial",
                objectMapper.convertValue(expectedDialAction, Map.class));
        ActionDto expectedActionDtoSayAlpha = ActionDtoGenerator.createTestActionDto(21, "SayAlpha",
                objectMapper.convertValue(expectedSayAlphaAction, Map.class));

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
        DialAction expectedDialAction = objectMapper.convertValue(
                expectedActionDtoDial.getTypeSpecific(), DialAction.class);
        SayAlphaAction expectedSayAlphaAction = objectMapper.convertValue(
                expectedActionDtoSayAlpha.getTypeSpecific(), SayAlphaAction.class);

        // actual actions
        DialAction actualDialAction = objectMapper.convertValue(
                actualActionDtoDial.getTypeSpecific(), DialAction.class);
        SayAlphaAction actualSayAlphaAction = objectMapper.convertValue(
                actualActionDtoSayAlpha.getTypeSpecific(), SayAlphaAction.class);

        assertThat(expectedDialAction, dialActionEqualTo(actualDialAction));
        assertThat(expectedSayAlphaAction, sayAlphaActionEqualTo(actualSayAlphaAction));
    }

}