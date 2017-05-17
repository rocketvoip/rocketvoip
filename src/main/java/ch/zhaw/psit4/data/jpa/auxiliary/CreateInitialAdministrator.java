/*
 * Copyright 2017 Jona Braun, Benedikt Herzog, Rafael Ostertag,
 *                Marcel Schöni, Marco Studerus, Martin Wittwer
 *
 * Redistribution and  use in  source and binary  forms, with  or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * 1. Redistributions  of  source code  must retain  the above  copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in  binary form must reproduce  the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation   and/or   other    materials   provided   with   the
 *    distribution.
 *
 * THIS SOFTWARE  IS PROVIDED BY  THE COPYRIGHT HOLDERS  AND CONTRIBUTORS
 * "AS  IS" AND  ANY EXPRESS  OR IMPLIED  WARRANTIES, INCLUDING,  BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES  OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE  ARE DISCLAIMED. IN NO EVENT  SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL,  EXEMPLARY,  OR  CONSEQUENTIAL DAMAGES  (INCLUDING,  BUT  NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE  GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS  INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF  LIABILITY, WHETHER IN  CONTRACT, STRICT LIABILITY,  OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN  ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package ch.zhaw.psit4.data.jpa.auxiliary;

import ch.zhaw.psit4.data.jpa.entities.Admin;
import ch.zhaw.psit4.data.jpa.repositories.AdminRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    public static final String INITIAL_FIRSTNAME = "RocketVoip";
    public static final String INITIAL_LASTNAME = "Administrator";
    public static final String INITIAL_USERNAME = "masteradmin@rocketvoip.local";
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateInitialAdministrator.class);
    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
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
        return new Admin(null,
                INITIAL_FIRSTNAME,
                INITIAL_LASTNAME,
                INITIAL_USERNAME,
                encodePassword(password),
                true);
    }

    private String createRandomPassword() {
        final SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }

    private String encodePassword(String password) {
        return PASSWORD_ENCODER.encode(password);
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
