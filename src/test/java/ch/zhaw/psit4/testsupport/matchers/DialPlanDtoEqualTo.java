package ch.zhaw.psit4.testsupport.matchers;

import ch.zhaw.psit4.dto.DialPlanDto;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * @author Jona Braun
 */
public class DialPlanDtoEqualTo extends TypeSafeMatcher<DialPlanDto> {
    private final DialPlanDto expected;

    private DialPlanDtoEqualTo(DialPlanDto expected) {
        this.expected = expected;
    }

    @Factory
    public static Matcher<DialPlanDto> dialPlanDtoEqualTo(DialPlanDto other) {
        return new DialPlanDtoEqualTo(other);
    }

    @Override
    protected boolean matchesSafely(DialPlanDto dialPlanDto) {
        Matcher<DialPlanDto> partialMatcher = DialPlanDtoPartialMatcher.dialPlanDtoAlmostEqualTo(expected);
        return partialMatcher.matches(dialPlanDto) && expected.getId() == dialPlanDto.getId();
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(expected);
        description.appendText("does not match");
    }
}
