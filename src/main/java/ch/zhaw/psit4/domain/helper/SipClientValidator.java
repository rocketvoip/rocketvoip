package ch.zhaw.psit4.domain.helper;

import ch.zhaw.psit4.domain.exceptions.InvalidConfigurationException;
import ch.zhaw.psit4.domain.sipclient.SipClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Checks if a sip client is valid for further processing.
 *
 * @author Jona Braun
 */
public class SipClientValidator {
    private static final String SIP_CLIENT_LIST_IS_EMPTY = "sipClientList is empty";
    private static final String SIP_CLIENT_LIST_IS_NULL = "sipClientList is null";
    private static final Logger LOGGER = LoggerFactory.getLogger(SipClientValidator.class);

    /**
     * Checks the sip client list if it is null or empty.
     *
     * @param sipClientList the sip client list to check
     * @throws InvalidConfigurationException sip client list is null or empty
     */
    public void validateSipClientList(List<SipClient> sipClientList) {
        if (sipClientList == null) {
            LOGGER.error(SIP_CLIENT_LIST_IS_NULL);
            throw new InvalidConfigurationException(SIP_CLIENT_LIST_IS_NULL);
        }
        if (sipClientList.isEmpty()) {
            LOGGER.error(SIP_CLIENT_LIST_IS_EMPTY);
            throw new InvalidConfigurationException(SIP_CLIENT_LIST_IS_EMPTY);
        }

    }

    /**
     * Checks one sip client for a null values.
     *
     * @param sipClient the sip client to check
     * @return true = sip is valid
     */
    public boolean isSipClientValid(SipClient sipClient) {
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
        if (sipClient.getPhoneNumber() == null) {
            LOGGER.warn("Phone number of a sipClient was null");
            return false;
        }
        return true;
    }

}
