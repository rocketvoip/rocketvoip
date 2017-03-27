package ch.zhaw.psit4.domain.sipclient;

import ch.zhaw.psit4.domain.helper.SipClientValidator;
import ch.zhaw.psit4.domain.interfaces.SipClientConfigurationInterface;

import java.util.List;

/**
 * Generates the SIP client configuration according to the asterisk chanel driver chan_sip.
 *
 * @author Rafael Ostertag
 */
public class SipClientConfigurationChanSip implements SipClientConfigurationInterface {
    private final SipClientValidator sipClientValidator = new SipClientValidator();

    /**
     * @inheritDoc
     */
    @Override
    public String generateSipClientConfiguration(List<SipClient> sipClientList) {
        sipClientValidator.validateSipClientList(sipClientList);

        return sipClientsToString(sipClientList);
    }

    private String sipClientsToString(List<SipClient> sipClientList) {

        StringBuilder stringBuilder = new StringBuilder();

        for (SipClient sipClient : sipClientList) {
            if (!sipClientValidator.isSipClientValid(sipClient)) {
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


}
