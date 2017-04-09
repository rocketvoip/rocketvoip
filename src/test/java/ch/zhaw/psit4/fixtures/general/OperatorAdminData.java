package ch.zhaw.psit4.fixtures.general;

/**
 * General methods for operator admin related data.
 *
 * @author Rafael Ostertag
 */
public final class OperatorAdminData {
    private OperatorAdminData() {
        // intentionally empty
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
