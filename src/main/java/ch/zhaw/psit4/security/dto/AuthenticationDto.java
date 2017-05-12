package ch.zhaw.psit4.security.dto;

import ch.zhaw.psit4.security.auxiliary.SecurityConstants;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Returned upon successful authentication.
 *
 * @author Rafael Ostertag
 */
@Data
public class AuthenticationDto {
    private boolean isOperator = false;

    /**
     * Create a AuthenticationDto from a UserDetails instance, based on the Authorities.
     *
     * @param userDetails UserDetail instance
     * @return AuthenticationDto instance based on the UserDetails.
     */
    public static AuthenticationDto fromUserDetails(UserDetails userDetails) {
        AuthenticationDto authenticationDto = new AuthenticationDto();
        userDetails.getAuthorities().forEach(authority -> {
            if (authority.getAuthority()
                    .equals(SecurityConstants.ROLE_PREFIX + SecurityConstants.CONFIG_ADMIN_ROLE_NAME)) {
                authenticationDto.setOperator(true);
            }
        });

        return authenticationDto;
    }
}
