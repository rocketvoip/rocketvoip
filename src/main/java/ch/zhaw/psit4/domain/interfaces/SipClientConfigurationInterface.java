package ch.zhaw.psit4.domain.interfaces;

import ch.zhaw.psit4.domain.sipclient.SipClient;

import java.util.List;

/**
 * @author Rafael Ostertag
 */
public interface SipClientConfigurationInterface {

    /**
     * Create sip client configuration according to asterisk V11 standard (chan_sip).
     *
     * @param sipClientList list of sip clients
     * @return string representing the configuration suitable for asterisk.
     */
    String generateSipClientConfiguration(List<SipClient> sipClientList);
}
