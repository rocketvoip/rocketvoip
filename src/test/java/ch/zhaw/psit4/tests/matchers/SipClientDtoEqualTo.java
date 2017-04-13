package ch.zhaw.psit4.tests.matchers;

import ch.zhaw.psit4.dto.SipClientDto;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Test SipClientDto for equality.
 *
 * @author Rafael Ostertag
 */
public class SipClientDtoEqualTo extends TypeSafeMatcher<SipClientDto> {
    private final SipClientDto expected;

    private SipClientDtoEqualTo(SipClientDto expected) {
        this.expected = expected;
    }

    @Factory
    public static Matcher<SipClientDto> sipClientDtoEqualTo(SipClientDto other) {
        return new SipClientDtoEqualTo(other);
    }

    @Override
    protected boolean matchesSafely(SipClientDto sipClientDto) {
        Matcher<SipClientDto> partialMatcher = SipClientDtoPartialMatcher.sipClientDtoAlmostEqualTo(sipClientDto);
        return partialMatcher.matches(sipClientDto) && expected.getId() == sipClientDto.getId();
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(expected);
        description.appendText("does not match");
    }


}
