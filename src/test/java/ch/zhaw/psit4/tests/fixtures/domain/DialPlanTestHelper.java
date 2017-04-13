package ch.zhaw.psit4.tests.fixtures.domain;

import ch.zhaw.psit4.domain.dialplan.DialPlanContext;
import ch.zhaw.psit4.domain.dialplan.DialPlanExtension;
import ch.zhaw.psit4.domain.dialplan.applications.DialApp;
import ch.zhaw.psit4.domain.sipclient.SipClient;
import ch.zhaw.psit4.tests.fixtures.general.CompanyData;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper for a domain specific dial plan.
 */
public class DialPlanTestHelper {
    private SipClientTestHelper sipClientTestHelper = new SipClientTestHelper();

    /**
     * Generates a list of dial plan contexts with company names "acme0", "acme1", ...
     *
     * @param numberOfContexts number of contexts to create
     * @param numberOfClients  number of clients per context to create
     * @return dial plan context list
     */
    public List<DialPlanContext> generateDialPlan(int numberOfContexts, int numberOfClients) {
        List<DialPlanContext> dialPlanContexts = new ArrayList<>();

        for (int i = 1; i <= numberOfContexts; i++) {
            DialPlanContext dialPlanContext = getDialPlanContext(numberOfClients, i);
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
    public DialPlanContext getDialPlanContext(int numberOfClients, int companyNumber) {
        DialPlanContext dialPlanContext = new DialPlanContext();
        dialPlanContext.setContextName(CompanyData.COMPANY_PREFIX + companyNumber);
        List<DialPlanExtension> dialPlanExtensionList = getDialPlanExtensionList(numberOfClients, companyNumber);
        dialPlanContext.setDialPlanExtensionList(dialPlanExtensionList);
        return dialPlanContext;
    }

    private List<DialPlanExtension> getDialPlanExtensionList(int numberOfClients, int companyNumber) {
        List<SipClient> sipClients = sipClientTestHelper.generateSipClientList(numberOfClients, CompanyData
                .COMPANY_PREFIX + companyNumber);
        List<DialPlanExtension> dialPlanExtensionList = new ArrayList<>();
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