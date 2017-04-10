package ch.zhaw.psit4.helper.security;

import ch.zhaw.psit4.data.jpa.entities.Admin;
import ch.zhaw.psit4.security.auxiliary.AdminDetails;
import ch.zhaw.psit4.security.jwt.TokenHandler;

/**
 * Create JWT authentication token from Admin entity.
 *
 * @author Rafael Ostertag
 */
public final class AuthenticationToken {
    // userService is only used when tokens are parsed. This tests here only create tokens, thus it can be null
    private static final TokenHandler tokenHandler = new TokenHandler("secret", null);

    private AuthenticationToken() {
        // intentionally empty
    }

    public static String createTokenFor(Admin admin) {
        return tokenHandler.createTokenForUser(new AdminDetails(admin));
    }
}
