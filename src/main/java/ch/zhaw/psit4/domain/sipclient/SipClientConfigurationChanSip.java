package ch.zhaw.psit4.domain.sipclient;

import ch.zhaw.psit4.domain.exceptions.InvalidConfigurationException;
import ch.zhaw.psit4.domain.interfaces.SipClientConfigurationInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Generates the SIP client configuration according to the asterisk chanel driver chan_sip.
 *
 * @author Rafael Ostertag
 */
public class SipClientConfigurationChanSip implements SipClientConfigurationInterface {
    private static final String SIP_CLIENT_LIST_IS_EMPTY = "sipClientList is empty";
    private static final String SIP_CLIENT_LIST_IS_NULL = "sipClientList is null";
    private static final Logger LOGGER = LoggerFactory.getLogger(SipClientConfigurationChanSip.class);

    /**
     * @inheritDoc
     */
    @Override
    public String generateSipClientConfiguration(List<SipClient> sipClientList) {
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
            stringBuilder.append(sipClient.getLabel());
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
