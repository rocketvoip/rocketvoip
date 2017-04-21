package ch.zhaw.psit4.domain;

import ch.zhaw.psit4.domain.exceptions.InvalidConfigurationException;
import ch.zhaw.psit4.domain.interfaces.DialPlanContextConfigurationInterface;
import ch.zhaw.psit4.domain.interfaces.SipClientConfigurationInterface;

import java.util.List;
import java.util.Optional;

/**
 * Create the string representation of sip.conf and extension.conf. Method may use the output of the various builder
 * classes.
 *
 * Content for sip.conf is best created by using SipClientConfigBuilder. Stock extension.conf content is created
 * using the CompanyDialPlanBuilder. For more sophisticated extension configuration, use the builders derived from
 * DialPlanConfigBuilder.
 *
 * @author Jona Braun
 */
public final class ConfigWriter {

    private ConfigWriter() {
        // Intentionally empty
    }

    /**
     * Convert a list of SipClients to content suitable for sip.conf.
     *
     * @param sipClientList list of SipClients
     * @return string suitable for sip.conf
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
     * Convert a list of DialPlanContexts to content suitable for extension.conf.
     *
     * @param dialPlanContextList list of DialPlanContexts.
     * @return string suitable for extension.conf
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
