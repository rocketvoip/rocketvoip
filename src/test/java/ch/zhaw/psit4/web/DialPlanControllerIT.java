package ch.zhaw.psit4.web;

import ch.zhaw.psit4.dto.CompanyDto;
import ch.zhaw.psit4.dto.DialPlanDto;
import ch.zhaw.psit4.testsupport.convenience.Json;
import ch.zhaw.psit4.testsupport.fixtures.database.BeanConfiguration;
import ch.zhaw.psit4.testsupport.fixtures.database.DatabaseFixtureBuilder;
import ch.zhaw.psit4.testsupport.fixtures.dto.DialPlanDtoGenerator;
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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.startsWith;
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

    }

    @Test
    public void getDialPlan() throws Exception {
    }

    @Test
    public void deleteDialPlan() throws Exception {
    }

    @Test
    public void updateDialPlan() throws Exception {
    }

//    @Test
//    public void createDialPlan() throws Exception {
//        // action
//        ActionDto actionDto = new ActionDto();
//        actionDto.setName("may fancy action");
//        actionDto.setType(ActionDto.ActionType.Dial);
//
//        // database setup
//        databaseFixtureBuilder1.company(1).addSipClient(1).addSipClient(2).build();
//
//        // team
//        DialAction dialAction = new DialAction();
//        dialAction.setRingingTime("30");
//        List<SipClientDto> sipClientDtoList = new ArrayList<>();
//        for (int i = 1; i <= 2; i++) {
//            sipClientDtoList.add(sipClientEntityToSipClientDto(databaseFixtureBuilder1.getSipClientList().get(i)));
//        }
//        dialAction.setSipClients(sipClientDtoList);
//
//        ObjectMapper objM = new ObjectMapper();
//        LinkedHashMap linkedHashMap = objM.convertValue(dialAction, LinkedHashMap.class);
//
//        actionDto.setTypeSpecific(linkedHashMap);
//
//        // Dial Plan
//        DialPlanDto dialPlanDto = new DialPlanDto();
//        CompanyDto companyDto = CompanyServiceImpl.companyEntityToCompanyDto(
//                databaseFixtureBuilder1.getCompany()
//        );
//        dialPlanDto.setCompany(companyDto);
//        dialPlanDto.setName("my fancy dialplan");
//
//        List<ActionDto> actionDtos = new ArrayList<>();
//        actionDtos.add(actionDto);
//        dialPlanDto.setActions(actionDtos);
//
//        // request
//        String dialPlanJson = Json.toJson(dialPlanDto);
//
//        String response = mockMvc.perform(
//                MockMvcRequestBuilders.post(V1_ACTIONS_PATH)
//                        .content(dialPlanJson)
//                        .accept(MediaType.APPLICATION_JSON_UTF8)
//                        .contentType(MediaType.APPLICATION_JSON_UTF8)
//        ).andExpect(
//                status().isCreated()
//        ).andExpect(
//                jsonPath("$.id").value(not(equalTo(dialPlanDto.getId())))
//        ).andExpect(
//                jsonPath("$.name").value(equalTo(dialPlanDto.getName()))
//        ).andReturn().getResponse().getContentAsString();
//
////        {
////            "id":0,
////            "name":"my fancy dialplan",
////            "company":{
////                    "name":"ACME1",
////                    "id":1
////                    },
////            "actions":[
////            {
////                "id":0,
////                "name":"may fancy action",
////                "type":"Dial",
////                "typeSpecific":{
////                        "time":"30",
////                        "team":[
////                     {
////                        "name":"SipClientLabel1",
////                        "phone":"0000000001",
////                        "secret":"SipClientSecret1",
////                        "id":1,
////                        "company":{
////                            "name":"ACME1",
////                            "id":1
////                        }
////                     },
////                     {
////                        "name":"SipClientLabel2",
////                        "phone":"0000000002",
////                        "secret":"SipClientSecret2",
////                        "id":2,
////                        "company":{
////                            "name":"ACME1",
////                            "id":1
////                         }
////                     }
////                   ]
////                }
////            }
////            ],
////            "phone":null
////        }
//        assertEquals(dialPlanJson, response);
//
//    }
}