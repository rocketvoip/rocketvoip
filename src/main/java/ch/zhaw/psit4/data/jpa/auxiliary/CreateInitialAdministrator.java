package ch.zhaw.psit4.data.jpa.auxiliary;

import ch.zhaw.psit4.data.jpa.entities.Admin;
import ch.zhaw.psit4.data.jpa.repositories.AdminRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Component taking care of creating an Administrator account in a pristine database.
 *
 * @author Rafael Ostertag
 */
@Component
public class CreateInitialAdministrator {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateInitialAdministrator.class);
    private AdminRepository adminRepository;

    public CreateInitialAdministrator(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    /**
     * Create an initial administrator account if no admin account exists.
     */
    public void createInitialAdminAccount() {
        if (hasAdminUsers()) {
            LOGGER.debug("Found administratior account(s).");
            return;
        }

        LOGGER.info("No administrator accounts found. Creating initial administrator accounts");

        String password = createRandomPassword();
        Admin initialAdminAccount = createInitialAdminAccountEntity(password);
        adminRepository.save(initialAdminAccount);
        LOGGER.warn("Created initial administrator account '{}' with password '{}'", initialAdminAccount.getUsername
                (), password);
    }

    private Admin createInitialAdminAccountEntity(String password) {
        return new Admin(null, "RocketVoip", "Administrator", "masteradmin", password, true);
    }

    private String createRandomPassword() {
        final SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }

    private boolean hasAdminUsers() {
        return adminRepository.count() != 0;
    }

    /**
     * Run after construction.
     */
    @PostConstruct
    public void init() {
        createInitialAdminAccount();
    }
}
