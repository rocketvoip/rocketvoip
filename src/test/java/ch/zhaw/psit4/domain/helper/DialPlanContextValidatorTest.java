package ch.zhaw.psit4.domain.helper;

import ch.zhaw.psit4.domain.dialplan.DialPlanContext;
import ch.zhaw.psit4.domain.dialplan.DialPlanExtension;
import ch.zhaw.psit4.domain.exceptions.InvalidConfigurationException;
import ch.zhaw.psit4.testsupport.fixtures.domain.DialPlanGenerator;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Jona Braun
 */
public class DialPlanContextValidatorTest {
    private DialPlanContext dialPlanContext;
    private DialPlanContextValidator dialPlanValidator;

    @Before
    public void setUp() throws Exception {
        dialPlanValidator = new DialPlanContextValidator();
        dialPlanContext = new DialPlanContext();
        generateDialPlanContext();
    }

    @Test(expected = InvalidConfigurationException.class)
    public void validateDialPlanContextListNull() throws Exception {
        dialPlanValidator.validateDialPlanContextList(null);
    }

    @Test(expected = InvalidConfigurationException.class)
    public void validateDialPlanContextListEmpty() throws Exception {
        dialPlanValidator.validateDialPlanContextList(Collections.emptyList());
    }

    @Test
    public void isDialPlanContextValidContextNull() throws Exception {
        assertFalse(dialPlanValidator.isDialPlanContextValid(null));
    }

    @Test
    public void isDialPlanContextValidContextNameNull() throws Exception {
        dialPlanContext.setContextName(null);
        assertFalse(dialPlanValidator.isDialPlanContextValid(dialPlanContext));
    }

    @Test
    public void isDialPlanContextValidExtensionPhoneNumberNull() throws Exception {
        for (DialPlanExtension dialPlanExtension : dialPlanContext.getDialPlanExtensionList()) {
            dialPlanExtension.setPhoneNumber(null);
        }
        assertFalse(dialPlanValidator.isDialPlanContextValid(dialPlanContext));
    }

    @Test
    public void isDialPlanContextValidExtensionPriorityNull() throws Exception {
        for (DialPlanExtension dialPlanExtension : dialPlanContext.getDialPlanExtensionList()) {
            dialPlanExtension.setPriority(null);
        }
        assertFalse(dialPlanValidator.isDialPlanContextValid(dialPlanContext));
    }

    @Test
    public void isDialPlanContextValidExtensionAppNull() throws Exception {
        for (DialPlanExtension dialPlanExtension : dialPlanContext.getDialPlanExtensionList()) {
            dialPlanExtension.setDialPlanApplication(null);
        }
        assertFalse(dialPlanValidator.isDialPlanContextValid(dialPlanContext));
    }

    @Test
    public void isDialPlanContextValid() throws Exception {
        assertTrue(dialPlanValidator.isDialPlanContextValid(dialPlanContext));
    }

    private void generateDialPlanContext() {
        dialPlanContext = DialPlanGenerator.getDialPlanContext(1, 1);
    }
}