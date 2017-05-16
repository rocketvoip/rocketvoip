package ch.zhaw.psit4.security;

import ch.zhaw.psit4.data.jpa.entities.Admin;
import ch.zhaw.psit4.security.auxiliary.AdminDetails;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Rafael Ostertag
 */
public class SecurityInformationTest {
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
    }

    @Test
    public void initializationHappyPath() throws Exception {
        AdminDetails adminDetails = new AdminDetails(adminMock);
        when(authenticationMock.getPrincipal()).thenReturn(adminDetails);

        SecurityInformation securityInformation = new SecurityInformation(securityContextMock);
        assertThat(securityInformation.getAdminDetails(), equalTo(adminDetails));
    }

    @Test(expected = SecurityException.class)
    public void currentPrincipalNullPrincipal() throws Exception {
        when(authenticationMock.getPrincipal()).thenReturn(null);

        SecurityInformation securityInformation = new SecurityInformation(securityContextMock);
    }

    @Test(expected = SecurityException.class)
    public void currentPrincipalNonAdminDetailsInstance() throws Exception {
        UserDetails userDetailsMock = mock(UserDetails.class);
        when(authenticationMock.getPrincipal()).thenReturn(userDetailsMock);

        SecurityInformation securityInformation = new SecurityInformation(securityContextMock);
    }

    @Test
    public void isOperatorTrue() throws Exception {
        when(adminDetailsMock.isSuperAdmin()).thenReturn(true);

        SecurityInformation securityInformation = new SecurityInformation(securityContextMock);

        assertThat(securityInformation.getAdminDetails(), equalTo(adminDetailsMock));
        assertThat(securityInformation.isOperator(), equalTo(true));

        verify(adminDetailsMock).isSuperAdmin();
    }

    @Test
    public void isOperatorFalse() throws Exception {
        when(adminDetailsMock.isSuperAdmin()).thenReturn(false);

        SecurityInformation securityInformation = new SecurityInformation(securityContextMock);

        assertThat(securityInformation.getAdminDetails(), equalTo(adminDetailsMock));
        assertThat(securityInformation.isOperator(), equalTo(false));

        verify(adminDetailsMock).isSuperAdmin();
    }

    @Test
    public void allowedCompanies() throws Exception {
        when(adminDetailsMock.getCompanyIds()).thenReturn(new ArrayList<>());

        SecurityInformation securityInformation = new SecurityInformation(securityContextMock);

        securityInformation.allowedCompanies();
        verify(adminDetailsMock).getCompanyIds();
    }

}