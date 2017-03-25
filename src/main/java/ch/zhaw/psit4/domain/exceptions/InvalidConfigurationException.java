package ch.zhaw.psit4.domain.exceptions;

/**
 * It is thrown if invalid parameters are passed to the @{@link ch.zhaw.psit4.domain.ConfigWriter}.
 *
 * @author Jona Braun
 */
public class InvalidConfigurationException extends RuntimeException {

    public InvalidConfigurationException(String message) {
        super(message);
    }

}
