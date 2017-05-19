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

package ch.zhaw.psit4.security;

import ch.zhaw.psit4.dto.CompanyDto;
import ch.zhaw.psit4.dto.DialPlanDto;
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
public class ReferenceMonitor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReferenceMonitor.class);

    private final AdminDetails adminDetails;

    public ReferenceMonitor(final SecurityContext securityContext) {
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

    public void inAllowedCompaniesOrThrow(long id) {
        if (isOperator()) {
            return;
        }

        if (allowedCompanies().contains(id)) {
            return;
        }

        LOGGER.error("User '{}' tried to access Company with id {})", adminDetails.getUsername(), id);

        throw new AccessDeniedException("Access denied");
    }

    public void isOperatorOrThrow() {
        if (adminDetails.isSuperAdmin()) {
            return;
        }
        LOGGER.error("User '{}' tried to access object where only operators have access", adminDetails.getUsername());

        throw new AccessDeniedException("Not allowed");
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

    public void hasAccessToOrThrow(DialPlanDto dialPlanDto) {
        assert dialPlanDto != null;

        if (adminDetails.isSuperAdmin()) {
            return;
        }

        try {
            hasAccessToOrThrow(dialPlanDto.getCompany());
        } catch (AccessDeniedException e) {
            LOGGER.error("User '{}' tried to access SipClient with id {}", adminDetails.getUsername(), dialPlanDto
                    .getId());

            throw new AccessDeniedException("Not allowed to access sipClient", e);
        }
    }
}
