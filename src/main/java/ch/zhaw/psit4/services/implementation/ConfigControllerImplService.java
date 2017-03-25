package ch.zhaw.psit4.services.implementation;

import ch.zhaw.psit4.data.jpa.repositories.SipClientRepository;
import ch.zhaw.psit4.domain.ConfigWriter;
import ch.zhaw.psit4.domain.ConfigZipWriter;
import ch.zhaw.psit4.domain.dialplan.DialPlanConfigurationChanSip;
import ch.zhaw.psit4.domain.interfaces.DialPlanConfigurationInterface;
import ch.zhaw.psit4.domain.interfaces.SipClientConfigurationInterface;
import ch.zhaw.psit4.domain.sipclient.SipClient;
import ch.zhaw.psit4.domain.sipclient.SipClientConfigurationChanSip;
import ch.zhaw.psit4.services.interfaces.ConfigControllerServiceInterface;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Puts together the asterisk configuration.
 *
 * @author Jona Braun
 */
@Service
public class ConfigControllerImplService implements ConfigControllerServiceInterface {
    private SipClientConfigurationInterface sipClientConfiguration;
    private DialPlanConfigurationInterface dialPlanConfigurationChanSip;
    private SipClientRepository sipClientRepository;

    public ConfigControllerImplService(SipClientRepository sipClientRepository) {
        this.sipClientRepository = sipClientRepository;
        sipClientConfiguration = new SipClientConfigurationChanSip();
        dialPlanConfigurationChanSip = new DialPlanConfigurationChanSip();
    }

    /**
     * Puts together the asterisk configuration
     * and returns it as zip file in a {@link ByteArrayOutputStream}.
     *
     * @return ByteArrayOutputStream of the zipped asterisk configuration
     * @throws ch.zhaw.psit4.domain.exceptions.InvalidConfigurationException if config is invalid
     * @throws ch.zhaw.psit4.domain.exceptions.ZipFileCreationException      if zip file creation fails
     */
    @Override
    public ByteArrayOutputStream getAsteriskConfiguration() {
        List<SipClient> sipClientList = getSipClientList();

        ConfigWriter configWriter = new ConfigWriter(sipClientConfiguration, dialPlanConfigurationChanSip);

        String sipClientConf = configWriter.generateSipClientConfiguration(sipClientList);
        String dialPlanConf = configWriter.generateDialPlanConfiguration(sipClientList, null);

        ConfigZipWriter configZipWriter = new ConfigZipWriter(sipClientConf, dialPlanConf);

        return configZipWriter.writeConfigurationZipFile();
    }

    private List<SipClient> getSipClientList() {
        List<SipClient> sipClientList = new ArrayList<>();
        for (ch.zhaw.psit4.data.jpa.entities.SipClient sipClient : sipClientRepository.findAll()) {
            SipClient sipClientDomain = sipClientEntityToSipClient(sipClient);
            sipClientList.add(sipClientDomain);
        }
        return sipClientList;
    }

    private SipClient sipClientEntityToSipClient(ch.zhaw.psit4.data.jpa.entities.SipClient sipClient) {
        SipClient sipClientDomain = new SipClient();

        sipClientDomain.setUsername(sipClient.getLabel());
        sipClientDomain.setPhoneNumber(sipClient.getPhoneNr());
        sipClientDomain.setCompany(sipClient.getCompany().getName());
        sipClientDomain.setSecret(sipClient.getSecret());
        sipClientDomain.setId(sipClient.getId());

        return sipClientDomain;
    }
}