package ch.zhaw.psit4.domain;

import ch.zhaw.psit4.domain.exceptions.InvalidConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Generates the sip client configuration.
 *
 * @author braunjon
 */
public class ConfigWriter {
    private static final String SIP_CLIENT_LIST_IS_EMPTY = "sipClientList is empty";
    private static final String SIP_CLIENT_LIST_IS_NULL = "sipClientList is null";
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigWriter.class);
    private static final String SIPCLIENT_TEMPLATE =
            "[%s]\n" +
                    "type=friend\n" +
                    "context=%s\n" +
                    "host=dynamic\n" +
                    "secret=%s\n";


    /**
     * Computes a list of sip clients and converts them into a configuration string.
     * The @{@link RuntimeException} @{@link InvalidConfigurationException} is thrown if the sipClientList is null or the list is empty.
     *
     * @param sipClientList a list filed with @{@link SipClient}
     * @return the configuration string for the sip clients
     */
    public String writeSipClientConfiguration(List<SipClient> sipClientList) {
        if (sipClientList == null) {
            LOGGER.error(SIP_CLIENT_LIST_IS_NULL);
            throw new InvalidConfigurationException(SIP_CLIENT_LIST_IS_NULL);
        }
        if (sipClientList.isEmpty()) {
            LOGGER.error(SIP_CLIENT_LIST_IS_EMPTY);
            throw new InvalidConfigurationException(SIP_CLIENT_LIST_IS_EMPTY);
        }

        return sipClientsToString(sipClientList);
    }

    private String sipClientsToString(List<SipClient> sipClientList) {

        StringBuilder stringBuilder = new StringBuilder();

        String sipClientUsername;
        String sipClientCompany;
        String sipClientSecret;

        String sipClientString;

        for (SipClient sipClient : sipClientList) {
            if (sipClient != null) {

                sipClientUsername = sipClient.getUsername();
                sipClientCompany = sipClient.getCompany();
                sipClientSecret = sipClient.getSecret();

                sipClientString = String.format(SIPCLIENT_TEMPLATE, sipClientUsername, sipClientCompany, sipClientSecret);

                if ((sipClientUsername != null) && (sipClientCompany != null) && (sipClientSecret != null)) {
                    stringBuilder.append(sipClientString);
                } else {
                    LOGGER.warn("at least one value of the following sipClient was null:\n" + sipClientString);
                }
            } else {
                LOGGER.warn("a sipClient is null");
            }
        }

        return stringBuilder.toString();
    }

}
