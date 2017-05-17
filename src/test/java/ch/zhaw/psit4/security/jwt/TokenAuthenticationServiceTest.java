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

package ch.zhaw.psit4.security.jwt;

import ch.zhaw.psit4.data.jpa.entities.Admin;
import ch.zhaw.psit4.security.auxiliary.AdminDetails;
import ch.zhaw.psit4.security.auxiliary.SecurityConstants;
import ch.zhaw.psit4.security.auxiliary.UserAuthentication;
import ch.zhaw.psit4.testsupport.fixtures.database.AdminEntity;
import ch.zhaw.psit4.testsupport.fixtures.security.AdminUserFixture;
import ch.zhaw.psit4.testsupport.mocks.HttpServletMock;
import ch.zhaw.psit4.testsupport.mocks.UserDetailsServiceMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.hamcrest.Matchers.*;
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
    private TokenHandler tokenHandler;
    private Admin admin;
    private AdminDetails adminDetails;

    @Before
    public void setUp() throws Exception {
        admin = AdminEntity.createAdmin(1);
        adminDetails = AdminUserFixture.createAdminDetails(admin);
        userDetailsServiceMock = UserDetailsServiceMock.makeMockForAdmin(admin);
        tokenHandler = mock(TokenHandler.class);
        tokenAuthenticationService = new TokenAuthenticationService(tokenHandler);

        httpServletResponseMock = HttpServletMock.mockHttpServletResponse();
        httpServletRequestMock = HttpServletMock.mockHttpServletRequest();
    }

    @Test
    public void addAuthentication() throws Exception {
        UserDetails userDetails = new AdminDetails(admin);
        UserAuthentication userAuthentication = new UserAuthentication(userDetails);

        when(tokenHandler.createTokenForUser(userDetails)).thenReturn("token");

        String token = tokenAuthenticationService.addAuthentication(httpServletResponseMock, userAuthentication);

        verify(tokenHandler).createTokenForUser(userDetails);
        verify(httpServletResponseMock).addHeader(SecurityConstants.AUTH_HEADER_NAME, token);
        assertThat(token, equalTo("token"));
    }

    @Test
    public void getAuthenticationNonNullToken() throws Exception {
        when(httpServletRequestMock.getHeader(SecurityConstants.AUTH_HEADER_NAME)).thenReturn("token");
        when(tokenHandler.parseUserFromToken("token")).thenReturn(adminDetails);

        UserAuthentication actual = tokenAuthenticationService.getAuthentication(httpServletRequestMock);

        verify(httpServletRequestMock).getHeader(SecurityConstants.AUTH_HEADER_NAME);
        verify(tokenHandler).parseUserFromToken("token");

        assertThat(actual, is(not(nullValue())));
        assertThat(actual.getName(), equalTo(adminDetails.getUsername()));

    }

    @Test
    public void getAuthenticationNullToken() throws Exception {
        when(httpServletRequestMock.getHeader(SecurityConstants.AUTH_HEADER_NAME)).thenReturn(null);

        UserAuthentication actual = tokenAuthenticationService.getAuthentication(httpServletRequestMock);

        verify(httpServletRequestMock).getHeader(SecurityConstants.AUTH_HEADER_NAME);
        verify(tokenHandler, never()).parseUserFromToken(anyString());

        assertThat(actual, is(nullValue()));
    }

    @Test
    public void getAuthenticationNullUserDetails() throws Exception {
        when(httpServletRequestMock.getHeader(SecurityConstants.AUTH_HEADER_NAME)).thenReturn("token");
        when(tokenHandler.parseUserFromToken("token")).thenReturn(null);

        UserAuthentication actual = tokenAuthenticationService.getAuthentication(httpServletRequestMock);

        verify(httpServletRequestMock).getHeader(SecurityConstants.AUTH_HEADER_NAME);
        verify(tokenHandler).parseUserFromToken("token");

        assertThat(actual, is(nullValue()));
    }

}