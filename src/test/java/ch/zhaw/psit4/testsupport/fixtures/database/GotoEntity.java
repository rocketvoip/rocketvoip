package ch.zhaw.psit4.testsupport.fixtures.database;

import ch.zhaw.psit4.data.jpa.entities.Goto;
import ch.zhaw.psit4.testsupport.fixtures.general.GotoData;

/**
 * @author Jona Braun
 */
public final class GotoEntity {
    private GotoEntity() {
        // intentionally empty
    }

    public static Goto createGotoEntity(int number, int priority) {
        Goto gotoEntity = new Goto(GotoData.getName(number),
                priority,
                null,
                null);
        return gotoEntity;
    }
}
