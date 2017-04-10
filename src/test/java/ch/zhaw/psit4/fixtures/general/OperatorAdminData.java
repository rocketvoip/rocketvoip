package ch.zhaw.psit4.fixtures.general;

/**
 * General methods for operator admin related data.
 *
 * @author Rafael Ostertag
 */
public final class OperatorAdminData {

    public static final String OPERATOR_ADMIN_FIRSTNAME_PREFIX = "OperatorAdmin Firstname ";
    public static final String OPERATOR_ADMIN_LASTNAME_PREFIX = "OperatorAdmin Lastname ";
    public static final String OPERATOR_ADMIN_PASSWORD_PREFIX = "OperatorAdmin Password ";

    private OperatorAdminData() {
        // intentionally empty
    }

    public static String getOperatorAdminFirstname(int number) {
        return OPERATOR_ADMIN_FIRSTNAME_PREFIX + number;
    }

    public static String getOperatorAdminLastname(int number) {
        return OPERATOR_ADMIN_LASTNAME_PREFIX + number;
    }

    public static String getOperatorAdminPassword(int number) {
        return OPERATOR_ADMIN_PASSWORD_PREFIX + number;
    }

    public static String getOperatorAdminUsername(int number) {
        return "operatoradmin.user" + number + "@local.host";
    }
}
