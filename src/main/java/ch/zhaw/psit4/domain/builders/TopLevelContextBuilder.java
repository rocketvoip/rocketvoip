/*
 * Copyright 2017 Jona Braun, Benedikt Herzog, Rafael Ostertag,
 *                Marcel Schöni, Marco Studerus, Martin Wittwer
 *
 * Redistribution and  use in  source and binary  forms, with  or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * 1. Redistributions  of  source code  must retain  the above  copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in  binary form must reproduce  the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation   and/or   other    materials   provided   with   the
 *    distribution.
 *
 * THIS SOFTWARE  IS PROVIDED BY  THE COPYRIGHT HOLDERS  AND CONTRIBUTORS
 * "AS  IS" AND  ANY EXPRESS  OR IMPLIED  WARRANTIES, INCLUDING,  BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES  OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE  ARE DISCLAIMED. IN NO EVENT  SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL,  EXEMPLARY,  OR  CONSEQUENTIAL DAMAGES  (INCLUDING,  BUT  NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE  GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS  INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF  LIABILITY, WHETHER IN  CONTRACT, STRICT LIABILITY,  OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN  ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package ch.zhaw.psit4.domain.builders;

import ch.zhaw.psit4.domain.applications.DialApp;
import ch.zhaw.psit4.domain.beans.DialPlanContext;
import ch.zhaw.psit4.domain.beans.DialPlanExtension;
import ch.zhaw.psit4.domain.beans.SipClient;
import ch.zhaw.psit4.domain.exceptions.InvalidConfigurationException;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This builder creates the necessary contexts with Dial extensions per company.
 * <p>
 * Use this builder to create the first batch of Asterisk contexts, enabling sip clients to be called by phone
 * number, or top level contexts reachable by phone number.
 *
 * The priorities of the extensions are all "1", for instance
 * <code>
 *     [contextN]
 *     exten => nr,1,...
 *     exten => nr,1,...
 *     ...
 * </code>
 *
 * @author Rafael Ostertag
 */
public class TopLevelContextBuilder extends DialPlanConfigBuilder {

    public static final String DEFAULT_PRIORITY = "1";
    public static final DialApp.Technology DEFAULT_TECHNOLOGY = DialApp.Technology.SIP;
    public static final String DEFAULT_TIMEOUT = "30";

    public TopLevelContextBuilder() {
        super();
    }

    public TopLevelContextBuilder(DialPlanConfigBuilder dialPlanConfigBuilder) {
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
    public TopLevelContextBuilder perCompanyDialExtensions(List<SipClient> sipClientList) {
        if (sipClientList == null || sipClientList.isEmpty()) {
            throw new InvalidConfigurationException("sipClientList must not be null or empty");
        }

        Map<String, List<SipClient>> sipClientPerCompany = sipClientList
                .stream()
                .collect(Collectors.groupingBy(SipClient::getCompany));

        sipClientPerCompany.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getKey))
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

    @Override
    protected void setAsteriskPrioritiesOnActiveContext() {
        // We cannot use the default priorities assigned by DialPlanConfigBuilder. We require all priorities to be "1".
        DialPlanContext activeContext = getActiveContext().getDialPlanContext();
        activeContext.getDialPlanExtensionList().forEach(x -> x.setPriority("1"));
    }
}
