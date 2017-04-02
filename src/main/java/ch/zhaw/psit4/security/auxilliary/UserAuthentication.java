package ch.zhaw.psit4.security.auxilliary;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author Rafael Ostertag
 */
public class UserAuthentication implements Authentication {
    private final UserDetails userDetails;
    private boolean authenticated;

    public UserAuthentication(UserDetails userDetails) {
        this.userDetails = userDetails;
        authenticated = false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userDetails.getAuthorities();
    }

    @Override
    public Object getCredentials() {
        return userDetails.getPassword();
    }

    @Override
    public UserDetails getDetails() {
        return userDetails;
    }

    @Override
    public Object getPrincipal() {
        return userDetails.getUsername();
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean b) {
        authenticated = b;
    }

    @Override
    public String getName() {
        return userDetails.getUsername();
    }
}
