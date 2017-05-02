package ch.zhaw.psit4.testsupport.matchers;

import ch.zhaw.psit4.dto.actions.GotoActionDto;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Test Goto for equality.
 *
 * @author Jona Braun
 */
public class GotoActionEqualTo extends TypeSafeMatcher<GotoActionDto> {
    private final GotoActionDto expected;

    private GotoActionEqualTo(GotoActionDto expected) {
        this.expected = expected;
    }

    @Factory
    public static Matcher<GotoActionDto> gotoActionEqualTo(GotoActionDto other) {
        return new GotoActionEqualTo(other);
    }

    @Override
    protected boolean matchesSafely(GotoActionDto gotoActionDto) {
        return expected.getNextDialPlanId() == (gotoActionDto.getNextDialPlanId());

    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(expected);
        description.appendText("does not match");
    }


}
