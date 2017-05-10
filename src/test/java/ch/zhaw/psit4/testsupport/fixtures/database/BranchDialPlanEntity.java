package ch.zhaw.psit4.testsupport.fixtures.database;

import ch.zhaw.psit4.data.jpa.entities.BranchDialPlan;
import ch.zhaw.psit4.testsupport.fixtures.general.BranchDialPlanData;

/**
 * @author Jona Braun
 */
public final class BranchDialPlanEntity {
    private BranchDialPlanEntity() {
        // intentionally empty
    }

    public static BranchDialPlan createBranchDialPlanEntity(int number) {
        return new BranchDialPlan(null,
                BranchDialPlanData.getButton(number));
    }
}
