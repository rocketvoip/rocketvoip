package ch.zhaw.psit4.domain.builders;

import ch.zhaw.psit4.domain.exceptions.InvalidConfigurationException;
import ch.zhaw.psit4.domain.interfaces.AsteriskSipClientInterface;

import java.util.LinkedList;
import java.util.List;

/**
 * SipClient configuration builder with fluent API. Output of this class is suitable for ConfigWriter.
 * <p>
 * This builder is mostly used to create the content for sip.conf
 *
 * @author Rafael Ostertag
 */
public class SipClientConfigBuilder {
    private List<AsteriskSipClientInterface> sipClients;

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
    public SipClientConfigBuilder addSipClient(AsteriskSipClientInterface sipClient) {
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
    public List<AsteriskSipClientInterface> build() {
        if (sipClients.isEmpty()) {
            throw new InvalidConfigurationException("no sip clients in configuration");
        }
        return sipClients;
    }

}
