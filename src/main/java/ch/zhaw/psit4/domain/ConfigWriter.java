package ch.zhaw.psit4.domain;

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
public final class ConfigWriter {

    private ConfigWriter() {
        // Intentionally empty
    }

    /**
     * Processes a list of sip clients and converts them into a configuration string.
     *
     * @param sipClientList can't be null or empty
     * @return the configuration string for the sip clients
     * @throws InvalidConfigurationException if the sipClientList is null or the list is empty
     */
    public static String generateSipClientConfiguration(List<? extends SipClientConfigurationInterface> sipClientList) {
        if (sipClientList == null) {
            throw new InvalidConfigurationException("sipClientList is null");
        }

        if (sipClientList.isEmpty()) {
            throw new InvalidConfigurationException("sipClientList is empty");
        }
        StringBuilder stringBuilder = new StringBuilder();
        sipClientList.forEach(x ->
                Optional
                        .ofNullable(x)
                        .ifPresent(y -> {
                                    y.validate();
                                    stringBuilder.append(y.toSipClientConfiguration());
                                }
                        )
        );
        return stringBuilder.toString();
    }

    /**
     * Processes a list of sip clients and DialPlan and converts them into a configuration string.
     *
     * @param dialPlanContextList can be null
     * @return the configuration string for the sip clients
     * @throws InvalidConfigurationException if the sipClientList is null or the list is empty
     */
    public static String generateDialPlanConfiguration(List<? extends DialPlanContextConfigurationInterface>
                                                               dialPlanContextList) {
        if (dialPlanContextList == null) {
            throw new InvalidConfigurationException("dialPlanContextList is null");
        }

        if (dialPlanContextList.isEmpty()) {
            throw new InvalidConfigurationException("dialPlanContextList is empty");
        }
        StringBuilder stringBuilder = new StringBuilder();

        dialPlanContextList.forEach(x ->
                Optional
                        .ofNullable(x)
                        .ifPresent(y -> {
                                    y.validate();
                                    stringBuilder.append(y.toDialPlanContextConfiguration());
                                }
                        )
        );

        return stringBuilder.toString();
    }

}
