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

package ch.zhaw.psit4.services.implementation;

import ch.zhaw.psit4.domain.ConfigWriter;
import ch.zhaw.psit4.domain.ConfigZipWriter;
import ch.zhaw.psit4.domain.exceptions.InvalidConfigurationException;
import ch.zhaw.psit4.domain.exceptions.ZipFileCreationException;
import ch.zhaw.psit4.domain.interfaces.AsteriskContextInterface;
import ch.zhaw.psit4.domain.interfaces.AsteriskSipClientInterface;
import ch.zhaw.psit4.services.implementation.adapters.DialPlanConfigAdapter;
import ch.zhaw.psit4.services.implementation.adapters.SipClientConfigAdapter;
import ch.zhaw.psit4.services.interfaces.ConfigServiceInterface;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Puts together the asterisk configuration.<br>
 * The asterisk configuration files produced by this service are sip.conf and extensions.conf.
 * <h1>sip.conf</h1>
 * See class {@link SipClientConfigurationChanSip} for further information about the structure of the file.
 * <h1>extension.conf</h1>
 * See class {@link DialPlanConfigurationChanSip} for further information about the structure of the file.
 *
 * @author Jona Braun
 */
@Service
public class ConfigServiceImpl implements ConfigServiceInterface {
    private final SipClientConfigAdapter sipClientConfigAdapter;
    private final DialPlanConfigAdapter dialPlanConfigAdapter;

    public ConfigServiceImpl(SipClientConfigAdapter sipClientConfigAdapter, DialPlanConfigAdapter
            dialPlanConfigAdapter) {
        this.dialPlanConfigAdapter = dialPlanConfigAdapter;
        this.sipClientConfigAdapter = sipClientConfigAdapter;
    }

    /**
     * Puts together the asterisk configuration
     * and returns it as zip file in a {@link ByteArrayOutputStream}.
     *
     * @return ByteArrayOutputStream of the zipped asterisk configuration
     * @throws InvalidConfigurationException if config is invalid
     * @throws ZipFileCreationException      if zip file creation fails
     */
    @Override
    public ByteArrayOutputStream getAsteriskConfiguration() {
        List<AsteriskSipClientInterface> sipClientList = sipClientConfigAdapter.getSipClientList();
        List<? extends AsteriskContextInterface> contexts = dialPlanConfigAdapter.getDialPlan();

        String sipClientConf = ConfigWriter.generateSipClientConfiguration(sipClientList);
        String dialPlanConf = ConfigWriter.generateDialPlanConfiguration(contexts);

        ConfigZipWriter configZipWriter = new ConfigZipWriter(sipClientConf, dialPlanConf);

        return configZipWriter.writeConfigurationZipFile();
    }

}