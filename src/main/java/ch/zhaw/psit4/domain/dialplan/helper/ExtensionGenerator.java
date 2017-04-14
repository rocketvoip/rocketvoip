package ch.zhaw.psit4.domain.dialplan.helper;

import ch.zhaw.psit4.domain.company.CompanyDomain;
import ch.zhaw.psit4.domain.dialplan.DialPlanExtension;
import ch.zhaw.psit4.domain.dialplan.applications.DialApp;
import ch.zhaw.psit4.domain.helper.SipClientValidator;
import ch.zhaw.psit4.domain.sipclient.SipClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Generates extensions.
 *
 * @author Jona Braun
 */
public class ExtensionGenerator {

    private static final SipClientValidator sipClientValidator = new SipClientValidator();
    private static final String TIMEOUT = "30";

    private ExtensionGenerator() {
        // intentionally empty
    }

    /**
     * Puts together the default extensions for a specific company.
     *
     * @param companyDomain the company
     * @return the default extension for the given company
     */
    public static List<DialPlanExtension> getDefaultExtension(CompanyDomain companyDomain) {
        List<SipClient> sipClientList = companyDomain.getSipClientList();
        List<DialPlanExtension> dialPlanExtensionList = new ArrayList<>();

        for (SipClient sipClient : sipClientList) {
            if (!sipClientValidator.isSipClientValid(sipClient)) {
                continue;
            }
            DialPlanExtension dialPlanExtension = getDefaultDialPlanExtension(sipClient);
            dialPlanExtensionList.add(dialPlanExtension);
        }

        return dialPlanExtensionList;
    }

    private static DialPlanExtension getDefaultDialPlanExtension(SipClient sipClient) {
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
