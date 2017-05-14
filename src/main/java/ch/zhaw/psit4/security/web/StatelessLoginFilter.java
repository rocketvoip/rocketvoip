package ch.zhaw.psit4.security.web;

import ch.zhaw.psit4.security.auxiliary.LoginDataDto;
import ch.zhaw.psit4.security.auxiliary.UserAuthentication;
import ch.zhaw.psit4.security.dto.AuthenticationDto;
import ch.zhaw.psit4.security.jwt.TokenAuthenticationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
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
        final LoginDataDto loginDataDto = OBJECT_MAPPER.readValue(httpServletRequest.getInputStream(), LoginDataDto
                .class);
        final UsernamePasswordAuthenticationToken authenticationToken = loginDataDto
                .toUsernamePasswordAuthenticationToken();

        OUR_LOGGER.debug("Trying to authenticate user '{}'", loginDataDto.getUsername());
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

        populateResponseWithInformation(response, authenticatedUser);

        OUR_LOGGER.info("Successfully authenticated '{}'", authenticatedUser.getUsername());
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
