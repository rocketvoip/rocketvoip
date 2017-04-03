package ch.zhaw.psit4.helper.matchers;

import ch.zhaw.psit4.dto.CompanyDto;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Compares Company objects without taking the id into account.
 *
 * @author Jona Braun
 */
public class CompanyDtoPartialMatcher extends TypeSafeMatcher<CompanyDto> {
    private final CompanyDto expected;

    private CompanyDtoPartialMatcher(CompanyDto expected) {
        this.expected = expected;
    }

    @Factory
    public static Matcher companyDtoAlmostEqualTo(CompanyDto other) {
        return new CompanyDtoPartialMatcher(other);
    }

    @Override
    protected boolean matchesSafely(CompanyDto item) {
        return expected.getName().equals(item.getName());
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(expected);
        description.appendText("does not match");
    }
}
