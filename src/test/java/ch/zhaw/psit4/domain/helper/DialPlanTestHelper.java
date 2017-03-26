package ch.zhaw.psit4.domain.helper;

/**
 * Helper for a domain specific dial plan.
 */
public class DialPlanTestHelper {
    private static final String USERNAME = "Name";
    private static final String PHONE = "Phone";

    /**
     * Generates a simple dial plan with company "acme".
     *
     * @param number the number of sip clients for the simple dial plan
     * @return the generated dial plan
     */
    public String getSimpleDialPlan(int number) {
        return getSimpleDialPlan(number, "acme");
    }

    /**
     * Generates a simple dial plan.
     *
     * @param number  the number of sip clients
     * @param company the company of the sip clients
     * @return the generated dial plan
     */
    public String getSimpleDialPlan(int number, String company) {
        String simpleDialPlan = "[simple-dial-plan]\n";
        for (int i = 1; i <= number; i++) {

            simpleDialPlan += "exten=> " +
                    PHONE + i + ", 1, " +
                    "Dial(SIP/" + USERNAME + i + "-" +
                    company.replaceAll(" ", "-") + ", 30)\n";
        }
        return simpleDialPlan + "\n";
    }
}