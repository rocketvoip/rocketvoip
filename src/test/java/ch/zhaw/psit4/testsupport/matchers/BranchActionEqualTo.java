package ch.zhaw.psit4.testsupport.matchers;

import ch.zhaw.psit4.dto.actions.BranchActionDto;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Test Branch for equality.
 *
 * @author Jona Braun
 */
public class BranchActionEqualTo extends TypeSafeMatcher<BranchActionDto> {
    private final BranchActionDto expected;

    private BranchActionEqualTo(BranchActionDto expected) {
        this.expected = expected;
    }

    @Factory
    public static Matcher<BranchActionDto> branchActionEqualTo(BranchActionDto other) {
        return new BranchActionEqualTo(other);
    }

    @Override
    protected boolean matchesSafely(BranchActionDto branchActionDto) {
        int i = 0;
        for (Long dialPlanId : expected.getNextDialPlanIds()) {
            if (!dialPlanId.equals(branchActionDto.getNextDialPlanIds().get(i))) {
                return false;
            }
            i++;
        }
        return expected.getHangupTime() == (branchActionDto.getHangupTime());
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(expected);
        description.appendText("does not match");
    }


}
