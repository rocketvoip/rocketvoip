package ch.zhaw.psit4.testsupport.matchers;

import ch.zhaw.psit4.security.auxiliary.AdminDetails;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * @author Rafael Ostertag
 */
public class AdminDetailsEqualTo extends TypeSafeMatcher<AdminDetails> {
    private final AdminDetails expected;

    public AdminDetailsEqualTo(AdminDetails other) {
        this.expected = other;
    }

    @Factory
    public static Matcher<AdminDetails> adminDetailsEqualTo(AdminDetails other) {
        return new AdminDetailsEqualTo(other);
    }

    @Override
    protected boolean matchesSafely(AdminDetails adminDetails) {
        return adminDetails.isAccountNonExpired() == expected.isAccountNonExpired() &&
                adminDetails.isAccountNonLocked() == expected.isAccountNonLocked() &&
                adminDetails.isCredentialsNonExpired() == expected.isCredentialsNonExpired() &&
                adminDetails.isEnabled() == expected.isEnabled() &&
                adminDetails.getAuthorities().equals(expected.getAuthorities()) &&
                adminDetails.getFirstname().equals(expected.getFirstname()) &&
                adminDetails.getLastname().equals(expected.getLastname()) &&
                adminDetails.getUsername().equals(expected.getUsername()) &&
                adminDetails.getPassword().equals(expected.getPassword()) &&
                adminDetails.isSuperAdmin() == expected.isSuperAdmin();
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(expected);
        description.appendText("does not match");
    }
}
