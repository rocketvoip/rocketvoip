package ch.zhaw.psit4.domain;

import ch.zhaw.psit4.domain.beans.SipClient;
import ch.zhaw.psit4.domain.exceptions.InvalidConfigurationException;
import ch.zhaw.psit4.domain.interfaces.DialPlanContextConfigurationInterface;
import ch.zhaw.psit4.domain.interfaces.SipClientConfigurationInterface;

import java.util.List;
import java.util.Optional;

/**
 * Generates the sip client configuration.
 *
 * @author Jona Braun
 */
public class ConfigWriter {

    private SipClientConfigurationInterface sipClientConfiguration;


    public ConfigWriter(SipClientConfigurationInterface sipClientConfiguration) {
        this.sipClientConfiguration = sipClientConfiguration;
    }

    /**
     * Processes a list of sip clients and converts them into a configuration string.
     *
     * @param sipClientList can't be null or empty
     * @return the configuration string for the sip clients
     * @throws InvalidConfigurationException if the sipClientList is null or the list is empty
     */
    public String generateSipClientConfiguration(List<SipClient> sipClientList) {
        return sipClientConfiguration.toSipClientConfiguration(sipClientList);
    }

    /**
     * Processes a list of sip clients and DialPlan and converts them into a configuration string.
     *
     * @param dialPlanContextList can be null
     * @return the configuration string for the sip clients
     * @throws InvalidConfigurationException if the sipClientList is null or the list is empty
     */
    public String generateDialPlanConfiguration(List<DialPlanContextConfigurationInterface>
                                                        dialPlanContextList) {
        StringBuilder stringBuilder = new StringBuilder();

        dialPlanContextList.forEach(x ->
                Optional
                        .ofNullable(x)
                        .ifPresent(y ->
                                stringBuilder.append(y.toDialPlanContextConfiguration())
                        )
        );

        return stringBuilder.toString();
    }

}
