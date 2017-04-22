package ch.zhaw.psit4.services.implementation;

import ch.zhaw.psit4.dto.ActionDto;
import ch.zhaw.psit4.dto.CompanyDto;
import ch.zhaw.psit4.dto.DialPlanDto;
import ch.zhaw.psit4.dto.SipClientDto;
import ch.zhaw.psit4.dto.actions.DialAction;
import ch.zhaw.psit4.services.interfaces.DialPlanServiceInterface;
import ch.zhaw.psit4.testsupport.fixtures.database.BeanConfiguration;
import ch.zhaw.psit4.testsupport.fixtures.database.DatabaseFixtureBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static ch.zhaw.psit4.services.implementation.SipClientServiceImpl.sipClientEntityToSipClientDto;

/**
 * @author Jona Braun
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Import(BeanConfiguration.class)
public class DialPlanServiceImplIT {
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private DialPlanServiceInterface dialPlanServiceInterface;
    private DatabaseFixtureBuilder databaseFixtureBuilder1;
    private DatabaseFixtureBuilder databaseFixtureBuilder2;

    @Before
    public void setUp() throws Exception {
        databaseFixtureBuilder1 = applicationContext.getBean(DatabaseFixtureBuilder.class);
        databaseFixtureBuilder2 = applicationContext.getBean(DatabaseFixtureBuilder.class);

//        databaseFixtureBuilder1.company(1).addSipClient(1).build();
//        databaseFixtureBuilder2.company(2).addSipClient(2).build();
    }

    @Test
    public void dialPlanEntityToDialPlanDto() throws Exception {
    }

    @Test
    public void dialPlanDtoToDialPlanEntity() throws Exception {
    }

    @Test
    public void getAllDialPlans() throws Exception {
    }

    @Test
    public void createDialPlan() throws Exception {
        DialPlanDto dialPlanDto = dialPlanServiceInterface.createDialPlan(getNewDialPlan());
        String asdf = dialPlanDto.getName();
        asdf.toCharArray();
    }

    @Test
    public void updateDialPlan() throws Exception {
    }

    @Test
    public void getDialPlan() throws Exception {
    }

    @Test
    public void deleteDialPlan() throws Exception {
    }

    private DialPlanDto getNewDialPlan() {
        // action
        ActionDto actionDto = new ActionDto();
        actionDto.setName("may fancy action");
        actionDto.setType(ActionDto.ActionType.Dial);

        // database setup
        databaseFixtureBuilder1.company(1).addSipClient(1).addSipClient(2).build();

        // team
        DialAction dialAction = new DialAction();
        dialAction.setRingingTime("30");
        List<SipClientDto> sipClientDtoList = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            sipClientDtoList.add(sipClientEntityToSipClientDto(databaseFixtureBuilder1.getSipClientList().get(i)));
        }
        dialAction.setSipClients(sipClientDtoList);

        ObjectMapper objM = new ObjectMapper();
        LinkedHashMap linkedHashMap = objM.convertValue(dialAction, LinkedHashMap.class);

        actionDto.setTypeSpecific(linkedHashMap);

        // Dial Plan
        DialPlanDto dialPlanDto = new DialPlanDto();
        CompanyDto companyDto = CompanyServiceImpl.companyEntityToCompanyDto(
                databaseFixtureBuilder1.getCompany()
        );
        dialPlanDto.setCompany(companyDto);
        dialPlanDto.setName("my fancy dialplan");

        List<ActionDto> actionDtos = new ArrayList<>();
        actionDtos.add(actionDto);
        dialPlanDto.setActions(actionDtos);
        dialPlanDto.setPhone("1234");

        return dialPlanDto;
    }
}