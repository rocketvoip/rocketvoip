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

package ch.zhaw.psit4.testsupport.fixtures.domain;

import ch.zhaw.psit4.domain.beans.SipClient;
import ch.zhaw.psit4.testsupport.fixtures.general.CompanyData;
import ch.zhaw.psit4.testsupport.fixtures.general.SipClientData;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper for testing the domain specific sip client configuration.
 *
 * @author Jona Braun
 */
public final class SipClientGenerator {
    private SipClientGenerator() {
        // intentionally empty
    }

    /**
     * Create a domain sip client. The id will be 0 and has to be set by the caller.
     *
     * @param company name of the company
     * @param number  number of sip client
     * @return domain SipClient instance.
     */
    public static SipClient getSipClient(int company, int number) {
        SipClient sipClient = new SipClient();
        sipClient.setCompany(CompanyData.getCompanyName(company));
        sipClient.setPhoneNumber(SipClientData.getSipClientPhoneNumber(number));
        sipClient.setSecret(SipClientData.getSipClientSecret(number));
        sipClient.setUsername(SipClientData.getSipClientLabel(number));
        return sipClient;
    }

    /**
     * Generates a list of domain specific sip clients.
     *
     * @param number  the sip clients to create
     * @param company the company of the sip clients
     * @return the generated sip client list
     */
    public static List<SipClient> generateSipClientList(int number, int company) {
        List<SipClient> sipClientList = new ArrayList<>();
        for (int i = 1; i <= number; i++) {
            SipClient sipClient = getSipClient(company, i);
            sipClientList.add(sipClient);
        }
        return sipClientList;
    }


}
