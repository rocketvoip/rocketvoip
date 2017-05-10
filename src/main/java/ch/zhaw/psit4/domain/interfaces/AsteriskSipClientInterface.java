package ch.zhaw.psit4.domain.interfaces;

/**
 * Implementations provides a valid Asterisk drive configuration.
 *
 * @author Rafael Ostertag
 */
public interface AsteriskSipClientInterface extends Validatable {

    /**
     * Create sip client configuration according to the asterisk drive standard.
     *
     * @return string representing the configuration suitable for asterisk.
     */
    String toSipClientConfiguration();
}
