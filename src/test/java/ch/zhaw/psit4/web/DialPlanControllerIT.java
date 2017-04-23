package ch.zhaw.psit4.web;

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
        DialAction dialAction = DialActionGenerator.createTestDialActionDtoFormSipClientEntities(1, sipClientList);

        SayAlphaAction sayAlphaAction = SayAlphaActionGenerator.createTestDialActionDto(2);

        ObjectMapper objectMapper = new ObjectMapper();

        ActionDto actionDtoDial = ActionDtoGenerator.createTestActionDto(1, "Dial",
                objectMapper.convertValue(dialAction, Map.class));
        ActionDto actionDtoSayAlpha = ActionDtoGenerator.createTestActionDto(2, "SayAlpha",
                objectMapper.convertValue(sayAlphaAction, Map.class));

        List<ActionDto> actionDtos = new ArrayList<>();
        actionDtos.add(actionDtoDial);
        actionDtos.add(actionDtoSayAlpha);

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
        DialPlanDto actual = Json.toObjectTypeSafe(response, DialPlanDto.class);

        assertThat(createdDialPlanDto, dialPlanDtoEqualTo(actual));

        // assert ActionDtos
        List<ActionDto> actualActionDtoList = actual.getActions();

        // check priority, first one should be dial
        ActionDto actualActionDtoDial = actualActionDtoList.get(0);
        ActionDto actualActionDtoSayAlpha = actualActionDtoList.get(1);

        assertThat(actionDtoDial, actionDtoAlmostEqualTo(actualActionDtoDial));
        assertThat(actionDtoSayAlpha, actionDtoAlmostEqualTo(actualActionDtoSayAlpha));

        // assert Actions
        DialAction actualDialAction = objectMapper.convertValue(
                actualActionDtoDial.getTypeSpecific(), DialAction.class);
        SayAlphaAction actualSayAlphaAction = objectMapper.convertValue(
                actualActionDtoSayAlpha.getTypeSpecific(), SayAlphaAction.class);

        assertThat(dialAction, dialActionEqualTo(actualDialAction));
        assertThat(sayAlphaAction, sayAlphaActionEqualTo(actualSayAlphaAction));

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
    public void updateDialPlan() throws Exception {
    }

}