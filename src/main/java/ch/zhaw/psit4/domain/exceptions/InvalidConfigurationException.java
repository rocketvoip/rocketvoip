package ch.zhaw.psit4.domain.exceptions;

/**
 * Thrown upon invalid configuration.
 *
 * @author Jona Braun
 */
public class InvalidConfigurationException extends RuntimeException {

    public InvalidConfigurationException(String message) {
        super(message);
    }
}
