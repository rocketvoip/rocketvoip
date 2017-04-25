package ch.zhaw.psit4.testsupport.matchers;

import ch.zhaw.psit4.dto.actions.GotoAction;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Test Goto for equality.
 *
 * @author Jona Braun
 */
public class GotoActionEqualTo extends TypeSafeMatcher<GotoAction> {
    private final GotoAction expected;

    private GotoActionEqualTo(GotoAction expected) {
        this.expected = expected;
    }

    @Factory
    public static Matcher<GotoAction> gotoActionEqualTo(GotoAction other) {
        return new GotoActionEqualTo(other);
    }

    @Override
    protected boolean matchesSafely(GotoAction gotoAction) {
        return expected.getNextDialPlanId() == (gotoAction.getNextDialPlanId());

    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(expected);
        description.appendText("does not match");
    }


}
