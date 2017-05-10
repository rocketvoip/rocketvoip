package ch.zhaw.psit4.domain.interfaces;

/**
 * Implementations provide a valid Asterisk dialplan application call configuration string.
 *
 * @author Jona Braun
 */
public interface AsteriskApplicationInterface extends Validatable {

    /**
     * Convert one Asterisk dialplan application configuration to a string suitable for concatenation with one
     * Asterisk dialplan extension.
     *
     * The application call configuration string must not end with a newline character ('\n').
     *
     * @return string representing the asterisk application call fragment.
     */
    String toApplicationCall();

    /**
     * Indicate whether or not the application requires an Asterisk Answer() action.
     *
     * @return {@code true} the context requires an Answer() action. {@code false} otherwise.
     */
    boolean requireAnswer();

    /**
     * Indicate whether or not the application requires an Asterisk WaitExten() action.
     *
     * @return {@code true} the context requires an WaitExten() action. {@code false} otherwise.
     */
    boolean requireWaitExten();

}
