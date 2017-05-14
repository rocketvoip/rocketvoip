package ch.zhaw.psit4.testsupport.matchers;

import ch.zhaw.psit4.dto.AdminDto;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Compares AdminDto objects without taking the id into account.
 *
 * @author Jona Braun
 */
public class AdminDtoPartialMatcher extends TypeSafeMatcher<AdminDto> {
    private final AdminDto expected;

    private AdminDtoPartialMatcher(AdminDto expected) {
        this.expected = expected;
    }

    @Factory
    public static Matcher<AdminDto> adminDtoAlmostEqualTo(AdminDto other) {
        return new AdminDtoPartialMatcher(other);
    }

    @Override
    protected boolean matchesSafely(AdminDto item) {
        return expected.getFirstName().equals(item.getFirstName())
                && expected.getLastName().equals(item.getLastName())
                && expected.getUserName().equals(item.getUserName())
                && expected.getPassword().equals(item.getPassword())
                && expected.getCompanyDtoList().containsAll(item.getCompanyDtoList());
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(expected);
        description.appendText("does not match");
    }
}
