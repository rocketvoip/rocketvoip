package ch.zhaw.psit4.services.implementation;

import ch.zhaw.psit4.domain.ConfigWriter;
import ch.zhaw.psit4.domain.ConfigZipWriter;
import ch.zhaw.psit4.domain.dialplan.DialPlanConfigurationChanSip;
import ch.zhaw.psit4.domain.interfaces.DialPlanConfigurationInterface;
import ch.zhaw.psit4.domain.interfaces.SipClientConfigurationInterface;
import ch.zhaw.psit4.domain.sipclient.SipClient;
import ch.zhaw.psit4.domain.sipclient.SipClientConfigurationChanSip;
import ch.zhaw.psit4.services.interfaces.ServiceConfigControllerInterface;
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
public class ConfigControllerServiceImpl implements ServiceConfigControllerInterface {

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

        SipClientConfigurationInterface sipClientConfiguration = new SipClientConfigurationChanSip();
        DialPlanConfigurationInterface dialPlanConfigurationChanSip = new DialPlanConfigurationChanSip();

        ConfigWriter configWriter = new ConfigWriter(sipClientConfiguration, dialPlanConfigurationChanSip);

        String sipClientConf = configWriter.generateSipClientConfiguration(sipClientList);
        String dialPlanConf = configWriter.generateDialPlanConfiguration(sipClientList, null);

        ConfigZipWriter configZipWriter = new ConfigZipWriter(sipClientConf, dialPlanConf);

        return configZipWriter.writeConfigurationZipFile();
    }

    private List<SipClient> getSipClientList() {
        // TODO get sip clients form the database
        SipClient sipClient = new SipClient();
        sipClient.setUsername("user1");
        sipClient.setPhoneNumber("1234");
        sipClient.setCompany("acme");
        sipClient.setSecret("secret1");

        ArrayList<SipClient> sipClientArrayList = new ArrayList<>();
        sipClientArrayList.add(sipClient);

        return sipClientArrayList;
    }
}