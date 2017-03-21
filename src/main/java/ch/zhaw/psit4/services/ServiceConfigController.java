package ch.zhaw.psit4.services;

import ch.zhaw.psit4.domain.ConfigWriter;
import ch.zhaw.psit4.domain.ConfigZipWriter;
import ch.zhaw.psit4.domain.SipClient;
import ch.zhaw.psit4.domain.SipClientConfigurationV11;
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
public class ServiceConfigController implements ServiceConfigControllerInterface {

    /**
     * Puts together the asterisk configuration
     * and returns it as zip file in a {@link ByteArrayOutputStream}.
     *
     * @return ByteArrayOutputStream of the zipped asterisk configuration
     * @throws ch.zhaw.psit4.domain.exceptions.InvalidConfigurationException
     * @throws ch.zhaw.psit4.domain.exceptions.ZipFileCreationException
     */
    public ByteArrayOutputStream getAsteriskConfiguration() {

        SipClientConfigurationV11 sipClientConfigurationV11 = new SipClientConfigurationV11();
        ConfigWriter configWriter = new ConfigWriter(sipClientConfigurationV11);

        List<SipClient> sipClientList = getSipClientList();

        String sipClientConf = configWriter.generateSipClientConfiguration(sipClientList);

        //TODO get dial plan configuration

        ConfigZipWriter configZipWriter = new ConfigZipWriter(sipClientConf, "");

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