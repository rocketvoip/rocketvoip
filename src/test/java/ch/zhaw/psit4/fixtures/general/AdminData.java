package ch.zhaw.psit4.fixtures.general;

/**
 * General methods for admin related data.
 *
 * @author Rafael Ostertag
 */
public final class AdminData {
    private AdminData() {
        // intentionally empty
    }

    public static String getAdminFirstname(int number) {
        return "Admin Firstname " + number;
    }

    public static String getAdminLastname(int number) {
        return "Admin Lastname " + number;
    }

    public static String getAdminPassword(int number) {
        return "Admin Password " + number;
    }

    public static String getAdminUsername(int number) {
        return "admin.user" + number + "@local.host";
    }
}
