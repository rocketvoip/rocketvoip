package ch.zhaw.psit4.testsupport.fixtures.dto;

import ch.zhaw.psit4.dto.actions.SayAlphaAction;
import ch.zhaw.psit4.testsupport.fixtures.general.SayAlphaActionData;

/**
 * @author Jona Braun
 */
public class SayAlphaActionGenerator {
    public static final long NON_EXISTING_ID = 100;

    private SayAlphaActionGenerator() {
        //intentionally empty
    }

    public static SayAlphaAction createTestDialActionDto(long number) {
        SayAlphaAction sayAlphaAction = new SayAlphaAction();
        sayAlphaAction.setSleepTime(SayAlphaActionData.getSleepTime((int) number));
        sayAlphaAction.setVoiceMessage(SayAlphaActionData.getVoiceMessage((int) number));
        return sayAlphaAction;
    }
}
