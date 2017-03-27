package ch.zhaw.psit4.domain.helper;

import ch.zhaw.psit4.domain.dialplan.DialPlanContext;
import ch.zhaw.psit4.domain.dialplan.DialPlanExtension;
import ch.zhaw.psit4.domain.dialplan.applications.DialApp;
import ch.zhaw.psit4.domain.sipclient.SipClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper for a domain specific dial plan.
 */
public class DialPlanTestHelper {
    private static final String USERNAME = "Name";
    private static final String PHONE = "Phone";
    private SipClientTestHelper sipClientTestHelper = new SipClientTestHelper();

    /**
     * Creates a dial plan with company name "acme0", "acme1", ...
     *
     * @param numberOfContext the number of contexts
     * @param numberOfClients the number of sip clients per context
     * @return the dial plan
     */
    public String getSimpleDialPlan(int numberOfContext, int numberOfClients) {
        return getSimpleDialPlan(numberOfContext, numberOfClients, "acme");
    }


    public String getSimpleDialPlan(int numberOfContext, int numberOfClients, String company) {

        String dialPlan = "";
        for (int i = 1; i <= numberOfContext; i++) {
            dialPlan += getSimpleDialPlanContext(numberOfClients, company + i);
        }

        return dialPlan;
    }

    /**
     * Generates a simple dial plan with company "acme".
     *
     * @param number the number of sip clients for the simple dial plan
     * @return the generated dial plan
     */
    public String getSimpleDialPlanContext(int number) {
        return getSimpleDialPlanContext(number, "acme");
    }

    /**
     * Generates a simple dial plan.
     *
     * @param number  the number of sip clients
     * @param company the company of the sip clients
     * @return the generated dial plan
     */
    public String getSimpleDialPlanContext(int number, String company) {
        String simpleDialPlan = "[" + company + "]\n";
        for (int i = 1; i <= number; i++) {

            simpleDialPlan += "exten=> " +
                    PHONE + i + ", 1, " +
                    "Dial(SIP/" + USERNAME + i + "-" +
                    company.replaceAll(" ", "-") + ", 30)\n";
        }
        return simpleDialPlan + "\n";
    }

    /**
     * Generates a list of dial plan contexts with company names "acme0", "amce1", ...
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
     * @param companyNumber   then number appended to "acme"
     * @return the generated context
     */
    public DialPlanContext getDialPlanContext(int numberOfClients, int companyNumber) {
        DialPlanContext dialPlanContext = new DialPlanContext();
        dialPlanContext.setContextName("acme" + companyNumber);
        List<DialPlanExtension> dialPlanExtensionList = getDialPlanExtensionList(numberOfClients, companyNumber);
        dialPlanContext.setDialPlanExtensionList(dialPlanExtensionList);
        return dialPlanContext;
    }

    private List<DialPlanExtension> getDialPlanExtensionList(int numberOfClients, int companyNumber) {
        List<SipClient> sipClients = sipClientTestHelper.generateSipClientList(numberOfClients, "acme" + companyNumber);
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