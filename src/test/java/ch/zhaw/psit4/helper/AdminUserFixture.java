package ch.zhaw.psit4.helper;

import ch.zhaw.psit4.data.jpa.entities.Admin;
import ch.zhaw.psit4.security.auxiliary.AdminDetails;

/**
 * Create Admin Entity and AdminDetails used in security tests.
 *
 * @author Rafael Ostertag
 */
public final class AdminUserFixture {
    private AdminUserFixture() {
        // intentionally empty
    }

    public static Admin createAdminEntityFixture() {
        return new Admin(null, "testfirstname", "testlastname", "test", "testpw", false);
    }

    public static AdminDetails createAdminDetails(Admin admin) {
        return new AdminDetails(admin);
    }
}
