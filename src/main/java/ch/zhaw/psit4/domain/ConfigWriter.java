package ch.zhaw.psit4.domain;

import ch.zhaw.psit4.domain.beans.DialPlanContext;
import ch.zhaw.psit4.domain.beans.SipClient;
import ch.zhaw.psit4.domain.exceptions.InvalidConfigurationException;
import ch.zhaw.psit4.domain.interfaces.DialPlanConfigurationInterface;
import ch.zhaw.psit4.domain.interfaces.SipClientConfigurationInterface;

import java.util.List;

/**
 * Generates the sip client configuration.
 *
 * @author Jona Braun
 */
public class ConfigWriter {

    private SipClientConfigurationInterface sipClientConfiguration;
    private DialPlanConfigurationInterface dialPlanConfiguration;


    public ConfigWriter(SipClientConfigurationInterface sipClientConfiguration,
                        DialPlanConfigurationInterface dialPlanConfiguration) {
        this.sipClientConfiguration = sipClientConfiguration;
        this.dialPlanConfiguration = dialPlanConfiguration;
    }

    /**
     * Processes a list of sip clients and converts them into a configuration string.
     *
     * @param sipClientList can't be null or empty
     * @return the configuration string for the sip clients
     * @throws InvalidConfigurationException if the sipClientList is null or the list is empty
     */
    public String generateSipClientConfiguration(List<SipClient> sipClientList) {
        return sipClientConfiguration.generateSipClientConfiguration(sipClientList);
    }

    /**
     * Processes a list of sip clients and DialPlan and converts them into a configuration string.
     *
     * @param dialPlanContextList can be null
     * @return the configuration string for the sip clients
     * @throws InvalidConfigurationException if the sipClientList is null or the list is empty
     */
    public String generateDialPlanConfiguration(List<DialPlanContext> dialPlanContextList) {
        return dialPlanConfiguration.generateDialPlanConfiguration(dialPlanContextList);
    }

}
