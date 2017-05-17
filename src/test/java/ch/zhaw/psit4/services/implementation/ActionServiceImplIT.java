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

package ch.zhaw.psit4.services.implementation;

import ch.zhaw.psit4.dto.ActionDto;
import ch.zhaw.psit4.dto.DialPlanDto;
import ch.zhaw.psit4.dto.SipClientDto;
import ch.zhaw.psit4.dto.actions.BranchActionDto;
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
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import static ch.zhaw.psit4.services.implementation.SipClientServiceImpl.sipClientEntityToSipClientDto;
import static ch.zhaw.psit4.testsupport.matchers.ActionDtoPartialMatcher.actionDtoAlmostEqualTo;
import static ch.zhaw.psit4.testsupport.matchers.BranchActionEqualTo.branchActionEqualTo;
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
    private ApplicationContext applicationContext;
    @Autowired
    private ActionServiceInterface actionServiceInterface;
    @Autowired
    private DialPlanServiceImpl dialPlanServiceImpl;

    private DatabaseFixtureBuilder databaseFixtureBuilder1;

    @Before
    public void setUp() throws Exception {
        databaseFixtureBuilder1 = applicationContext.getBean(DatabaseFixtureBuilder.class);
    }

    @Test
    public void saveDialPlanWithNullActions() throws Exception {
        databaseFixtureBuilder1.setCompany(1).addDialPlan(1).addSipClient(1).addSipClient(2).build();
        DialPlanDto dialPlanDto = generateDialPlan(generateDialActionDto(1), 1);
        dialPlanDto.setActions(null);
        actionServiceInterface.saveActions(dialPlanDto);

        List<ActionDto> actionDtos = actionServiceInterface.retrieveActions(dialPlanDto.getId());
        assertThat(actionDtos, is(empty()));
    }

    @Test
    public void saveOneDialAction() throws Exception {
        databaseFixtureBuilder1.setCompany(1).addDialPlan(1).addSipClient(1).addSipClient(2).build();
        DialPlanDto dialPlanDto = generateDialPlan(generateDialActionDto(1), 1);
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
        databaseFixtureBuilder1.setCompany(1).addDialPlan(1).addSipClient(1).addSipClient(2).build();
        DialPlanDto dialPlanDto = generateDialPlan(generateDialActionDtos(2), 1);

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
        databaseFixtureBuilder1.setCompany(1).addDialPlan(1).addDialPlan(2).build();

        ActionDto gotoActionDto1 = generateGotoActionDto(1, 2);
        ActionDto gotoActionDto2 = generateGotoActionDto(2, 2);
        List<ActionDto> gotoActionDtos = new ArrayList<>();
        gotoActionDtos.add(gotoActionDto1);
        gotoActionDtos.add(gotoActionDto2);

        DialPlanDto dialPlanDto = generateDialPlan(gotoActionDtos, 1);

        actionServiceInterface.saveActions(dialPlanDto);

        List<ActionDto> actionDtos = actionServiceInterface.retrieveActions(dialPlanDto.getId());
        ActionDto expected = dialPlanDto.getActions().get(0);
        ActionDto actual = actionDtos.get(0);
        assertThat(expected, actionDtoAlmostEqualTo(actual));

        ObjectMapper objectMapper = new ObjectMapper();
        GotoActionDto expectedGotoAction = objectMapper.convertValue(expected.getTypeSpecific(), GotoActionDto.class);
        GotoActionDto actualGotoAction = objectMapper.convertValue(actual.getTypeSpecific(), GotoActionDto.class);
        assertThat(expectedGotoAction, gotoActionEqualTo(actualGotoAction));

        ActionDto expected2 = dialPlanDto.getActions().get(1);
        ActionDto actual2 = actionDtos.get(1);
        assertThat(expected2, actionDtoAlmostEqualTo(actual2));

        GotoActionDto expectedGotoAction2 = objectMapper.convertValue(expected2.getTypeSpecific(), GotoActionDto.class);
        GotoActionDto actualGotoAction2 = objectMapper.convertValue(actual2.getTypeSpecific(), GotoActionDto.class);
        assertThat(expectedGotoAction2, gotoActionEqualTo(actualGotoAction2));
    }

    @Test
    public void saveBranchDto() {
        int dialPlanNumberButton1 = 2;
        int dialPlanNumberButton2 = 3;

        databaseFixtureBuilder1.setCompany(1).addDialPlan(1).addDialPlan(dialPlanNumberButton1).addDialPlan
                (dialPlanNumberButton2).build();

        // normal order: dialPlanNumberButton1, dialPlanNumberButton2
        ActionDto branchActionDto1 = generateBranchActionDto(1, Arrays.asList(dialPlanNumberButton1, dialPlanNumberButton2));
        // reverse order: dialPlanNumberButton2, dialPlanNumberButton1
        ActionDto branchActionDto2 = generateBranchActionDto(1, Arrays.asList(dialPlanNumberButton2, dialPlanNumberButton1));

        List<ActionDto> branchActionDtos = new ArrayList<>();
        branchActionDtos.add(branchActionDto1);
        branchActionDtos.add(branchActionDto2);

        DialPlanDto dialPlanDto = generateDialPlan(branchActionDtos, 1);

        actionServiceInterface.saveActions(dialPlanDto);

        checkGetBranchDto(dialPlanDto, 1);
        checkGetBranchDto(dialPlanDto, 2);

    }

    @Test
    public void updateBranchDto() {
        int normalDialPlanNumber = 1;
        int dialPlanNumberButton1 = 2;
        int dialPlanNumberButton2 = 3;

        int branchDialPlanNumber1 = 1;
        int branchDialPlanNumber2 = 2;

        databaseFixtureBuilder1.setCompany(1).addDialPlan(normalDialPlanNumber)
                .addDialPlan(dialPlanNumberButton1)
                .addDialPlan(dialPlanNumberButton2)
                .addBranchDialPlan(branchDialPlanNumber1, dialPlanNumberButton1)
                .addBranchDialPlan(branchDialPlanNumber2, dialPlanNumberButton2)
                .addBranch(1, 1, normalDialPlanNumber, Arrays.asList(branchDialPlanNumber1, branchDialPlanNumber2))
                .build();

        // change order: dialPlanNumberButton2, dialPlanNumberButton1
        ActionDto branchActionDto1 = generateBranchActionDto(1, Arrays.asList(dialPlanNumberButton2, dialPlanNumberButton1));

        List<ActionDto> branchActionDtos = new ArrayList<>();
        branchActionDtos.add(branchActionDto1);

        DialPlanDto dialPlanDto = generateDialPlan(branchActionDtos, normalDialPlanNumber);

        actionServiceInterface.updateActions(dialPlanDto);

        checkGetBranchDto(dialPlanDto, 1);
    }

    /*
     * Asserts that the given actions in the dialPlanDto are equal to the retrieved actions.
     * Indirectly with branchActionEqualTo it also checks the order of the dialPlanIds. This is necessary
     * since the order represents the button which has to be pressed in a call.
     */
    private void checkGetBranchDto(DialPlanDto dialPlanDto, int actionNumber) {
        List<ActionDto> actionDtos = actionServiceInterface.retrieveActions(dialPlanDto.getId());
        ActionDto expected = dialPlanDto.getActions().get(actionNumber - 1);
        ActionDto actual = actionDtos.get(actionNumber - 1);
        assertThat(expected, actionDtoAlmostEqualTo(actual));

        ObjectMapper objectMapper = new ObjectMapper();
        BranchActionDto expectedBranch = objectMapper.convertValue(expected.getTypeSpecific(), BranchActionDto.class);
        BranchActionDto actualBranch = objectMapper.convertValue(actual.getTypeSpecific(), BranchActionDto.class);
        assertThat(expectedBranch, branchActionEqualTo(actualBranch));
    }

    @Test
    public void updateActions() throws Exception {
        databaseFixtureBuilder1.setCompany(1).addDialPlan(1).addSipClient(1).addSipClient(2).build();
        DialPlanDto dialPlanDto = generateDialPlan(generateDialActionDto(1), 1);
        actionServiceInterface.saveActions(dialPlanDto);

        List<ActionDto> actionDtos = actionServiceInterface.retrieveActions(dialPlanDto.getId());

        ActionDto expected = dialPlanDto.getActions().get(0);
        ActionDto actual = actionDtos.get(0);
        assertThat(expected, actionDtoAlmostEqualTo(actual));

        // update
        List<ActionDto> actionDtoList = generateSayAlphaActionDtos(2);
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
        databaseFixtureBuilder1.setCompany(1).addDialPlan(1).addDialPlan(2).addSipClient(1).addSipClient(2).build();
        DialPlanDto dialPlanDto = generateDialPlan(generateDialActionDto(1), 1);
        actionServiceInterface.saveActions(dialPlanDto);

        List<ActionDto> actionDtos = actionServiceInterface.retrieveActions(dialPlanDto.getId());

        ActionDto expected = dialPlanDto.getActions().get(0);
        ActionDto actual = actionDtos.get(0);
        assertThat(expected, actionDtoAlmostEqualTo(actual));

        actionServiceInterface.deleteActions(dialPlanDto.getId());

        List<ActionDto> actualActionDtos = actionServiceInterface.retrieveActions(dialPlanDto.getId());

        assertThat(actualActionDtos, is(empty()));
    }

    private DialPlanDto generateDialPlan(ActionDto actionDto, int number) {
        List<ActionDto> actionDtos = new ArrayList<>();
        actionDtos.add(actionDto);
        return generateDialPlan(actionDtos, number);

    }

    private DialPlanDto generateDialPlan(List<ActionDto> actionDtos, int number) {
        DialPlanDto dialPlanDto =
                dialPlanServiceImpl.dialPlanEntityToDialPlanDto(databaseFixtureBuilder1.getDialPlanList().get(number));
        dialPlanDto.setActions(actionDtos);
        return dialPlanDto;
    }

    private List<ActionDto> generateSayAlphaActionDtos(int number) {
        List<ActionDto> actionDtos = new ArrayList<>();
        for (int i = 1; i <= number; i++) {
            actionDtos.add(generateSayAlphaActionDto(i));
        }
        return actionDtos;
    }

    private ActionDto generateSayAlphaActionDto(int number) {
        SayAlphaActionDto sayAlphaActionDto = SayAlphaActionDtoGenerator.createTestDialActionDto(number);

        ObjectMapper objM = new ObjectMapper();
        LinkedHashMap linkedHashMap = objM.convertValue(sayAlphaActionDto, LinkedHashMap.class);

        return ActionDtoGenerator.createTestActionDto(number, "SayAlpha", linkedHashMap);
    }

    private ActionDto generateGotoActionDto(int number, int dialPanNumber) {
        GotoActionDto gotoActionDto = new GotoActionDto();
        gotoActionDto.setNextDialPlanId(databaseFixtureBuilder1.getDialPlanList().get(dialPanNumber).getId());

        ObjectMapper objM = new ObjectMapper();
        LinkedHashMap linkedHashMap = objM.convertValue(gotoActionDto, LinkedHashMap.class);

        return ActionDtoGenerator.createTestActionDto(number, "Goto", linkedHashMap);
    }

    private ActionDto generateBranchActionDto(int number, List<Integer> dialPanNumbers) {
        BranchActionDto branchActionDto = new BranchActionDto();
        branchActionDto.setHangupTime(number);

        List<Long> dialPlaIds = new ArrayList<>();

        dialPanNumbers.forEach(x ->
                dialPlaIds.add(databaseFixtureBuilder1.getDialPlanList().get(x).getId()));

        branchActionDto.setNextDialPlanIds(dialPlaIds);

        ObjectMapper objM = new ObjectMapper();
        LinkedHashMap linkedHashMap = objM.convertValue(branchActionDto, LinkedHashMap.class);

        return ActionDtoGenerator.createTestActionDto(number, "Branch", linkedHashMap);
    }

    private List<ActionDto> generateDialActionDtos(int number) {
        List<ActionDto> actionDtos = new ArrayList<>();
        for (int i = 1; i <= number; i++) {
            actionDtos.add(generateDialActionDto(i));
        }
        return actionDtos;
    }

    private ActionDto generateDialActionDto(int number) {
        List<SipClientDto> sipClientDtoList = new ArrayList<>();
        databaseFixtureBuilder1.getSipClientList().values()
                .forEach(x -> sipClientDtoList.add(sipClientEntityToSipClientDto(x)));

        DialActionDto dialActionDto = DialActionDtoGenerator.createTestDialActionDto(number, sipClientDtoList);

        ObjectMapper objM = new ObjectMapper();
        LinkedHashMap linkedHashMap = objM.convertValue(dialActionDto, LinkedHashMap.class);

        return ActionDtoGenerator.createTestActionDto(number, "Dial", linkedHashMap);
    }

}