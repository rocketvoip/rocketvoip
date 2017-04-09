package ch.zhaw.psit4.security.jwt;

import ch.zhaw.psit4.data.jpa.entities.Admin;
import ch.zhaw.psit4.helper.AdminUserFixture;
import ch.zhaw.psit4.helper.mocks.HttpServletMock;
import ch.zhaw.psit4.helper.mocks.UserDetailsServiceMock;
import ch.zhaw.psit4.security.auxiliary.AdminDetails;
import ch.zhaw.psit4.security.auxiliary.SecurityConstants;
import ch.zhaw.psit4.security.auxiliary.UserAuthentication;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static ch.zhaw.psit4.helper.matchers.AdminDetailsEqualTo.adminDetailsEqualTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Rafael Ostertag
 */
public class TokenAuthenticationServiceTest {
    private TokenAuthenticationService tokenAuthenticationService;
    private UserDetailsService userDetailsServiceMock;
    private HttpServletResponse httpServletResponseMock;
    private HttpServletRequest httpServletRequestMock;
    private Admin admin;
    private AdminDetails adminDetails;

    @Before
    public void setUp() throws Exception {
        admin = AdminUserFixture.createAdminEntityFixture();
        adminDetails = AdminUserFixture.createAdminDetails(admin);
        userDetailsServiceMock = UserDetailsServiceMock.makeMockForAdmin(admin);
        tokenAuthenticationService = new TokenAuthenticationService("secret", userDetailsServiceMock);

        httpServletResponseMock = HttpServletMock.mockHttpServletResponse();
        httpServletRequestMock = HttpServletMock.mockHttpServletRequest();
    }

    @Test
    public void addAuthentication() throws Exception {
        UserAuthentication userAuthentication = new UserAuthentication(new AdminDetails(admin));
        String token = tokenAuthenticationService.addAuthentication(httpServletResponseMock, userAuthentication);

        verify(httpServletResponseMock).addHeader(SecurityConstants.AUTH_HEADER_NAME, token);
    }

    @Test
    public void getAuthentication() throws Exception {
        UserAuthentication userAuthentication = new UserAuthentication(adminDetails);
        TokenHandler tokenHandler = new TokenHandler("secret", userDetailsServiceMock);

        when(httpServletRequestMock.getHeader(SecurityConstants.AUTH_HEADER_NAME))
                .thenReturn(tokenHandler.createTokenForUser(new AdminDetails(admin)));

        UserAuthentication actualUserAuthentication = tokenAuthenticationService.getAuthentication
                (httpServletRequestMock);
        AdminDetails actual = (AdminDetails) actualUserAuthentication.getDetails();

        assertThat(actual, adminDetailsEqualTo(adminDetails));
        verify(httpServletRequestMock).getHeader(SecurityConstants.AUTH_HEADER_NAME);
        verify(userDetailsServiceMock).loadUserByUsername(any());
    }

    @Test
    public void getAuthenticationNull() throws Exception {
        when(httpServletRequestMock.getHeader(SecurityConstants.AUTH_HEADER_NAME))
                .thenReturn(null);

        UserAuthentication actualUserAuthentication = tokenAuthenticationService.getAuthentication
                (httpServletRequestMock);
        assertThat(actualUserAuthentication, is(nullValue()));
    }
}