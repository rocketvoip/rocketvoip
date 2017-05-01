package ch.zhaw.psit4.testsupport.fixtures.database;

import ch.zhaw.psit4.data.jpa.entities.Branch;
import ch.zhaw.psit4.testsupport.fixtures.general.BranchData;

/**
 * @author Jona Braun
 */
public final class BranchEntity {
    private BranchEntity() {
        // intentionally empty
    }

    public static Branch createBranchEntity(int number, int priority) {
        return new Branch(BranchData.getName(number),
                priority,
                null,
                BranchData.getHangupTime(number));
    }
}
