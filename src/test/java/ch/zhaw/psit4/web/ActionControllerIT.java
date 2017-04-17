package ch.zhaw.psit4.web;

import ch.zhaw.psit4.dto.ActionDto;
import ch.zhaw.psit4.dto.CompanyDto;
import ch.zhaw.psit4.dto.SipClientDto;
import ch.zhaw.psit4.dto.actions.TeamAction;
import ch.zhaw.psit4.services.implementation.CompanyServiceImpl;
import ch.zhaw.psit4.testsupport.convenience.Json;
import ch.zhaw.psit4.testsupport.fixtures.database.BeanConfiguration;
import ch.zhaw.psit4.testsupport.fixtures.database.DatabaseFixtureBuilder;
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

import java.util.ArrayList;
import java.util.List;

import static ch.zhaw.psit4.services.implementation.SipClientServiceImpl.sipClientEntityToSipClientDto;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Jona Braun
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@Import(BeanConfiguration.class)
public class ActionControllerIT {

    private static final String V1_ACTIONS_PATH = "/v1/action";

    @Autowired
    private WebApplicationContext wac;

    private DatabaseFixtureBuilder databaseFixtureBuilder1;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        databaseFixtureBuilder1 = wac.getBean(DatabaseFixtureBuilder.class);

    }

    @Test
    public void getAction() throws Exception {
    }


    @Test
    public void updateAction() throws Exception {
        // action
        ActionDto actionDto = new ActionDto();
        actionDto.setName("may fancy action");
        actionDto.setType(ActionDto.ActionType.TEAM);

        // database setup
        databaseFixtureBuilder1.company(1).addSipClient(1).addSipClient(2).build();
        CompanyDto companyDto = CompanyServiceImpl.companyEntityToCompanyDto(
                databaseFixtureBuilder1.getCompany()
        );
        actionDto.setCompany(companyDto);

        // team
        TeamAction teamAction = new TeamAction();
        teamAction.setTime("30");
        List<SipClientDto> sipClientDtoList = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            sipClientDtoList.add(sipClientEntityToSipClientDto(databaseFixtureBuilder1.getSipClientList().get(i)));
        }
        teamAction.setTeam(sipClientDtoList);

        actionDto.setTypeSpecific(teamAction);

        // request
        String actionJson = Json.toJson(actionDto);

        String response = mockMvc.perform(
                MockMvcRequestBuilders.post(V1_ACTIONS_PATH)
                        .content(actionJson)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isCreated()
        ).andExpect(
                jsonPath("$.id").value(not(equalTo(actionDto.getId())))
        ).andExpect(
                jsonPath("$.name").value(equalTo(actionDto.getName()))
        ).andReturn().getResponse().getContentAsString();

        // {"id":0,
        // "name":"may fancy action",
        // "company":{"name":"ACME1","id":1},
        // "type":"TEAM",
        // "typeSpecific":{
        // "time":"30",
        // "team":
        // [
        // {"name":"SipClientLabel1","phone":"0000000001","secret":"SipClientSecret1","id":1,"company":{"name":"ACME1","id":1}},
        // {"name":"SipClientLabel2","phone":"0000000002","secret":"SipClientSecret2","id":2,"company":{"name":"ACME1","id":1}}
        // ]
        // }}
        assertEquals(actionJson, response);

    }
}