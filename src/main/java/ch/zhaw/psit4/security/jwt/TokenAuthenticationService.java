package ch.zhaw.psit4.security.jwt;

import ch.zhaw.psit4.security.auxiliary.SecurityConstants;
import ch.zhaw.psit4.security.auxiliary.UserAuthentication;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Rafael Ostertag
 */
public class TokenAuthenticationService {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(TokenAuthenticationService.class);

    private final TokenHandler tokenHandler;

    public TokenAuthenticationService(TokenHandler tokenHandler) {
        this.tokenHandler = tokenHandler;
    }

    public String addAuthentication(HttpServletResponse response, UserAuthentication authentication) {
        final UserDetails userDetails = authentication.getDetails();
        String token = tokenHandler.createTokenForUser(userDetails);
        response.addHeader(SecurityConstants.AUTH_HEADER_NAME, token);
        LOGGER.debug("Added authentication token to headers");
        return token;
    }

    public UserAuthentication getAuthentication(HttpServletRequest request) {
        final String token = request.getHeader(SecurityConstants.AUTH_HEADER_NAME);
        if (token != null) {
            LOGGER.debug("Extracted authentication token from header {}", SecurityConstants.AUTH_HEADER_NAME);
            final UserDetails userDetails = tokenHandler.parseUserFromToken(token);
            if (userDetails != null) {
                LOGGER.debug("Read user details from token");
                return new UserAuthentication(userDetails);
            } else {
                LOGGER.warn("Unable to extract user details from token: ", token);
            }
        }
        return null;
    }
}
