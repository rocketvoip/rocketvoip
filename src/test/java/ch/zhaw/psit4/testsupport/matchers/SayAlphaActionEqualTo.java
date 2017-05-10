package ch.zhaw.psit4.testsupport.matchers;

import ch.zhaw.psit4.dto.actions.SayAlphaActionDto;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Test SayAlpha for equality.
 *
 * @author Jona Braun
 */
public class SayAlphaActionEqualTo extends TypeSafeMatcher<SayAlphaActionDto> {
    private final SayAlphaActionDto expected;

    private SayAlphaActionEqualTo(SayAlphaActionDto expected) {
        this.expected = expected;
    }

    @Factory
    public static Matcher<SayAlphaActionDto> sayAlphaActionEqualTo(SayAlphaActionDto other) {
        return new SayAlphaActionEqualTo(other);
    }

    @Override
    protected boolean matchesSafely(SayAlphaActionDto sayAlphaActionDto) {
        return expected.getSleepTime() == (sayAlphaActionDto.getSleepTime())
                && expected.getVoiceMessage().equals(sayAlphaActionDto.getVoiceMessage());

    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(expected);
        description.appendText("does not match");
    }


}
