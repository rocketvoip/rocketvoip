package ch.zhaw.psit4.security.auxiliary;

import java.util.concurrent.TimeUnit;

/**
 * @author Rafael Ostertag
 */
public final class SecurityConstants {
    public static final String CONFIG_ADMIN_ROLE_NAME = "CONFIG_ADMIN";
    public static final String COMPANY_ADMIN_ROLE_NAME = "COMPANY_ADMIN";
    public static final String AUTH_HEADER_NAME = "X-AUTH-TOKEN";
    public static final long TOKEN_EXPIRATION_IN_MILLIS = TimeUnit.HOURS.toMillis(1l);
    public static final String ROLE_PREFIX = "ROLE_";


    private SecurityConstants() {
        // intentionally empty
    }
}
