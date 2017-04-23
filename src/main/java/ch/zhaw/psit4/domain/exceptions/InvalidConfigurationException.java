package ch.zhaw.psit4.domain.exceptions;

/**
 * Thrown upon invalid configuration.
 *
 * @author Jona Braun
 */
public class InvalidConfigurationException extends RuntimeException {

    public InvalidConfigurationException() {
        super();
    }

    public InvalidConfigurationException(String message) {
        super(message);
    }

    public InvalidConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidConfigurationException(Throwable cause) {
        super(cause);
    }

    protected InvalidConfigurationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
