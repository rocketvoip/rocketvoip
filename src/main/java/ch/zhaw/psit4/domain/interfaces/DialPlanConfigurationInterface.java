package ch.zhaw.psit4.domain.interfaces;

import ch.zhaw.psit4.domain.dialplan.DialPlanContext;
import ch.zhaw.psit4.domain.exceptions.InvalidConfigurationException;

import java.util.List;

/**
 * Puts together the dial plan of asterisk.
 *
 * @author Jona Braun
 */
@FunctionalInterface
public interface DialPlanConfigurationInterface {


    /**
     * Creates the dial plan configuration according to asterisk driver standard.
     *
     * @param dialPlanContextList all dial plan contexts
     * @return the string representing the extension.conf of asterisk
     * @throws InvalidConfigurationException if the dialPlanContext ist null or empty
     */
    String generateDialPlanConfiguration(List<DialPlanContext> dialPlanContextList);

}
