package ch.zhaw.psit4.security;

import ch.zhaw.psit4.dto.CompanyDto;
import ch.zhaw.psit4.dto.SipClientDto;
import ch.zhaw.psit4.security.auxiliary.AdminDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContext;

import java.util.Collections;
import java.util.List;

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

    private final AdminDetails adminDetails;

    public SecurityInformation(final SecurityContext securityContext) {
        adminDetails = currentPrincipal(securityContext);
    }

    private AdminDetails currentPrincipal(final SecurityContext securityContext) {
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

    public boolean isOperator() {
        return adminDetails.isSuperAdmin();
    }

    public AdminDetails getAdminDetails() {
        return adminDetails;
    }

    public List<Long> allowedCompanies() {
        return Collections.unmodifiableList(adminDetails.getCompanyIds());
    }

    public void hasAccessToOrThrow(SipClientDto sipClientDto) {
        assert sipClientDto != null;

        if (adminDetails.isSuperAdmin()) {
            return;
        }

        try {
            hasAccessToOrThrow(sipClientDto.getCompany());
        } catch (AccessDeniedException e) {
            LOGGER.error("User '{}' tried to access SipClient with id {}", adminDetails.getUsername(), sipClientDto
                    .getId());

            throw new AccessDeniedException("Not allowed to access sipClient", e);
        }
    }

    public void hasAccessToOrThrow(CompanyDto companyDto) {
        if (companyDto == null) {
            LOGGER.error("User '{}' tried to access null Company. Denied Access.", adminDetails.getUsername());
            throw new AccessDeniedException("Not allowed to access null Company");
        }

        if (adminDetails.isSuperAdmin()) {
            return;
        }

        if (!allowedCompanies().contains(companyDto.getId())) {
            LOGGER.error("User '{}' tried to access Company '{}' (id: {})",
                    adminDetails.getUsername(),
                    companyDto.getName(),
                    companyDto.getId());
            throw new AccessDeniedException("Not allowed to access Company");
        }
    }

}
