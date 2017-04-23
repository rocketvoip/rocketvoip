package ch.zhaw.psit4.testsupport.matchers;

import ch.zhaw.psit4.dto.actions.SayAlphaAction;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Test SayAlpha for equality.
 *
 * @author Jona Braun
 */
public class SayAlphaActionEqualTo extends TypeSafeMatcher<SayAlphaAction> {
    private final SayAlphaAction expected;

    private SayAlphaActionEqualTo(SayAlphaAction expected) {
        this.expected = expected;
    }

    @Factory
    public static Matcher<SayAlphaAction> sayAlphaActionEqualTo(SayAlphaAction other) {
        return new SayAlphaActionEqualTo(other);
    }

    @Override
    protected boolean matchesSafely(SayAlphaAction sayAlphaAction) {
        return expected.getSleepTime() == (sayAlphaAction.getSleepTime())
                && expected.getVoiceMessage().equals(sayAlphaAction.getVoiceMessage());

    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(expected);
        description.appendText("does not match");
    }


}
