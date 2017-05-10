package ch.zhaw.psit4.testsupport.matchers;

import ch.zhaw.psit4.dto.SipClientDto;
import ch.zhaw.psit4.dto.actions.DialActionDto;
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
public class DialActionEqualTo extends TypeSafeMatcher<DialActionDto> {
    private final DialActionDto expected;

    private DialActionEqualTo(DialActionDto expected) {
        this.expected = expected;
    }

    @Factory
    public static Matcher<DialActionDto> dialActionEqualTo(DialActionDto other) {
        return new DialActionEqualTo(other);
    }

    @Override
    protected boolean matchesSafely(DialActionDto dialActionDto) {
        int i = 0;
        for (SipClientDto expectedSipClient : expected.getSipClients()) {
            Matcher<SipClientDto> partialMatcher = sipClientDtoAlmostEqualTo(expectedSipClient);
            if (!partialMatcher.matches(dialActionDto.getSipClients().get(i))) {
                return false;
            }
            i++;
        }

        return expected.getRingingTime() == dialActionDto.getRingingTime();

    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(expected);
        description.appendText("does not match");
    }


}
