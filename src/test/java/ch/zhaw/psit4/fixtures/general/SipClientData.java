package ch.zhaw.psit4.fixtures.general;

/**
 * General methods for sip client related data.
 *
 * @author Rafael Ostertag
 */
public final class SipClientData {
    private SipClientData() {
        // intentionally empty
    }

    public static String getSipClientSecret(int number) {
        return "SipClientSecret" + number;
    }

    public static String getSipClientPhoneNumber(int number) {
        return String.format("%010d", number);
    }

    public static String getSipClientLabel(int number) {
        return "SipClientLabel" + number;
    }
}
