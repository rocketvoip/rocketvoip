package ch.zhaw.psit4.testsupport.fixtures.database;

import ch.zhaw.psit4.data.jpa.entities.SayAlpha;
import ch.zhaw.psit4.testsupport.fixtures.general.SayAlphaData;

/**
 * @author Rafael Ostertag
 */
public final class SayAlphaEntity {
    private SayAlphaEntity() {
        // intentionally empty
    }

    public static SayAlpha createSayAlphaEntity(int number, int timeout) {
        SayAlpha sayAlpha = new SayAlpha(SayAlphaData.getName(number),
                SayAlphaData.getMessage(number),
                timeout,
                null);
        return sayAlpha;
    }
}
