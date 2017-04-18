package ch.zhaw.psit4.domain.interfaces;

/**
 * Puts together the asterisk sip client configuration.
 *
 * @author Rafael Ostertag
 */
public interface SipClientConfigurationInterface extends Validatable {

    /**
     * Create sip client configuration according to the asterisk drive standard.
     *
     * @return string representing the configuration suitable for asterisk.
     */
    String toSipClientConfiguration();
}
