package ch.zhaw.psit4.domain.helper;

import ch.zhaw.psit4.domain.exceptions.InvalidConfigurationException;
import ch.zhaw.psit4.domain.sipclient.SipClient;
import ch.zhaw.psit4.testsupport.fixtures.domain.SipClientGenerator;
import ch.zhaw.psit4.testsupport.fixtures.general.CompanyData;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Jona Braun
 */
public class SipClientValidatorTest {
    private SipClientValidator sipClientValidator;
    private SipClient sipClient;

    @Before
    public void setup() {
        sipClientValidator = new SipClientValidator();
        sipClient = new SipClient();
        sipClient = SipClientGenerator.getSipClient(CompanyData.COMPANY_PREFIX, 1);
    }

    @Test(expected = InvalidConfigurationException.class)
    public void validateDialPlanContextListNull() throws Exception {
        sipClientValidator.validateSipClientList(null);
    }

    @Test(expected = InvalidConfigurationException.class)
    public void validateDialPlanContextListEmpty() throws Exception {
        sipClientValidator.validateSipClientList(Collections.emptyList());
    }

    @Test
    public void testClientWithNullUsername() throws Exception {
        sipClient.setUsername(null);
        assertFalse(sipClientValidator.isSipClientValid(sipClient));
    }

    @Test
    public void testClientWithNullCompany() throws Exception {
        sipClient = new SipClient();
        // set everything except the company
        sipClient.setPhoneNumber("1");
        sipClient.setSecret("");
        sipClient.setId(1);
        sipClient.setUsername("");
        assertFalse(sipClientValidator.isSipClientValid(sipClient));
    }

    @Test
    public void testClientWithNullSecret() throws Exception {
        sipClient.setSecret(null);
        assertFalse(sipClientValidator.isSipClientValid(sipClient));
    }

    @Test
    public void testClientWithNullPhoneNumber() throws Exception {
        sipClient.setPhoneNumber(null);
        assertFalse(sipClientValidator.isSipClientValid(sipClient));
    }

    @Test
    public void testClientWithNoNullValues() throws Exception {
        assertTrue(sipClientValidator.isSipClientValid(sipClient));
    }

}