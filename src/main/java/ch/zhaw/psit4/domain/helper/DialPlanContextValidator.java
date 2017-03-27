package ch.zhaw.psit4.domain.helper;

import ch.zhaw.psit4.domain.dialplan.DialPlanContext;
import ch.zhaw.psit4.domain.dialplan.DialPlanExtension;
import ch.zhaw.psit4.domain.exceptions.InvalidConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Validates a DialPlanContext.
 *
 * @author Jona Braun
 */
public class DialPlanContextValidator {

    private static final String DIAL_PLAN_CONTEXT_LIST_IS_NULL = "dial context list is null";
    private static final String DIAL_PLAN_CONTEXT_LIST_IS_EMTPY = "dial context list is empty";
    private static final Logger LOGGER = LoggerFactory.getLogger(DialPlanContextValidator.class);

    /**
     * Checks the dial plan list if it is null or empty.
     *
     * @param dialPlanContextList the dial plan list to check
     * @throws InvalidConfigurationException dial plan list is null or empty
     */
    public void validateDialPlanContextList(List<DialPlanContext> dialPlanContextList) {
        if (dialPlanContextList == null) {
            LOGGER.error(DIAL_PLAN_CONTEXT_LIST_IS_NULL);
            throw new InvalidConfigurationException(DIAL_PLAN_CONTEXT_LIST_IS_NULL);
        }
        if (dialPlanContextList.isEmpty()) {
            LOGGER.error(DIAL_PLAN_CONTEXT_LIST_IS_EMTPY);
            throw new InvalidConfigurationException(DIAL_PLAN_CONTEXT_LIST_IS_EMTPY);
        }
    }


    /**
     * Checks a dial plan context for a null values.
     *
     * @param dialPlanContext the dial plan context to check
     * @return true = dial plan context is valid
     */
    public boolean isDialPlanContextValid(DialPlanContext dialPlanContext) {
        if (dialPlanContext == null) {
            LOGGER.warn("a dialPlanContext is null");
            return false;
        }
        if (dialPlanContext.getContextName() == null) {
            LOGGER.warn("The Context-Name of a dialPlanContext is null");
            return false;
        }
        for (DialPlanExtension dialPlanExtension : dialPlanContext.getDialPlanExtensionList()) {
            if (dialPlanExtension.getPhoneNumber() == null) {
                LOGGER.warn("The phone number of an extension is null");
                return false;
            }
            if (dialPlanExtension.getPriority() == null) {
                LOGGER.warn("The priority of an extension is null");
                return false;
            }
            if (dialPlanExtension.getDialPlanApplication() == null) {
                LOGGER.warn("The application of an extension is null");
                return false;
            }
            // TODO check the Application ?
        }
        return true;
    }


}
