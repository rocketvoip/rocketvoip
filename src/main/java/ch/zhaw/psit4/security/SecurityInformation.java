package ch.zhaw.psit4.security;

import ch.zhaw.psit4.security.auxiliary.AdminDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContext;

/**
 * Easy access to current Spring Security Context information.
 * <p>
 * Although the Spring Security Context is thread local, for easier testing instantiation requires the current
 * Security Context passed.
 *
 * @author Rafael Ostertag
 */
public class SecurityInformation {
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityInformation.class);
    private final SecurityContext securityContext;

    public SecurityInformation(final SecurityContext securityContext) {
        this.securityContext = securityContext;
    }

    public AdminDetails currentPrincipal() {
        final Object principal = securityContext.getAuthentication().getPrincipal();

        if (principal == null) {
            String message = "No principal in current context";
            LOGGER.error(message);
            throw new SecurityException(message);
        }

        if (!(principal instanceof AdminDetails)) {
            String message = "Principal not instance of AdminDetails";
            LOGGER.error(message);
            throw new SecurityException(message);
        }

        return (AdminDetails) principal;
    }
}
