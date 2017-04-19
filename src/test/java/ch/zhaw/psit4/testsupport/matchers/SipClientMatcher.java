package ch.zhaw.psit4.testsupport.matchers;

import ch.zhaw.psit4.domain.beans.SipClient;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * @author Rafael Ostertag
 */
public class SipClientMatcher extends TypeSafeMatcher<SipClient> {
    private SipClient expected;

    public SipClientMatcher(SipClient expected) {
        this.expected = expected;
    }

    @Factory
    public static Matcher<SipClient> sipClientMatcher(SipClient other) {
        return new SipClientMatcher(other);
    }

    @Override
    protected boolean matchesSafely(SipClient sipClient) {
        return sipClient.getCompany().equals(expected.getCompany()) &&
                sipClient.getId() == expected.getId() &&
                sipClient.getLabel().equals(expected.getLabel()) &&
                sipClient.getPhoneNumber().equals(expected.getPhoneNumber()) &&
                sipClient.getSecret().equals(expected.getSecret()) &&
                sipClient.getUsername().equals(expected.getUsername());
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(expected);
        description.appendText("does not match");
    }
}
