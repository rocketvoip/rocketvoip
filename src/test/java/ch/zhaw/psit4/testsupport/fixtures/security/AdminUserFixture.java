package ch.zhaw.psit4.testsupport.fixtures.security;

import ch.zhaw.psit4.data.jpa.entities.Admin;
import ch.zhaw.psit4.security.auxiliary.AdminDetails;

/**
 * Create AdminDetails used in security tests.
 *
 * @author Rafael Ostertag
 */
public final class AdminUserFixture {
    private AdminUserFixture() {
        // intentionally empty
    }

    public static AdminDetails createAdminDetails(Admin admin) {
        return new AdminDetails(admin);
    }
}
