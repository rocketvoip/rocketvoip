package ch.zhaw.psit4.testsupport.matchers;

import ch.zhaw.psit4.dto.AdminDto;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Tests AdminDtos for equality.
 *
 * @author Jona Braun
 */
public class AdminDtoEqualTo extends TypeSafeMatcher<AdminDto> {
    private final AdminDto expected;

    private AdminDtoEqualTo(AdminDto expected) {
        this.expected = expected;
    }

    @Factory
    public static Matcher<AdminDto> adminDtoEqualTo(AdminDto other) {
        return new AdminDtoEqualTo(other);
    }

    @Override
    protected boolean matchesSafely(AdminDto adminDto) {
        Matcher<AdminDto> partialMatcher = AdminDtoPartialMatcher.adminDtoAlmostEqualTo(expected);
        return partialMatcher.matches(adminDto) && expected.getId() == adminDto.getId();
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(expected);
        description.appendText("does not match");
    }
}
