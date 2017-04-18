package ch.zhaw.psit4.domain.sipclient;

import ch.zhaw.psit4.domain.beans.SipClient;
import ch.zhaw.psit4.domain.exceptions.InvalidConfigurationException;
import ch.zhaw.psit4.domain.interfaces.SipClientConfigurationInterface;

import java.util.List;

/**
 * Generates the SIP client configuration according to the asterisk chanel driver chan_sip.
 * One sip client in the asterisk sip.conf file has following structure:<br><br>
 * <code>
 * [sip-phone1]<br>
 * type=friend<br>
 * context=company1<br>
 * host=dynamic<br>
 * secret=Test-Pass<br>
 * </code>
 *
 * @author Rafael Ostertag
 */
public class SipClientConfigurationChanSip implements SipClientConfigurationInterface {
    /**
     * @inheritDoc
     */
    @Override
    public String toSipClientConfiguration(List<SipClient> sipClientList) {
        if (sipClientList == null) {
            throw new InvalidConfigurationException("sipClientList is null");
        }
        if (sipClientList.isEmpty()) {
            throw new InvalidConfigurationException("sipClientList is empty");
        }
        return sipClientsToString(sipClientList);
    }

    private String sipClientsToString(List<SipClient> sipClientList) {

        StringBuilder stringBuilder = new StringBuilder();

        for (SipClient sipClient : sipClientList) {
            if (sipClient == null) {
                continue;
            }
            sipClient.validate();
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


}
