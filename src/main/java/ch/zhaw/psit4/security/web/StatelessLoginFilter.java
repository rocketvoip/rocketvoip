package ch.zhaw.psit4.security.web;

/**
 * @author Rafael Ostertag
 */

import ch.zhaw.psit4.security.auxilliary.LoginData;
import ch.zhaw.psit4.security.auxilliary.UserAuthentication;
import ch.zhaw.psit4.security.jwt.TokenAuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class StatelessLoginFilter extends AbstractAuthenticationProcessingFilter {
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
        final LoginData loginData = OBJECT_MAPPER.readValue(httpServletRequest.getInputStream(), LoginData.class);
        final UsernamePasswordAuthenticationToken authenticationToken = loginData
                .toUsernamePasswordAuthenticationToken();

        return getAuthenticationManager().authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain
            chain, Authentication authResult) throws IOException, ServletException {
        final UserDetails authenticatedUser = userDetailsService.loadUserByUsername(authResult.getName());
        final UserAuthentication userAuthentication = new UserAuthentication(authenticatedUser);
        tokenAuthenticationService.addAuthentication(response, userAuthentication);

        userAuthentication.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(userAuthentication);
    }
}
