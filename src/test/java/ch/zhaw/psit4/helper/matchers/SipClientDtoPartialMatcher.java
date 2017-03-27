package ch.zhaw.psit4.helper.matchers;

import ch.zhaw.psit4.dto.SipClientDto;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Compares SipClientDto objects without taking the id into account.
 *
 * @author Rafael Ostertag
 */
public class SipClientDtoPartialMatcher extends TypeSafeMatcher<SipClientDto> {
    private final SipClientDto expected;

    private SipClientDtoPartialMatcher(SipClientDto expected) {
        this.expected = expected;
    }

    @Factory
    public static Matcher sipClientDtoAlmostEqualTo(SipClientDto other) {
        return new SipClientDtoPartialMatcher(other);
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(expected);
        description.appendText("does not match");
    }

    @Override
    protected boolean matchesSafely(SipClientDto sipClientDto) {
        return expected.getName().equals(sipClientDto.getName()) &&
                expected.getPhone().equals(sipClientDto.getPhone()) &&
                expected.getSecret().equals(sipClientDto.getSecret());
    }
}