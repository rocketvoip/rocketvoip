package ch.zhaw.psit4.tests.fixtures.database;

import ch.zhaw.psit4.data.jpa.entities.Admin;
import ch.zhaw.psit4.tests.fixtures.general.OperatorAdminData;

/**
 * Create Operator Admin entity fixtures. Difference to Admin entities is simply the superAdmin flag
 *
 * @author Rafael Ostertag
 */
public final class OperatorAdminEntity {
    private OperatorAdminEntity() {
        // intentionally empty
    }

    /**
     * Create an admin entity without assigning it to a company. The entity returned will have the superOperatorAdmin
     * flag set to true.
     *
     * @param number OperatorAdmin number
     * @return Admin entity.
     */
    public static Admin createOperatorAdmin(int number) {
        return new Admin(null,
                OperatorAdminData.getOperatorAdminFirstname(number),
                OperatorAdminData.getOperatorAdminLastname(number),
                OperatorAdminData.getOperatorAdminUsername(number),
                OperatorAdminData.getOperatorAdminPassword(number),
                true);
    }


}
