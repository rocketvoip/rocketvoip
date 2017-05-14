package ch.zhaw.psit4.testsupport.fixtures.database;

import ch.zhaw.psit4.data.jpa.entities.Admin;
import ch.zhaw.psit4.testsupport.fixtures.general.AdminData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Create Admin entity fixtures.
 *
 * @author Rafael Ostertag
 */
public final class AdminEntity {
    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

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
                AdminData.getAdminFirstname(number),
                AdminData.getAdminLastname(number),
                AdminData.getAdminUsername(number),
                PASSWORD_ENCODER.encode(AdminData.getAdminPassword(number)),
                false);
    }


}
