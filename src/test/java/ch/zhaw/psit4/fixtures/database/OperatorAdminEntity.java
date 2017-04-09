package ch.zhaw.psit4.fixtures.database;

import ch.zhaw.psit4.data.jpa.entities.Admin;

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
                getOperatorAdminFirstname(number),
                getOperatorAdminLastname(number),
                getOperatorAdminUsername(number),
                getOperatorAdminPassword(number),
                false);
    }

    public static String getOperatorAdminFirstname(int number) {
        return "OperatorAdmin Firstname " + number;
    }

    public static String getOperatorAdminLastname(int number) {
        return "OperatorAdmin Lastname " + number;
    }

    public static String getOperatorAdminPassword(int number) {
        return "OperatorAdmin Password " + number;
    }

    public static String getOperatorAdminUsername(int number) {
        return "operatoradmin.user" + number + "@local.host";
    }


}
