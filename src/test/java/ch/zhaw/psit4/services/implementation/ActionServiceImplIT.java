package ch.zhaw.psit4.services.implementation;

import ch.zhaw.psit4.data.jpa.repositories.DialPlanRepository;
import ch.zhaw.psit4.data.jpa.repositories.SayAlphaRepository;
import ch.zhaw.psit4.dto.ActionDto;
import ch.zhaw.psit4.dto.DialPlanDto;
import ch.zhaw.psit4.dto.SipClientDto;
import ch.zhaw.psit4.dto.actions.DialAction;
import ch.zhaw.psit4.dto.actions.SayAlphaAction;
import ch.zhaw.psit4.services.interfaces.ActionServiceInterface;
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
import static ch.zhaw.psit4.testsupport.matchers.ActionDtoPartialMatcher.actionDtoAlmostEqualTo;
import static ch.zhaw.psit4.testsupport.matchers.DialActionEqualTo.dialActionEqualTo;
import static ch.zhaw.psit4.testsupport.matchers.SayAlphaActionEqualTo.sayAlphaActionEqualTo;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author Jona Braun
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Import(BeanConfiguration.class)
public class ActionServiceImplIT {
    @Autowired
    SayAlphaRepository sayAlphaRepository;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private ActionServiceInterface actionServiceInterface;
    @Autowired
    private DialPlanServiceImpl dialPlanServiceImpl;
    @Autowired
    private DialPlanRepository dialPlanRepository;
    private DatabaseFixtureBuilder databaseFixtureBuilder1;

    @Before
    public void setUp() throws Exception {
        databaseFixtureBuilder1 = applicationContext.getBean(DatabaseFixtureBuilder.class);
    }

    @Test
    public void saveDialPlanWithNullActions() throws Exception {
        databaseFixtureBuilder1.company(1).addDialPlan(1).addSipClient(1).addSipClient(2).build();
        DialPlanDto dialPlanDto = getDialPlan(getDialActionDto(1), 1);
        dialPlanDto.setActions(null);
        actionServiceInterface.saveActions(dialPlanDto);

        List<ActionDto> actionDtos = actionServiceInterface.retrieveActions(dialPlanDto.getId());
        assertThat(actionDtos, is(empty()));
    }

    @Test
    public void saveOneDialAction() throws Exception {
        databaseFixtureBuilder1.company(1).addDialPlan(1).addSipClient(1).addSipClient(2).build();
        DialPlanDto dialPlanDto = getDialPlan(getDialActionDto(1), 1);
        actionServiceInterface.saveActions(dialPlanDto);

        List<ActionDto> actionDtos = actionServiceInterface.retrieveActions(dialPlanDto.getId());

        ActionDto expected = dialPlanDto.getActions().get(0);
        ActionDto actual = actionDtos.get(0);
        assertThat(expected, actionDtoAlmostEqualTo(actual));

        ObjectMapper objectMapper = new ObjectMapper();
        DialAction expectedDialAction = objectMapper.convertValue(expected.getTypeSpecific(), DialAction.class);
        DialAction actualDialAction = objectMapper.convertValue(actual.getTypeSpecific(), DialAction.class);
        assertThat(expectedDialAction, dialActionEqualTo(actualDialAction));
    }

    @Test
    public void saveDialAndSayAlphaActions() throws Exception {
        databaseFixtureBuilder1.company(1).addDialPlan(1).addSipClient(1).addSipClient(2).build();
        DialPlanDto dialPlanDto = getDialPlan(getDialActionDtos(2), 1);

        actionServiceInterface.saveActions(dialPlanDto);

        List<ActionDto> actionDtos = actionServiceInterface.retrieveActions(dialPlanDto.getId());
        ActionDto expected = dialPlanDto.getActions().get(0);
        ActionDto actual = actionDtos.get(0);
        assertThat(expected, actionDtoAlmostEqualTo(actual));

        ActionDto expected1 = dialPlanDto.getActions().get(1);
        ActionDto actual1 = actionDtos.get(1);
        assertThat(expected1, actionDtoAlmostEqualTo(actual1));

        ObjectMapper objectMapper = new ObjectMapper();
        DialAction expectedDialAction = objectMapper.convertValue(expected.getTypeSpecific(), DialAction.class);
        DialAction actualDialAction = objectMapper.convertValue(actual.getTypeSpecific(), DialAction.class);
        assertThat(expectedDialAction, dialActionEqualTo(actualDialAction));

        DialAction expectedDialAction1 = objectMapper.convertValue(expected1.getTypeSpecific(), DialAction.class);
        DialAction actualDialAction1 = objectMapper.convertValue(actual1.getTypeSpecific(), DialAction.class);
        assertThat(expectedDialAction1, dialActionEqualTo(actualDialAction1));
    }

    @Test
    public void updateActions() throws Exception {
        databaseFixtureBuilder1.company(1).addDialPlan(1).addSipClient(1).addSipClient(2).build();
        DialPlanDto dialPlanDto = getDialPlan(getDialActionDto(1), 1);
        actionServiceInterface.saveActions(dialPlanDto);

        List<ActionDto> actionDtos = actionServiceInterface.retrieveActions(dialPlanDto.getId());

        ActionDto expected = dialPlanDto.getActions().get(0);
        ActionDto actual = actionDtos.get(0);
        assertThat(expected, actionDtoAlmostEqualTo(actual));

        // update
        List<ActionDto> actionDtoList = getSayAlphaActionDtos(2);
        dialPlanDto.setActions(actionDtoList);

        actionServiceInterface.updateActions(dialPlanDto);

        actionDtos = actionServiceInterface.retrieveActions(dialPlanDto.getId());

        expected = dialPlanDto.getActions().get(0);
        actual = actionDtos.get(0);
        assertThat(expected, actionDtoAlmostEqualTo(actual));

        ObjectMapper objectMapper = new ObjectMapper();
        SayAlphaAction expectedSayAlphaAction = objectMapper.convertValue(expected.getTypeSpecific(), SayAlphaAction.class);
        SayAlphaAction actualSayAlphaAction = objectMapper.convertValue(actual.getTypeSpecific(), SayAlphaAction.class);
        assertThat(expectedSayAlphaAction, sayAlphaActionEqualTo(actualSayAlphaAction));

        expected = dialPlanDto.getActions().get(1);
        actual = actionDtos.get(1);
        assertThat(expected, actionDtoAlmostEqualTo(actual));

        objectMapper = new ObjectMapper();
        expectedSayAlphaAction = objectMapper.convertValue(expected.getTypeSpecific(), SayAlphaAction.class);
        actualSayAlphaAction = objectMapper.convertValue(actual.getTypeSpecific(), SayAlphaAction.class);
        assertThat(expectedSayAlphaAction, sayAlphaActionEqualTo(actualSayAlphaAction));

    }

    @Test
    public void deleteActions() throws Exception {
        databaseFixtureBuilder1.company(1).addDialPlan(1).addSipClient(1).addSipClient(2).build();
        DialPlanDto dialPlanDto = getDialPlan(getDialActionDto(1), 1);
        actionServiceInterface.saveActions(dialPlanDto);

        List<ActionDto> actionDtos = actionServiceInterface.retrieveActions(dialPlanDto.getId());

        ActionDto expected = dialPlanDto.getActions().get(0);
        ActionDto actual = actionDtos.get(0);
        assertThat(expected, actionDtoAlmostEqualTo(actual));

        actionServiceInterface.deleteActions(dialPlanDto.getId());

        List<ActionDto> actualActionDtos = actionServiceInterface.retrieveActions(dialPlanDto.getId());

        assertThat(actualActionDtos, is(empty()));


    }

    private DialPlanDto getDialPlan(List<ActionDto> actionDtos, int number) {
        List<DialPlanDto> dialPlanDtoList = new ArrayList<>();
        databaseFixtureBuilder1.getDialPlanList().values()
                .forEach(x -> dialPlanDtoList.add(dialPlanServiceImpl.dialPlanEntityToDialPlanDto(x)));

        DialPlanDto dialPlanDto = dialPlanDtoList.get(number - 1);
        dialPlanDto.setActions(actionDtos);
        return dialPlanDto;
    }

    private DialPlanDto getDialPlan(ActionDto actionDto, int number) {
        List<ActionDto> actionDtos = new ArrayList<>();
        actionDtos.add(actionDto);
        return getDialPlan(actionDtos, number);

    }

    private List<ActionDto> getSayAlphaActionDtos(int number) {
        List<ActionDto> actionDtos = new ArrayList<>();
        for (int i = 1; i <= number; i++) {
            actionDtos.add(getSayAlphaActionDto(i));
        }
        return actionDtos;
    }

    private ActionDto getSayAlphaActionDto(int number) {
        ActionDto actionDto = new ActionDto();
        actionDto.setName("may fancy action " + number);
        actionDto.setType("SayAlpha");

        SayAlphaAction sayAlphaAction = new SayAlphaAction();
        sayAlphaAction.setVoiceMessage("Hello, World!");
        sayAlphaAction.setSleepTime(5);

        ObjectMapper objM = new ObjectMapper();
        LinkedHashMap linkedHashMap = objM.convertValue(sayAlphaAction, LinkedHashMap.class);

        actionDto.setTypeSpecific(linkedHashMap);
        return actionDto;
    }

    private List<ActionDto> getDialActionDtos(int number) {
        List<ActionDto> actionDtos = new ArrayList<>();
        for (int i = 1; i <= number; i++) {
            actionDtos.add(getDialActionDto(i));
        }
        return actionDtos;
    }

    private ActionDto getDialActionDto(int number) {
        ActionDto actionDto = new ActionDto();
        actionDto.setName("may fancy action " + number);
        actionDto.setType("Dial");

        DialAction dialAction = new DialAction();
        dialAction.setRingingTime(30);

        List<SipClientDto> sipClientDtoList = new ArrayList<>();
        databaseFixtureBuilder1.getSipClientList().values()
                .forEach(x -> sipClientDtoList.add(sipClientEntityToSipClientDto(x)));

        dialAction.setSipClients(sipClientDtoList);

        ObjectMapper objM = new ObjectMapper();
        LinkedHashMap linkedHashMap = objM.convertValue(dialAction, LinkedHashMap.class);

        actionDto.setTypeSpecific(linkedHashMap);
        return actionDto;
    }

}