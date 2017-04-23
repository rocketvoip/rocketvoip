package ch.zhaw.psit4.testsupport.matchers;

import ch.zhaw.psit4.dto.DialPlanDto;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Compares DialPlanDto objects without taking the id and the type specific into account.
 *
 * @author Jona Braun
 */
public class DialPlanDtoPartialMatcher extends TypeSafeMatcher<DialPlanDto> {
    private final DialPlanDto expected;

    private DialPlanDtoPartialMatcher(DialPlanDto expected) {
        this.expected = expected;
    }

    @Factory
    public static Matcher<DialPlanDto> dialPlanDtoAlmostEqualTo(DialPlanDto other) {
        return new DialPlanDtoPartialMatcher(other);
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(expected);
        description.appendText("does not match");
    }

    @Override
    protected boolean matchesSafely(DialPlanDto dialPlanDto) {
        return expected.getName().equals(dialPlanDto.getName()) &&
                expected.getPhone().equals(dialPlanDto.getPhone());
    }
}