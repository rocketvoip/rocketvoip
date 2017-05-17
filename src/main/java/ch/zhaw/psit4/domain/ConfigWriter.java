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

package ch.zhaw.psit4.domain;

import ch.zhaw.psit4.domain.exceptions.InvalidConfigurationException;
import ch.zhaw.psit4.domain.interfaces.AsteriskContextInterface;
import ch.zhaw.psit4.domain.interfaces.AsteriskSipClientInterface;

import java.util.List;
import java.util.Optional;

/**
 * Create the string representation of sip.conf and extension.conf. Method may use the output of the various builder
 * classes.
 *
 * Content for sip.conf is best created by using SipClientConfigBuilder. Stock extension.conf content is created
 * using the CompanyDialPlanBuilder. For more sophisticated extension configuration, use the builders derived from
 * DialPlanConfigBuilder.
 *
 * @author Jona Braun
 */
public final class ConfigWriter {

    private ConfigWriter() {
        // Intentionally empty
    }

    /**
     * Convert a list of SipClients to content suitable for sip.conf.
     *
     * @param sipClientList list of SipClients
     * @return string suitable for sip.conf
     * @throws InvalidConfigurationException if the sipClientList is null or the list is empty
     */
    public static String generateSipClientConfiguration(List<? extends AsteriskSipClientInterface> sipClientList) {
        if (sipClientList == null) {
            throw new InvalidConfigurationException("sipClientList is null");
        }

        if (sipClientList.isEmpty()) {
            throw new InvalidConfigurationException("sipClientList is empty");
        }
        StringBuilder stringBuilder = new StringBuilder();
        sipClientList.forEach(x ->
                Optional
                        .ofNullable(x)
                        .ifPresent(y -> {
                                    y.validate();
                                    stringBuilder.append(y.toSipClientConfiguration());
                                }
                        )
        );
        return stringBuilder.toString();
    }

    /**
     * Convert a list of DialPlanContexts to content suitable for extension.conf.
     *
     * @param dialPlanContextList list of DialPlanContexts.
     * @return string suitable for extension.conf
     * @throws InvalidConfigurationException if the sipClientList is null or the list is empty
     */
    public static String generateDialPlanConfiguration(List<? extends AsteriskContextInterface>
                                                               dialPlanContextList) {
        if (dialPlanContextList == null) {
            throw new InvalidConfigurationException("dialPlanContextList is null");
        }

        if (dialPlanContextList.isEmpty()) {
            throw new InvalidConfigurationException("dialPlanContextList is empty");
        }
        StringBuilder stringBuilder = new StringBuilder();

        dialPlanContextList.forEach(x ->
                Optional
                        .ofNullable(x)
                        .ifPresent(y -> {
                                    y.validate();
                                    stringBuilder.append(y.toDialPlanContextConfiguration());
                                }
                        )
        );

        return stringBuilder.toString();
    }

}
