package ch.zhaw.psit4.domain.dialplan.helper;

import ch.zhaw.psit4.domain.beans.Company;
import ch.zhaw.psit4.domain.beans.DialPlanExtension;
import ch.zhaw.psit4.domain.beans.SipClient;
import ch.zhaw.psit4.domain.dialplan.applications.DialApp;
import ch.zhaw.psit4.domain.interfaces.DialPlanExtensionConfigurationInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Generates extensions.
 *
 * @author Jona Braun
 */
public class ExtensionGenerator {

    private static final String TIMEOUT = "30";

    private ExtensionGenerator() {
        // intentionally empty
    }

    /**
     * Puts together the default extensions for a specific company.
     *
     * @param company the company
     * @return the default extension for the given company
     */
    public static List<DialPlanExtensionConfigurationInterface> getDefaultExtensions(Company company) {
        List<SipClient> sipClientList = company.getSipClientList();
        List<DialPlanExtensionConfigurationInterface> dialPlanExtensionList = new ArrayList<>();

        for (SipClient sipClient : sipClientList) {
            sipClient.validate();
            DialPlanExtension dialPlanExtension = getDefaultExtension(sipClient);
            dialPlanExtensionList.add(dialPlanExtension);
        }

        return dialPlanExtensionList;
    }

    private static DialPlanExtension getDefaultExtension(SipClient sipClient) {
        DialPlanExtension dialPlanExtension = new DialPlanExtension();
        dialPlanExtension.setPhoneNumber(sipClient.getPhoneNumber());
        dialPlanExtension.setPriority("1");

        List<SipClient> sipClients = new ArrayList<>();
        sipClients.add(sipClient);
        DialApp dialApp = new DialApp(DialApp.Technology.SIP, sipClients, TIMEOUT);

        dialPlanExtension.setDialPlanApplication(dialApp);
        return dialPlanExtension;
    }
}
