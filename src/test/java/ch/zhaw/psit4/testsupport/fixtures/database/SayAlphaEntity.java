package ch.zhaw.psit4.testsupport.fixtures.database;

import ch.zhaw.psit4.data.jpa.entities.SayAlpha;
import ch.zhaw.psit4.testsupport.fixtures.general.ActionData;
import ch.zhaw.psit4.testsupport.fixtures.general.SayAlphaActionData;

/**
 * Create SayAlpha entities.
 *
 * @author Jona Braun
 */
public final class SayAlphaEntity {
    private SayAlphaEntity() {
        // intentionally empty
    }

    /**
     * The dialPlan is set to null.
     *
     * @param number number of fixture.
     * @return SayAlpha entity
     */
    public static SayAlpha createSayAlphaEntity(int number) {
        return new SayAlpha(ActionData.getName(number),
                Integer.toString(number),
                SayAlphaActionData.getVoiceMessage(number),
                SayAlphaActionData.getSleepTime(number),
                null);
    }

}
