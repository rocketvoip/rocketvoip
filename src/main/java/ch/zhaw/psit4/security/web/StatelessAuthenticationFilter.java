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
import ch.zhaw.psit4.security.jwt.TokenAuthenticationService;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * @author Rafael Ostertag
 */
public class StatelessAuthenticationFilter extends GenericFilterBean {
    // There is field called logger in GenericFilterBean.
    private static final Logger OUR_LOGGER = LoggerFactory.getLogger(StatelessLoginFilter.class);
    private final TokenAuthenticationService tokenAuthenticationService;

    public StatelessAuthenticationFilter(TokenAuthenticationService tokenAuthenticationService) {
        this.tokenAuthenticationService = tokenAuthenticationService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        try {
            OUR_LOGGER.debug("Process JWT authentication token");
            Optional<UserAuthentication> authentication = Optional.of(
                    tokenAuthenticationService.getAuthentication((HttpServletRequest) servletRequest)
            );

            authentication.ifPresent(x -> {
                x.setAuthenticated(true);
                SecurityContextHolder.getContext().setAuthentication(x);
            });

            filterChain.doFilter(servletRequest, servletResponse);
            SecurityContextHolder.getContext().setAuthentication(null);

            authentication.ifPresent(
                    x -> OUR_LOGGER.info("Authenticated request for {}", x.getName())
            );
        } catch (NullPointerException e) {
            OUR_LOGGER.error("No authentication token found", e);
            abortWithUnauthorizedResponse((HttpServletResponse) servletResponse);
        } catch (AuthenticationException | JwtException e) {
            OUR_LOGGER.error("Error processing JWT token: {}", e.getMessage(), e);
            abortWithUnauthorizedResponse((HttpServletResponse) servletResponse);
        }
    }

    private void abortWithUnauthorizedResponse(HttpServletResponse servletResponse) {
        SecurityContextHolder.clearContext();
        servletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
