package ch.zhaw.psit4.security;

import ch.zhaw.psit4.data.jpa.entities.Admin;
import ch.zhaw.psit4.security.auxiliary.AdminDetails;
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
    @Test
    public void getAdminDetailsHappyPath() throws Exception {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);

        Admin admin = mock(Admin.class);
        AdminDetails adminDetails = new AdminDetails(admin);
        when(authentication.getPrincipal()).thenReturn(adminDetails);

        SecurityInformation securityInformation = new SecurityInformation(securityContext);
        assertThat(securityInformation.getAdminDetails(), equalTo(adminDetails));
    }

    @Test(expected = SecurityException.class)
    public void currentPrincipalNullPrincipal() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(null);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityInformation securityInformation = new SecurityInformation(securityContext);
    }

    @Test(expected = SecurityException.class)
    public void currentPrincipalNonAdminDetailsInstance() throws Exception {
        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityInformation securityInformation = new SecurityInformation(securityContext);
    }

    @Test
    public void isOperatorTrue() throws Exception {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);

        AdminDetails adminDetails = mock(AdminDetails.class);
        when(adminDetails.isSuperAdmin()).thenReturn(true);

        when(authentication.getPrincipal()).thenReturn(adminDetails);

        SecurityInformation securityInformation = new SecurityInformation(securityContext);

        assertThat(securityInformation.getAdminDetails(), equalTo(adminDetails));
        assertThat(securityInformation.isOperator(), equalTo(true));

        verify(adminDetails).isSuperAdmin();
    }

    @Test
    public void isOperatorFalse() throws Exception {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);

        AdminDetails adminDetails = mock(AdminDetails.class);
        when(adminDetails.isSuperAdmin()).thenReturn(false);

        when(authentication.getPrincipal()).thenReturn(adminDetails);

        SecurityInformation securityInformation = new SecurityInformation(securityContext);

        assertThat(securityInformation.getAdminDetails(), equalTo(adminDetails));
        assertThat(securityInformation.isOperator(), equalTo(false));

        verify(adminDetails).isSuperAdmin();
    }

    @Test
    public void allowedCompanies() throws Exception {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);

        AdminDetails adminDetails = mock(AdminDetails.class);
        when(adminDetails.getCompanyIds()).thenReturn(new ArrayList<>());

        when(authentication.getPrincipal()).thenReturn(adminDetails);

        SecurityInformation securityInformation = new SecurityInformation(securityContext);

        securityInformation.allowedCompanies();
        verify(adminDetails).getCompanyIds();
    }

}