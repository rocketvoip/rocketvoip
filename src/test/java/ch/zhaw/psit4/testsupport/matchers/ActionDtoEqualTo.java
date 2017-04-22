package ch.zhaw.psit4.testsupport.matchers;

import ch.zhaw.psit4.dto.ActionDto;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Test ActionDto for equality without taking the type specific into account.
 *
 * @author Jona Braun
 */
public class ActionDtoEqualTo extends TypeSafeMatcher<ActionDto> {
    private final ActionDto expected;

    private ActionDtoEqualTo(ActionDto expected) {
        this.expected = expected;
    }

    @Factory
    public static Matcher<ActionDto> actionDtoEqualTo(ActionDto other) {
        return new ActionDtoEqualTo(other);
    }

    @Override
    protected boolean matchesSafely(ActionDto actionDto) {
        Matcher<ActionDto> partialMatcher = ActionDtoPartialMatcher.actionDtoAlmostEqualTo(expected);
        return partialMatcher.matches(actionDto) && expected.getId() == actionDto.getId();
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(expected);
        description.appendText("does not match");
    }


}
