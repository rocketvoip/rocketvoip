package ch.zhaw.psit4.domain;

import ch.zhaw.psit4.domain.exceptions.InvalidConfigurationException;
import ch.zhaw.psit4.domain.interfaces.SipClientConfigurationInterface;

import java.util.List;

/**
 * Generates the sip client configuration.
 *
 * @author Jona Braun
 */
public class ConfigWriter {

    private SipClientConfigurationInterface sipClientConfigurationInterface;

    public ConfigWriter(SipClientConfigurationInterface sipClientConfigurationInterface) {
        this.sipClientConfigurationInterface = sipClientConfigurationInterface;
    }

    /**
     * Processes a list of sip clients and converts them into a configuration string.
     *
     * @param sipClientList can't be null or empty
     * @return the configuration string for the sip clients
     * @throws InvalidConfigurationException if the sipClientList is null or the list is empty
     */
    public String generateSipClientConfiguration(List<SipClient> sipClientList) {
        return sipClientConfigurationInterface.generateSipClientConfiguration(sipClientList);
    }

}
