package ch.zhaw.psit4.testsupport.fixtures.database;

import ch.zhaw.psit4.data.jpa.entities.DialPlan;
import ch.zhaw.psit4.testsupport.fixtures.general.DialPlanData;

/**
 * @author Rafael Ostertag
 */
public final class DialPlanEntity {
    private DialPlanEntity() {
        // intentionally empty
    }

    /**
     * Create a DialPlan entity. The company field is null and has to be set by the caller.
     *
     * @param number number of fixture.
     * @return DialPlan entity.
     */
    public static DialPlan createDialPlanEntity(int number) {
        return new DialPlan(DialPlanData.getTitle(number),
                DialPlanData.getPhoneNumber(number),
                null
        );

    }
}
