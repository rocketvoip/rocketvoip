package ch.zhaw.psit4.domain.helper;

import ch.zhaw.psit4.domain.sipclient.SipClient;
import ch.zhaw.psit4.fixtures.domain.SipClientDomainGenerator;
import ch.zhaw.psit4.fixtures.general.CompanyData;
import ch.zhaw.psit4.fixtures.general.SipClientData;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper for testing the domain specific sip client configuration.
 *
 * @author Jona Braun
 */
public class SipClientTestHelper {

    /**
     * Generates a sipClientConfig with the company prefix "acme".
     *
     * @param numberOfCompanies number of Companies in the sip config
     * @param numberOfClients   number of clients per company
     * @return the generated config
     */
    public String generateSipClientConfig(int numberOfCompanies, int numberOfClients) {
        String sipClientConfig = "";
        for (int i = 1; i <= numberOfCompanies; i++) {
            sipClientConfig += generateSipClientConfig(numberOfClients, CompanyData.COMPANY_PREFIX + i);
        }
        return sipClientConfig;
    }

    /**
     * Generates the expected config string of sip clients.
     *
     * @param number  the number of sip clients the config should be created
     * @param company the company the of the sip clients
     * @return the generated config
     */
    public String generateSipClientConfig(int number, String company) {
        String config = "";
        for (int i = 1; i <= number; i++) {
            config += "[" + SipClientData.getSipClientLabel(i) + "-" + company.replaceAll(" ", "-") + "]\n" +
                    "type=friend\n" +
                    "context=" + company.replaceAll(" ", "-") + "\n" +
                    "host=dynamic\n" +
                    "secret=" + SipClientData.getSipClientSecret(i) + "\n\n";
        }
        return config;
    }

    /**
     * Generates a list of domain specific sip clients.
     *
     * @param number  the sip clients to create
     * @param company the company of the sip clients
     * @return the generated sip client list
     */
    public List<SipClient> generateSipClientList(int number, String company) {
        List<SipClient> sipClientList = new ArrayList<>();
        for (int i = 1; i <= number; i++) {
            SipClient sipClient = SipClientDomainGenerator.getSipClientDomain(company, i);
            sipClientList.add(sipClient);
        }
        return sipClientList;
    }


}
