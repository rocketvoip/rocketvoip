package ch.zhaw.psit4.domain;

import ch.zhaw.psit4.domain.beans.DialPlanContext;
import ch.zhaw.psit4.domain.beans.DialPlanExtension;
import ch.zhaw.psit4.domain.beans.SipClient;
import ch.zhaw.psit4.domain.dialplan.applications.DialApp;
import ch.zhaw.psit4.domain.exceptions.InvalidConfigurationException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This builder creates the necessary contexts with Dial extensions per company.
 *
 * @author Rafael Ostertag
 */
public class CompanyDialPlanBuilder extends DialPlanConfigBuilder {

    public static final String DEFAULT_PRIORITY = "1";
    public static final DialApp.Technology DEFAULT_TECHNOLOGY = DialApp.Technology.SIP;
    public static final String DEFAULT_TIMEOUT = "30";

    public CompanyDialPlanBuilder() {
        super();
    }

    public CompanyDialPlanBuilder(DialPlanConfigBuilder dialPlanConfigBuilder) {
        super(dialPlanConfigBuilder);
    }

    /**
     * Takes a list of SipClients, and creates for each company a context with all clients belonging to the
     * respective company.
     *
     * @param sipClientList list of SipClients.
     * @return CompanyDialPlan instance.
     * @throws InvalidConfigurationException when sipClientList is null or empty
     */
    public CompanyDialPlanBuilder perCompanyDialExtensions(List<SipClient> sipClientList) {
        if (sipClientList == null || sipClientList.isEmpty()) {
            throw new InvalidConfigurationException("sipClientList must not be null or empty");
        }

        Map<String, List<SipClient>> sipClientPerCompany = sipClientList
                .stream()
                .collect(Collectors.groupingBy(SipClient::getCompany));

        sipClientPerCompany.entrySet().stream()
                .sorted((a, b) -> a.getKey().compareTo(b.getKey()))
                .forEach(
                x -> {
                    DialPlanContext dialPlanContext = new DialPlanContext();
                    dialPlanContext.setContextName(x.getKey());
                    addNewContext(dialPlanContext);

                    x.getValue().forEach(y -> {
                        DialPlanExtension dialPlanExtension = new DialPlanExtension();
                        dialPlanExtension.setPriority(DEFAULT_PRIORITY);
                        dialPlanExtension.setPhoneNumber(y.getPhoneNumber());
                        addNewExtension(dialPlanExtension);

                        DialApp dialApp = DialApp.factory(DEFAULT_TECHNOLOGY, y, DEFAULT_TIMEOUT);
                        setApplication(dialApp);
                    });
                }
        );

        return this;
    }
}
