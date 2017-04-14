package ch.zhaw.psit4.testsupport.fixtures.general;

/**
 * General methods for sip client related data.
 *
 * @author Rafael Ostertag
 */
public final class SipClientData {

    public static final String SIP_CLIENT_SECRET_PREFIX = "SipClientSecret";
    public static final String SIP_CLIENT_LABEL_PREFIX = "SipClientLabel";

    private SipClientData() {
        // intentionally empty
    }

    public static String getSipClientSecret(int number) {
        return SIP_CLIENT_SECRET_PREFIX + number;
    }

    public static String getSipClientPhoneNumber(int number) {
        return String.format("%010d", number);
    }

    public static String getSipClientLabel(int number) {
        return SIP_CLIENT_LABEL_PREFIX + number;
    }
}
