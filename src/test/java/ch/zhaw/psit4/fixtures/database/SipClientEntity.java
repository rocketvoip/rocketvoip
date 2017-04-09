package ch.zhaw.psit4.fixtures.database;

import ch.zhaw.psit4.data.jpa.entities.SipClient;

/**
 * Create SipClient entity fixtures.
 *
 * @author Rafael Ostertag
 */
public final class SipClientEntity {
    private SipClientEntity() {
        // intentionally empt
    }

    /**
     * Create a SipClient fixture. The entity returned does not have the company field set.
     *
     * @param number number of the fixture
     * @return SipClient entity instance.
     */
    public static SipClient createSipClient(int number) {
        return new SipClient(null,
                getSipClientLabel(number),
                getSipClientPhoneNumber(number),
                getSipClientSecret(number));
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
