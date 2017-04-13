package ch.zhaw.psit4.tests.fixtures.database;

import ch.zhaw.psit4.data.jpa.entities.SipClient;
import ch.zhaw.psit4.tests.fixtures.general.SipClientData;

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
                SipClientData.getSipClientLabel(number),
                SipClientData.getSipClientPhoneNumber(number),
                SipClientData.getSipClientSecret(number));
    }

}
