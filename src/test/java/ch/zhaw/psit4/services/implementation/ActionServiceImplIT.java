package ch.zhaw.psit4.services.implementation;

import ch.zhaw.psit4.data.jpa.repositories.DialPlanRepository;
import ch.zhaw.psit4.data.jpa.repositories.SayAlphaRepository;
import ch.zhaw.psit4.dto.ActionDto;
import ch.zhaw.psit4.dto.DialPlanDto;
import ch.zhaw.psit4.dto.SipClientDto;
import ch.zhaw.psit4.dto.actions.DialActionDto;
import ch.zhaw.psit4.dto.actions.GotoActionDto;
import ch.zhaw.psit4.dto.actions.SayAlphaActionDto;
import ch.zhaw.psit4.services.interfaces.ActionServiceInterface;
import ch.zhaw.psit4.testsupport.fixtures.database.BeanConfiguration;
import ch.zhaw.psit4.testsupport.fixtures.database.DatabaseFixtureBuilder;
import ch.zhaw.psit4.testsupport.fixtures.dto.ActionDtoGenerator;
import ch.zhaw.psit4.testsupport.fixtures.dto.DialActionDtoGenerator;
import ch.zhaw.psit4.testsupport.fixtures.dto.SayAlphaActionDtoGenerator;
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
import static ch.zhaw.psit4.testsupport.matchers.GotoActionEqualTo.gotoActionEqualTo;
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
        DialActionDto expectedDialActionDto = objectMapper.convertValue(expected.getTypeSpecific(), DialActionDto.class);
        DialActionDto actualDialActionDto = objectMapper.convertValue(actual.getTypeSpecific(), DialActionDto.class);
        assertThat(expectedDialActionDto, dialActionEqualTo(actualDialActionDto));
    }

    @Test
    public void saveMultipleDialActions() throws Exception {
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
        DialActionDto expectedDialActionDto = objectMapper.convertValue(expected.getTypeSpecific(), DialActionDto.class);
        DialActionDto actualDialActionDto = objectMapper.convertValue(actual.getTypeSpecific(), DialActionDto.class);
        assertThat(expectedDialActionDto, dialActionEqualTo(actualDialActionDto));

        DialActionDto expectedDialActionDto1 = objectMapper.convertValue(expected1.getTypeSpecific(), DialActionDto.class);
        DialActionDto actualDialActionDto1 = objectMapper.convertValue(actual1.getTypeSpecific(), DialActionDto.class);
        assertThat(expectedDialActionDto1, dialActionEqualTo(actualDialActionDto1));
    }

    @Test
    public void saveGotoActions() throws Exception {
        databaseFixtureBuilder1.company(1).addDialPlan(1).build();

        DialPlanDto dialPlanDto = getDialPlan(getGotoActionDtos(2), 1);

        actionServiceInterface.saveActions(dialPlanDto);

        List<ActionDto> actionDtos = actionServiceInterface.retrieveActions(dialPlanDto.getId());
        ActionDto expected = dialPlanDto.getActions().get(0);
        ActionDto actual = actionDtos.get(0);
        assertThat(expected, actionDtoAlmostEqualTo(actual));

        ObjectMapper objectMapper = new ObjectMapper();
        GotoActionDto expectedDialAction = objectMapper.convertValue(expected.getTypeSpecific(), GotoActionDto.class);
        GotoActionDto actualDialAction = objectMapper.convertValue(actual.getTypeSpecific(), GotoActionDto.class);
        assertThat(expectedDialAction, gotoActionEqualTo(actualDialAction));

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
        SayAlphaActionDto expectedSayAlphaActionDto = objectMapper.convertValue(expected.getTypeSpecific(), SayAlphaActionDto.class);
        SayAlphaActionDto actualSayAlphaActionDto = objectMapper.convertValue(actual.getTypeSpecific(), SayAlphaActionDto.class);
        assertThat(expectedSayAlphaActionDto, sayAlphaActionEqualTo(actualSayAlphaActionDto));

        expected = dialPlanDto.getActions().get(1);
        actual = actionDtos.get(1);
        assertThat(expected, actionDtoAlmostEqualTo(actual));

        objectMapper = new ObjectMapper();
        expectedSayAlphaActionDto = objectMapper.convertValue(expected.getTypeSpecific(), SayAlphaActionDto.class);
        actualSayAlphaActionDto = objectMapper.convertValue(actual.getTypeSpecific(), SayAlphaActionDto.class);
        assertThat(expectedSayAlphaActionDto, sayAlphaActionEqualTo(actualSayAlphaActionDto));

    }

    @Test
    public void deleteActions() throws Exception {
        databaseFixtureBuilder1.company(1).addDialPlan(1).addDialPlan(2).addSipClient(1).addSipClient(2).build();
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
        SayAlphaActionDto sayAlphaActionDto = SayAlphaActionDtoGenerator.createTestDialActionDto(number);

        ObjectMapper objM = new ObjectMapper();
        LinkedHashMap linkedHashMap = objM.convertValue(sayAlphaActionDto, LinkedHashMap.class);

        return ActionDtoGenerator.createTestActionDto(number, "SayAlpha", linkedHashMap);
    }

    private List<ActionDto> getGotoActionDtos(int number) {
        List<ActionDto> actionDtos = new ArrayList<>();
        for (int i = 1; i <= number; i++) {
            actionDtos.add(getGotoActionDto(i));
        }
        return actionDtos;
    }

    private ActionDto getGotoActionDto(int number) {
        GotoActionDto gotoActionDto = new GotoActionDto();

        databaseFixtureBuilder1.addDialPlan(number * 10).build();
        gotoActionDto.setNextDialPlanId(databaseFixtureBuilder1.getDialPlanList().get(number * 10).getId());

        ObjectMapper objM = new ObjectMapper();
        LinkedHashMap linkedHashMap = objM.convertValue(gotoActionDto, LinkedHashMap.class);

        return ActionDtoGenerator.createTestActionDto(number, "Goto", linkedHashMap);
    }

    private List<ActionDto> getDialActionDtos(int number) {
        List<ActionDto> actionDtos = new ArrayList<>();
        for (int i = 1; i <= number; i++) {
            actionDtos.add(getDialActionDto(i));
        }
        return actionDtos;
    }

    private ActionDto getDialActionDto(int number) {
        List<SipClientDto> sipClientDtoList = new ArrayList<>();
        databaseFixtureBuilder1.getSipClientList().values()
                .forEach(x -> sipClientDtoList.add(sipClientEntityToSipClientDto(x)));

        DialActionDto dialActionDto = DialActionDtoGenerator.createTestDialActionDto(number, sipClientDtoList);

        ObjectMapper objM = new ObjectMapper();
        LinkedHashMap linkedHashMap = objM.convertValue(dialActionDto, LinkedHashMap.class);

        return ActionDtoGenerator.createTestActionDto(number, "Dial", linkedHashMap);
    }

}