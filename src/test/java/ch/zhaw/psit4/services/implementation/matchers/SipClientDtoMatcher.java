package ch.zhaw.psit4.services.implementation.matchers;

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
public class SipClientDtoMatcher extends TypeSafeMatcher<SipClientDto> {
    SipClientDto expected;

    private SipClientDtoMatcher(SipClientDto expected) {
        this.expected = expected;
    }

    @Factory
    public static Matcher almostEqualTo(SipClientDto other) {
        return new SipClientDtoMatcher(other);
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