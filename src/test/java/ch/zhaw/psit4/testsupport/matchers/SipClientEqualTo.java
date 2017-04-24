package ch.zhaw.psit4.testsupport.matchers;

import ch.zhaw.psit4.domain.beans.SipClient;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * @author Rafael Ostertag
 */
public class SipClientEqualTo extends TypeSafeMatcher<SipClient> {
    private SipClient expected;

    public SipClientEqualTo(SipClient expected) {
        this.expected = expected;
    }

    @Factory
    public static Matcher<SipClient> sipClientEqualTo(SipClient other) {
        return new SipClientEqualTo(other);
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