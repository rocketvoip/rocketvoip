package ch.zhaw.psit4.testsupport.fixtures.database;

import ch.zhaw.psit4.data.jpa.entities.Dial;
import ch.zhaw.psit4.testsupport.fixtures.general.ActionData;
import ch.zhaw.psit4.testsupport.fixtures.general.DialActionData;

/**
 * Create Dial entities.
 *
 * @author Jona Braun
 */
public final class DialEntity {
    private DialEntity() {
        // intentionally empty
    }

    /**
     * The dialPlan and sipClients are set to null.
     *
     * @param number number of fixture.
     * @return Dial entity
     */
    public static Dial createDialEntity(int number) {
        return new Dial(ActionData.getName(number),
                Integer.toString(number),
                DialActionData.getRingingTime(number),
                null,
                null);
    }

}
