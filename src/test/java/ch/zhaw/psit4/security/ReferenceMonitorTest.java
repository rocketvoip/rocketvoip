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

import ch.zhaw.psit4.data.jpa.entities.Admin;
import ch.zhaw.psit4.dto.CompanyDto;
import ch.zhaw.psit4.dto.DialPlanDto;
import ch.zhaw.psit4.dto.SipClientDto;
import ch.zhaw.psit4.security.auxiliary.AdminDetails;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Rafael Ostertag
 */
public class ReferenceMonitorTest {
    private SecurityContext securityContextMock;
    private Authentication authenticationMock;
    private Admin adminMock;
    private AdminDetails adminDetailsMock;

    @Before
    public void setUp() throws Exception {
        securityContextMock = mock(SecurityContext.class);
        authenticationMock = mock(Authentication.class);
        adminMock = mock(Admin.class);
        adminDetailsMock = mock(AdminDetails.class);

        when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        when(authenticationMock.getPrincipal()).thenReturn(adminDetailsMock);
        when(adminDetailsMock.getUsername()).thenReturn("mockuser");
    }

    @Test
    public void initializationHappyPath() throws Exception {
        AdminDetails adminDetails = new AdminDetails(adminMock);
        when(authenticationMock.getPrincipal()).thenReturn(adminDetails);

        ReferenceMonitor referenceMonitor = new ReferenceMonitor(securityContextMock);
        assertThat(referenceMonitor.getAdminDetails(), equalTo(adminDetails));
    }

    @Test(expected = SecurityException.class)
    public void currentPrincipalNullPrincipal() throws Exception {
        when(authenticationMock.getPrincipal()).thenReturn(null);

        ReferenceMonitor referenceMonitor = new ReferenceMonitor(securityContextMock);
    }

    @Test(expected = SecurityException.class)
    public void currentPrincipalNonAdminDetailsInstance() throws Exception {
        UserDetails userDetailsMock = mock(UserDetails.class);
        when(authenticationMock.getPrincipal()).thenReturn(userDetailsMock);

        ReferenceMonitor referenceMonitor = new ReferenceMonitor(securityContextMock);
    }

    @Test
    public void isOperatorTrue() throws Exception {
        when(adminDetailsMock.isSuperAdmin()).thenReturn(true);

        ReferenceMonitor referenceMonitor = new ReferenceMonitor(securityContextMock);

        assertThat(referenceMonitor.getAdminDetails(), equalTo(adminDetailsMock));
        assertThat(referenceMonitor.isOperator(), equalTo(true));

        verify(adminDetailsMock).isSuperAdmin();
    }

    @Test
    public void isOperatorFalse() throws Exception {
        when(adminDetailsMock.isSuperAdmin()).thenReturn(false);

        ReferenceMonitor referenceMonitor = new ReferenceMonitor(securityContextMock);

        assertThat(referenceMonitor.getAdminDetails(), equalTo(adminDetailsMock));
        assertThat(referenceMonitor.isOperator(), equalTo(false));

        verify(adminDetailsMock).isSuperAdmin();
    }

    @Test
    public void allowedCompanies() throws Exception {
        when(adminDetailsMock.getCompanyIds()).thenReturn(new ArrayList<>());

        ReferenceMonitor referenceMonitor = new ReferenceMonitor(securityContextMock);

        referenceMonitor.allowedCompanies();
        verify(adminDetailsMock).getCompanyIds();
    }

    @Test(expected = AccessDeniedException.class)
    public void inAllowedCompaniesOrThrowEmptyList() throws Exception {
        when(adminDetailsMock.getCompanyIds()).thenReturn(new ArrayList<>());
        ReferenceMonitor referenceMonitor = new ReferenceMonitor(securityContextMock);

        referenceMonitor.inAllowedCompaniesOrThrow(1L);
    }

    @Test(expected = AccessDeniedException.class)
    public void inAllowedCompaniesOrThrowNotInList() throws Exception {
        when(adminDetailsMock.getCompanyIds()).thenReturn(Arrays.asList(2L, 3L));
        ReferenceMonitor referenceMonitor = new ReferenceMonitor(securityContextMock);

        referenceMonitor.inAllowedCompaniesOrThrow(1L);
    }

    @Test
    public void inAllowedCompaniesOrThrowInList() throws Exception {
        when(adminDetailsMock.getCompanyIds()).thenReturn(Arrays.asList(1L, 2L, 3L));
        ReferenceMonitor referenceMonitor = new ReferenceMonitor(securityContextMock);

        referenceMonitor.inAllowedCompaniesOrThrow(1L);
        verify(adminDetailsMock).getCompanyIds();
    }

    @Test
    public void inAllowedCompaniesOrThrowOperator() throws Exception {
        when(adminDetailsMock.isSuperAdmin()).thenReturn(true);
        when(adminDetailsMock.getCompanyIds()).thenReturn(Arrays.asList(1L, 2L, 3L));
        ReferenceMonitor referenceMonitor = new ReferenceMonitor(securityContextMock);

        referenceMonitor.inAllowedCompaniesOrThrow(1L);
        verify(adminDetailsMock).isSuperAdmin();
        verify(adminDetailsMock, never()).getCompanyIds();
    }

    @Test(expected = AccessDeniedException.class)
    public void isOperatorOrThrowNonOperator() throws Exception {
        when(adminDetailsMock.isSuperAdmin()).thenReturn(false);
        ReferenceMonitor referenceMonitor = new ReferenceMonitor(securityContextMock);
        referenceMonitor.isOperatorOrThrow();
    }

    @Test
    public void isOperatorOrThrowOperator() throws Exception {
        when(adminDetailsMock.isSuperAdmin()).thenReturn(true);
        ReferenceMonitor referenceMonitor = new ReferenceMonitor(securityContextMock);
        referenceMonitor.isOperatorOrThrow();

        verify(adminDetailsMock).isSuperAdmin();
    }

    @Test(expected = AccessDeniedException.class)
    public void hasAccessToOrThrowNullCompany() throws Exception {

        ReferenceMonitor referenceMonitor = new ReferenceMonitor(securityContextMock);
        referenceMonitor.hasAccessToOrThrow((CompanyDto) null);
    }

    @Test
    public void companyHasAccessToOrThrowOperatorUser() throws Exception {
        when(adminDetailsMock.isSuperAdmin()).thenReturn(true);

        ReferenceMonitor referenceMonitor = new ReferenceMonitor(securityContextMock);
        referenceMonitor.hasAccessToOrThrow(new CompanyDto());

        verify(adminDetailsMock).isSuperAdmin();

    }

    @Test(expected = AccessDeniedException.class)
    public void companyHasAccessToOrThrowAdminUserNotAllowed() throws Exception {
        when(adminDetailsMock.isSuperAdmin()).thenReturn(false);
        when(adminDetailsMock.getCompanyIds()).thenReturn(Arrays.asList(1L, 2L));

        ReferenceMonitor referenceMonitor = new ReferenceMonitor(securityContextMock);

        CompanyDto companyDto = new CompanyDto();
        companyDto.setName("test");
        companyDto.setId(3L);

        referenceMonitor.hasAccessToOrThrow(companyDto);
    }

    @Test
    public void companyHasAccessToOrThrowAdminUserAllowed() throws Exception {
        when(adminDetailsMock.isSuperAdmin()).thenReturn(false);
        when(adminDetailsMock.getCompanyIds()).thenReturn(Arrays.asList(1L, 2L));

        ReferenceMonitor referenceMonitor = new ReferenceMonitor(securityContextMock);

        CompanyDto companyDto = new CompanyDto();
        companyDto.setName("test");
        companyDto.setId(2L);

        referenceMonitor.hasAccessToOrThrow(companyDto);
        verify(adminDetailsMock).getCompanyIds();
    }

    @Test
    public void sipClientHasAccessToOrThrowOperator() throws Exception {
        when(adminDetailsMock.isSuperAdmin()).thenReturn(true);

        ReferenceMonitor referenceMonitor = new ReferenceMonitor(securityContextMock);
        referenceMonitor.hasAccessToOrThrow(new SipClientDto());

        verify(adminDetailsMock).isSuperAdmin();
    }

    @Test(expected = AccessDeniedException.class)
    public void sipClientHasAccessToOrThrowAdminUserNullCompany() throws Exception {
        when(adminDetailsMock.isSuperAdmin()).thenReturn(false);

        ReferenceMonitor referenceMonitor = new ReferenceMonitor(securityContextMock);
        referenceMonitor.hasAccessToOrThrow(new SipClientDto());
    }

    @Test(expected = AccessDeniedException.class)
    public void sipClientHasAccessToOrThrowAdminUserDeniedCompany() throws Exception {
        when(adminDetailsMock.isSuperAdmin()).thenReturn(false);
        when(adminDetailsMock.getCompanyIds()).thenReturn(Arrays.asList(1L, 2L));


        CompanyDto companyDto = new CompanyDto();
        companyDto.setId(3L);
        companyDto.setName("test");

        SipClientDto sipClientDto = new SipClientDto();
        sipClientDto.setCompany(companyDto);

        ReferenceMonitor referenceMonitor = new ReferenceMonitor(securityContextMock);
        referenceMonitor.hasAccessToOrThrow(sipClientDto);
    }

    @Test
    public void sipClientHasAccessToOrThrowAdminUserAllowedCompany() throws Exception {
        when(adminDetailsMock.isSuperAdmin()).thenReturn(false);
        when(adminDetailsMock.getCompanyIds()).thenReturn(Arrays.asList(1L, 2L));


        CompanyDto companyDto = new CompanyDto();
        companyDto.setId(1L);
        companyDto.setName("test");

        SipClientDto sipClientDto = new SipClientDto();
        sipClientDto.setCompany(companyDto);

        ReferenceMonitor referenceMonitor = new ReferenceMonitor(securityContextMock);
        referenceMonitor.hasAccessToOrThrow(sipClientDto);
    }

    @Test
    public void dialPlanHasAccessToOrThrowOperator() throws Exception {
        when(adminDetailsMock.isSuperAdmin()).thenReturn(true);

        ReferenceMonitor referenceMonitor = new ReferenceMonitor(securityContextMock);
        referenceMonitor.hasAccessToOrThrow(new DialPlanDto());

        verify(adminDetailsMock).isSuperAdmin();
    }

    @Test(expected = AccessDeniedException.class)
    public void dialPlanHasAccessToOrThrowAdminUserNullCompany() throws Exception {
        when(adminDetailsMock.isSuperAdmin()).thenReturn(false);

        ReferenceMonitor referenceMonitor = new ReferenceMonitor(securityContextMock);
        referenceMonitor.hasAccessToOrThrow(new DialPlanDto());
    }

    @Test(expected = AccessDeniedException.class)
    public void dialPlantHasAccessToOrThrowAdminUserDeniedCompany() throws Exception {
        when(adminDetailsMock.isSuperAdmin()).thenReturn(false);
        when(adminDetailsMock.getCompanyIds()).thenReturn(Arrays.asList(1L, 2L));


        CompanyDto companyDto = new CompanyDto();
        companyDto.setId(3L);
        companyDto.setName("test");

        DialPlanDto dialPlanDto = new DialPlanDto();
        dialPlanDto.setCompany(companyDto);

        ReferenceMonitor referenceMonitor = new ReferenceMonitor(securityContextMock);
        referenceMonitor.hasAccessToOrThrow(dialPlanDto);
    }

    @Test
    public void dialPlanHasAccessToOrThrowAdminUserAllowedCompany() throws Exception {
        when(adminDetailsMock.isSuperAdmin()).thenReturn(false);
        when(adminDetailsMock.getCompanyIds()).thenReturn(Arrays.asList(1L, 2L));


        CompanyDto companyDto = new CompanyDto();
        companyDto.setId(1L);
        companyDto.setName("test");

        DialPlanDto dialPlanDto = new DialPlanDto();
        dialPlanDto.setCompany(companyDto);

        ReferenceMonitor referenceMonitor = new ReferenceMonitor(securityContextMock);
        referenceMonitor.hasAccessToOrThrow(dialPlanDto);
    }
}