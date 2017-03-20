package ch.zhaw.psit4.domain;

import ch.zhaw.psit4.domain.exceptions.InvalidConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Generates the sip client configuration.
 *
 * @author Jona Braun
 */
public class ConfigWriter {
    private static final String SIP_CLIENT_LIST_IS_EMPTY = "sipClientList is empty";
    private static final String SIP_CLIENT_LIST_IS_NULL = "sipClientList is null";
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigWriter.class);

    /**
     * Processes a list of sip clients and converts them into a configuration string.
     *
     * @param sipClientList can't be null or empty
     * @return the configuration string for the sip clients
     * @throws InvalidConfigurationException if the sipClientList is null or the list is empty
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

        for (SipClient sipClient : sipClientList) {
            if (!isSipClientValid(sipClient)) {
                continue;
            }
            stringBuilder.append("[");
            stringBuilder.append(sipClient.getUsername());
            stringBuilder.append("]\n");
            stringBuilder.append("type=friend\n");
            stringBuilder.append("context=");
            stringBuilder.append(sipClient.getCompany());
            stringBuilder.append("\n");
            stringBuilder.append("host=dynamic\n");
            stringBuilder.append("secret=");
            stringBuilder.append(sipClient.getSecret());
            stringBuilder.append("\n\n");

        }

        return stringBuilder.toString();
    }

    private boolean isSipClientValid(SipClient sipClient) {
        if (sipClient == null) {
            LOGGER.warn("a sipClient is null");
            return false;
        }
        if (sipClient.getUsername() == null) {
            LOGGER.warn("Username of a sipClient was null");
            return false;
        }
        if (sipClient.getCompany() == null) {
            LOGGER.warn("Company of a sipClient was null");
            return false;
        }
        if (sipClient.getSecret() == null) {
            LOGGER.warn("Secret of a sipClient was null");
            return false;
        }
        return true;
    }

}
