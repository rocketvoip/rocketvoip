package ch.zhaw.psit4.testsupport.fixtures.domain;

import ch.zhaw.psit4.domain.beans.DialPlanContext;
import ch.zhaw.psit4.domain.beans.DialPlanExtension;
import ch.zhaw.psit4.domain.beans.SipClient;
import ch.zhaw.psit4.domain.dialplan.applications.DialApp;
import ch.zhaw.psit4.domain.interfaces.DialPlanContextConfigurationInterface;
import ch.zhaw.psit4.domain.interfaces.DialPlanExtensionConfigurationInterface;
import ch.zhaw.psit4.testsupport.fixtures.general.CompanyData;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper for a domain specific dial plan.
 */
public final class DialPlanGenerator {
    private DialPlanGenerator() {
        // intentionally empty
    }

    /**
     * Generates a list of dial plan contexts.
     *
     * @param numberOfContexts number of contexts to create
     * @param numberOfClients  number of clients per context to create
     * @return dial plan context list
     */
    public static List<DialPlanContextConfigurationInterface> generateDialPlan(int numberOfContexts, int
            numberOfClients) {
        List<DialPlanContextConfigurationInterface> dialPlanContexts = new ArrayList<>();

        for (int companyNumber = 1; companyNumber <= numberOfContexts; companyNumber++) {
            DialPlanContext dialPlanContext = getDialPlanContext(numberOfClients, companyNumber);
            dialPlanContexts.add(dialPlanContext);
        }
        return dialPlanContexts;
    }

    /**
     * Generates one DialPlanContext
     *
     * @param numberOfClients number of clients in this context
     * @param companyNumber   then number appended to CompanyData.COMPANY_PREFIX
     * @return the generated context
     */
    public static DialPlanContext getDialPlanContext(int numberOfClients, int companyNumber) {
        DialPlanContext dialPlanContext = new DialPlanContext();
        dialPlanContext.setContextName(CompanyData.getCompanyName(companyNumber));

        List<DialPlanExtensionConfigurationInterface> dialPlanExtensionList = getDialPlanExtensionList
                (numberOfClients, companyNumber);
        dialPlanContext.setDialPlanExtensionList(dialPlanExtensionList);
        return dialPlanContext;
    }

    private static List<DialPlanExtensionConfigurationInterface> getDialPlanExtensionList(int numberOfClients, int companyNumber) {
        List<SipClient> sipClients = SipClientGenerator.generateSipClientList(numberOfClients, companyNumber);
        List<DialPlanExtensionConfigurationInterface> dialPlanExtensionList = new ArrayList<>();
        for (int j = 1; j <= numberOfClients; j++) {

            DialPlanExtension dialPlanExtension = new DialPlanExtension();
            List<SipClient> oneSipClientList = new ArrayList<>();
            oneSipClientList.add(sipClients.get(j - 1));

            dialPlanExtension.setDialPlanApplication(new DialApp(DialApp.Technology.SIP, oneSipClientList, "30"));
            dialPlanExtension.setPhoneNumber(oneSipClientList.get(0).getPhoneNumber());
            dialPlanExtension.setPriority("1");

            dialPlanExtensionList.add(dialPlanExtension);

        }
        return dialPlanExtensionList;
    }
}