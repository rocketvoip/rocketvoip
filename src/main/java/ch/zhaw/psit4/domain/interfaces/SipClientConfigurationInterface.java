package ch.zhaw.psit4.domain.interfaces;

import ch.zhaw.psit4.domain.beans.SipClient;

import java.util.List;

/**
 * Puts together the asterisk sip client configuration.
 *
 * @author Rafael Ostertag
 */
@FunctionalInterface
public interface SipClientConfigurationInterface {

    /**
     * Create sip client configuration according to the asterisk drive standard.
     *
     * @param sipClientList list of sip clients
     * @return string representing the configuration suitable for asterisk.
     */
    String toSipClientConfiguration(List<SipClient> sipClientList);
}
