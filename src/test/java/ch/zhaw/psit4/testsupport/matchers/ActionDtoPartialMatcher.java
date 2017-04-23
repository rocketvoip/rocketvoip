package ch.zhaw.psit4.testsupport.matchers;

import ch.zhaw.psit4.dto.ActionDto;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Compares ActionDto objects without taking the id and the type specific into account.
 *
 * @author Jona Braun
 */
public class ActionDtoPartialMatcher extends TypeSafeMatcher<ActionDto> {
    private final ActionDto expected;

    private ActionDtoPartialMatcher(ActionDto expected) {
        this.expected = expected;
    }

    @Factory
    public static Matcher<ActionDto> actionDtoAlmostEqualTo(ActionDto other) {
        return new ActionDtoPartialMatcher(other);
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(expected);
        description.appendText("does not match");
    }

    @Override
    protected boolean matchesSafely(ActionDto actionDto) {
        return expected.getName().equals(actionDto.getName()) &&
                expected.getType().equals(actionDto.getType());
    }
}