package ch.zhaw.psit4.testsupport.matchers;

import ch.zhaw.psit4.dto.SipClientDto;
import ch.zhaw.psit4.dto.actions.DialAction;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static ch.zhaw.psit4.testsupport.matchers.SipClientDtoPartialMatcher.sipClientDtoAlmostEqualTo;

/**
 * Test DialAction for equality.
 *
 * @author Jona Braun
 */
public class DialActionEqualTo extends TypeSafeMatcher<DialAction> {
    private final DialAction expected;

    private DialActionEqualTo(DialAction expected) {
        this.expected = expected;
    }

    @Factory
    public static Matcher<DialAction> dialActionEqualTo(DialAction other) {
        return new DialActionEqualTo(other);
    }

    @Override
    protected boolean matchesSafely(DialAction dialAction) {
        int i = 0;
        for (SipClientDto expectedSipClient : expected.getSipClients()) {
            Matcher<SipClientDto> partialMatcher = sipClientDtoAlmostEqualTo(expectedSipClient);
            if (!partialMatcher.matches(dialAction.getSipClients().get(i))) {
                return false;
            }
            i++;
        }

        return expected.getRingingTime().equals(dialAction.getRingingTime());

    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(expected);
        description.appendText("does not match");
    }


}
