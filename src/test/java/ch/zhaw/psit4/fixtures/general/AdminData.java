package ch.zhaw.psit4.fixtures.general;

/**
 * General methods for admin related data.
 *
 * @author Rafael Ostertag
 */
public final class AdminData {

    public static final String ADMIN_FIRSTNAME_PREFIX = "Admin Firstname ";
    public static final String ADMIN_LASTNAME_PREFIX = "Admin Lastname ";
    public static final String ADMIN_PASSWORD_PREFIX = "Admin Password ";

    private AdminData() {
        // intentionally empty
    }

    public static String getAdminFirstname(int number) {
        return ADMIN_FIRSTNAME_PREFIX + number;
    }

    public static String getAdminLastname(int number) {
        return ADMIN_LASTNAME_PREFIX + number;
    }

    public static String getAdminPassword(int number) {
        return ADMIN_PASSWORD_PREFIX + number;
    }

    public static String getAdminUsername(int number) {
        return "admin.user" + number + "@local.host";
    }
}
