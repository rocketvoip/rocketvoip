package ch.zhaw.psit4.testsupport.fixtures.database;

import ch.zhaw.psit4.data.jpa.entities.Dial;
import ch.zhaw.psit4.testsupport.fixtures.general.DialData;

/**
 * @author Rafael Ostertag
 */
public final class DialEntity {
    private DialEntity() {
        // intentionally empty
    }

    public static Dial createDialEntity(int number, int priority, int timeout) {
        return new Dial(DialData.getTitle(number), priority, timeout, null, null);
    }
}
