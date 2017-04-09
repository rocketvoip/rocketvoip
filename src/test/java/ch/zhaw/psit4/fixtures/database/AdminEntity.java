package ch.zhaw.psit4.fixtures.database;

import ch.zhaw.psit4.data.jpa.entities.Admin;

/**
 * Create Admin entity fixtures.
 *
 * @author Rafael Ostertag
 */
public final class AdminEntity {
    private AdminEntity() {
        // intentionally empty
    }

    /**
     * Create an admin entity without assigning it to a company. The entity returned will have the superAdmin flag
     * set to false.
     *
     * @param number admin number
     * @return Admin entity.
     */
    public static Admin createAdmin(int number) {
        return new Admin(null,
                getAdminFirstname(number),
                getAdminLastname(number),
                getAdminUsername(number),
                getAdminPassword(number),
                false);
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
