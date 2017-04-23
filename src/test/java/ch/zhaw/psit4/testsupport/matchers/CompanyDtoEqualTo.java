package ch.zhaw.psit4.testsupport.matchers;

import ch.zhaw.psit4.dto.CompanyDto;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Tests Companies for equality.
 *
 * @author Jona Braun
 */
public class CompanyDtoEqualTo extends TypeSafeMatcher<CompanyDto> {
    private final CompanyDto expected;

    private CompanyDtoEqualTo(CompanyDto expected) {
        this.expected = expected;
    }

    @Factory
    public static Matcher<CompanyDto> companyDtoEqualTo(CompanyDto other) {
        return new CompanyDtoEqualTo(other);
    }

    @Override
    protected boolean matchesSafely(CompanyDto companyDto) {
        Matcher<CompanyDto> partialMatcher = CompanyDtoPartialMatcher.companyDtoAlmostEqualTo(expected);
        return partialMatcher.matches(companyDto) && expected.getId() == companyDto.getId();
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(expected);
        description.appendText("does not match");
    }
}
