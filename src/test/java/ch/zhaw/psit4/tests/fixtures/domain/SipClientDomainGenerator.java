package ch.zhaw.psit4.tests.fixtures.domain;

import ch.zhaw.psit4.domain.sipclient.SipClient;
import ch.zhaw.psit4.tests.fixtures.general.SipClientData;

/**
 * Create a domain sip client fixture
 *
 * @author Rafael Ostertag
 */
public final class SipClientDomainGenerator {
    private SipClientDomainGenerator() {
        // intentionally empty
    }

    /**
     * Create a domain sip client. The id will be 0 and has to be set by the caller.
     *
     * @param company name of the company
     * @param number  number of sip client
     * @return domain SipClient instance.
     */
    public static SipClient getSipClientDomain(String company, int number) {
        SipClient sipClient = new SipClient();
        sipClient.setCompany(company);
        sipClient.setPhoneNumber(SipClientData.getSipClientPhoneNumber(number));
        sipClient.setSecret(SipClientData.getSipClientSecret(number));
        sipClient.setUsername(SipClientData.getSipClientLabel(number));
        return sipClient;
    }

}
