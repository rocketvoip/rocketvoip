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

import ch.zhaw.psit4.domain.exceptions.InvalidConfigurationException;
import ch.zhaw.psit4.domain.interfaces.AsteriskSipClientInterface;

import java.util.LinkedList;
import java.util.List;

/**
 * SipClient configuration builder with fluent API. Output of this class is suitable for ConfigWriter.
 * <p>
 * This builder is mostly used to create the content for sip.conf
 *
 * @author Rafael Ostertag
 */
public class SipClientConfigBuilder {
    private List<AsteriskSipClientInterface> sipClients;

    public SipClientConfigBuilder() {
        sipClients = new LinkedList<>();
    }

    /**
     * Add a SipClient.
     *
     * @param sipClient instance implementing SipClientConfigurationInterface.
     * @return SipClientConfigBuilder
     * @throws InvalidConfigurationException                       when sipClient is null.
     * @throws ch.zhaw.psit4.domain.exceptions.ValidationException when sipClient is invalid.
     */
    public SipClientConfigBuilder addSipClient(AsteriskSipClientInterface sipClient) {
        if (sipClient == null) {
            throw new InvalidConfigurationException("sipClientConfigurationInerface must not be null");
        }
        sipClient.validate();
        sipClients.add(sipClient);
        return this;
    }

    /**
     * Build the configuration.
     *
     * @return list of SipClientConfigurationInstances suitable for ConfigWriter.
     * @throws InvalidConfigurationException when no sip clients have been added.
     */
    public List<AsteriskSipClientInterface> build() {
        if (sipClients.isEmpty()) {
            throw new InvalidConfigurationException("no sip clients in configuration");
        }
        return sipClients;
    }

}
