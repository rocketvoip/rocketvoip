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

package ch.zhaw.psit4.security.web;

import ch.zhaw.psit4.security.auxiliary.UserAuthentication;
import ch.zhaw.psit4.security.dto.AuthenticationDto;
import ch.zhaw.psit4.security.dto.LoginDataDto;
import ch.zhaw.psit4.security.jwt.TokenAuthenticationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Rafael Ostertag
 */
public class StatelessLoginFilter extends AbstractAuthenticationProcessingFilter {
    private static final Logger OUR_LOGGER = LoggerFactory.getLogger(StatelessLoginFilter.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final TokenAuthenticationService tokenAuthenticationService;
    private final UserDetailsService userDetailsService;

    public StatelessLoginFilter(String defaultFilterProcessesUrl,
                                TokenAuthenticationService tokenAuthenticationService,
                                UserDetailsService userDetailsService,
                                AuthenticationManager authenticationManager) {
        super(defaultFilterProcessesUrl);
        this.tokenAuthenticationService = tokenAuthenticationService;
        this.userDetailsService = userDetailsService;
        setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse
            httpServletResponse) throws IOException, ServletException {
        try {
            final LoginDataDto loginData = OBJECT_MAPPER.readValue(httpServletRequest.getInputStream(), LoginDataDto.class);
            final UsernamePasswordAuthenticationToken authenticationToken = loginData
                    .toUsernamePasswordAuthenticationToken();

            OUR_LOGGER.debug("Trying to authenticate user '{}'", loginData.getUsername());
            return getAuthenticationManager().authenticate(authenticationToken);
        } catch (Exception e) {
            OUR_LOGGER.error("Error encountered while authenticating user.", e);
            throw new AuthenticationServiceException("Error while authenticating. Try again.");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain
            chain, Authentication authResult) throws IOException, ServletException {
        final UserDetails authenticatedUser = userDetailsService.loadUserByUsername(authResult.getName());
        final UserAuthentication userAuthentication = new UserAuthentication(authenticatedUser);
        tokenAuthenticationService.addAuthentication(response, userAuthentication);

        userAuthentication.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(userAuthentication);

        populateResponseWithInformation(response, authenticatedUser);

        OUR_LOGGER.info("Successfully authenticated '{}'", authenticatedUser.getUsername());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    private void populateResponseWithInformation(HttpServletResponse response, UserDetails authenticatedUser) {
        try {
            AuthenticationDto authenticationDto = AuthenticationDto.fromUserDetails(authenticatedUser);

            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.getWriter().print(OBJECT_MAPPER.writeValueAsString(authenticationDto));
        } catch (JsonProcessingException e) {
            OUR_LOGGER.error("Error serializing authentication information", e);
        } catch (IOException e) {
            OUR_LOGGER.error("Error writing response", e);
        }
    }
}
