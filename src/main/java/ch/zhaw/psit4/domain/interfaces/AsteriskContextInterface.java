package ch.zhaw.psit4.domain.interfaces;

/**
 * Implementations provide a valid Asterisk dialplan context configuration fragment.
 *
 * @author Rafael Ostertag
 */
public interface AsteriskContextInterface extends Validatable {
    /**
     * Convert one dialplan context configuration to a valid Asterisk configuration fragment.
     * <p>
     * Implementations must guarantee, that multiple dialplan context configuration fragments can be concatenated to
     * form a valid Asterisk extension.conf
     *
     * @return configuration as string.
     */
    String toDialPlanContextConfiguration();
}
