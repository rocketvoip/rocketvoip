package ch.zhaw.psit4.domain;

import ch.zhaw.psit4.domain.exceptions.InvalidConfigurationException;
import ch.zhaw.psit4.domain.interfaces.SipClientConfigurationInterface;

import java.util.LinkedList;
import java.util.List;

/**
 * SipClient configuration builder with fluent API. Output of this class is suitable for ConfigWriter.
 *
 * @author Rafael Ostertag
 */
public class SipClientConfigBuilder {
    private List<SipClientConfigurationInterface> sipClients;

    public SipClientConfigBuilder() {
        sipClients = new LinkedList<>();
    }

    /**
     * Add a SipClient.
     *
     * @param sipClient instance implementing SipClientConfigurationInterface.
     * @return SipClientConfigBuilder
     * @throws InvalidConfigurationException                       when sipClient is null.
     * @throws ch.zhaw.psit4.domain.exceptions.ValidationException when sipClient is invalid.
     */
    SipClientConfigBuilder addSipClient(SipClientConfigurationInterface sipClient) {
        if (sipClient == null) {
            throw new InvalidConfigurationException("sipClientConfigurationInerface must not be null");
        }
        sipClient.validate();
        sipClients.add(sipClient);
        return this;
    }

    /**
     * Build the configuration.
     *
     * @return list of SipClientConfigurationInstances suitable for ConfigWriter.
     * @throws InvalidConfigurationException when no sip clients have been added.
     */
    public List<SipClientConfigurationInterface> build() {
        if (sipClients.isEmpty()) {
            throw new InvalidConfigurationException("no sip clients in configuration");
        }
        return sipClients;
    }

}
