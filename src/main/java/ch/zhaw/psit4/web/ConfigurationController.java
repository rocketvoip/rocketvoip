package ch.zhaw.psit4.web;

import ch.zhaw.psit4.domain.ConfigWriter;
import ch.zhaw.psit4.domain.ConfigZipWriter;
import ch.zhaw.psit4.domain.SipClient;
import ch.zhaw.psit4.domain.SipClientConfigurationV11;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jona Braun
 */
@RestController
@RequestMapping(path = "/v1")
public class ConfigurationController {
    private static final String ZIP_FILE_NAME = "config.zip";

    @GetMapping(value = "/configuration/zip", produces = "application/zip")
    public byte[] getAsteriskConfiguration(HttpServletResponse response) {

        response.addHeader("Content-Disposition", "attachment; filename=" + ZIP_FILE_NAME);

        SipClientConfigurationV11 sipClientConfigurationV11 = new SipClientConfigurationV11();
        ConfigWriter configWriter = new ConfigWriter(sipClientConfigurationV11);

        List<SipClient> sipClientList = getSipClientList();

        String sipClientConf = configWriter.generateSipClientConfiguration(sipClientList);

        //TODO get dial plan configuration
        ConfigZipWriter configZipWriter = new ConfigZipWriter(sipClientConf, "");

        return configZipWriter.writeConfigurationZipFile().toByteArray();
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
