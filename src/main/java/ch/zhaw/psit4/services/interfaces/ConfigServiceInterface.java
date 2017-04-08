package ch.zhaw.psit4.services.interfaces;

import java.io.ByteArrayOutputStream;

/**
 * Puts together the asterisk configuration.
 *
 * @author Jona Braun
 */
@FunctionalInterface
public interface ConfigServiceInterface {

    /**
     * Puts together the asterisk configuration.
     *
     * @return the asterisk configuration
     */
    ByteArrayOutputStream getAsteriskConfiguration();
}
