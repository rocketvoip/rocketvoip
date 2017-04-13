package ch.zhaw.psit4.tests.mocks;

import ch.zhaw.psit4.data.jpa.entities.Admin;
import ch.zhaw.psit4.security.auxiliary.AdminDetails;
import ch.zhaw.psit4.security.auxiliary.UserDetailsServiceImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Rafael Ostertag
 */
public final class UserDetailsServiceMock {

    public static final String NON_EXISTING_USER = "doesnotexist";

    private UserDetailsServiceMock() {
        // intentionally empty
    }

    /**
     * Mock UserDetailsService. UserDetailsService.loadUserByUsername() returns a AdminDetails instance when called
     * with a username == admin.getUsername(). When called with username == "doesnotexist", it throws a
     * UsernameNotFoundException().
     *
     * @param admin Admin entity used to create AdminDetails.
     * @return Mocked UserDetailsServiceImpl instance.
     */
    public static UserDetailsService makeMockForAdmin(Admin admin) {
        UserDetailsService userDetailsService = mock(UserDetailsServiceImpl.class);
        when(userDetailsService.loadUserByUsername(admin.getUsername())).thenReturn(new AdminDetails(admin));
        when(userDetailsService.loadUserByUsername(NON_EXISTING_USER)).thenThrow(new UsernameNotFoundException("User " +
                "not found"));

        return userDetailsService;
    }
}
