package ch.zhaw.psit4.services.implementation;

import ch.zhaw.psit4.data.jpa.entities.Company;
import ch.zhaw.psit4.data.jpa.repositories.CompanyRepository;
import ch.zhaw.psit4.data.jpa.repositories.SipClientRepository;
import ch.zhaw.psit4.domain.ConfigWriter;
import ch.zhaw.psit4.domain.ConfigZipWriter;
import ch.zhaw.psit4.domain.dialplan.DialPlanConfigurationChanSip;
import ch.zhaw.psit4.domain.dialplan.DialPlanContext;
import ch.zhaw.psit4.domain.dialplan.DialPlanExtension;
import ch.zhaw.psit4.domain.dialplan.applications.DialApp;
import ch.zhaw.psit4.domain.exceptions.InvalidConfigurationException;
import ch.zhaw.psit4.domain.exceptions.ZipFileCreationException;
import ch.zhaw.psit4.domain.helper.SipClientValidator;
import ch.zhaw.psit4.domain.interfaces.DialPlanConfigurationInterface;
import ch.zhaw.psit4.domain.interfaces.SipClientConfigurationInterface;
import ch.zhaw.psit4.domain.sipclient.SipClient;
import ch.zhaw.psit4.domain.sipclient.SipClientConfigurationChanSip;
import ch.zhaw.psit4.services.interfaces.ConfigServiceInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Puts together the asterisk configuration.<br>
 * The asterisk configuration files produced by this service are sip.conf and extensions.conf.
 * <h1>sip.conf</h1>
 * See class {@link SipClientConfigurationChanSip} for further information about the structure of the file.
 * <h1>extension.conf</h1>
 * In the extension.conf file there are tow different main contexts.<br>
 * <h2>default context</h2>
 * <p>First there is a default context for every company. The default context ensures that every SIP-client
 * can be called.<br>
 * In the following example sip-phone1/2 is the name of a sip client.:<br><br>
 * <code>
 * [company-name]<br>
 * exten => phone-number1,1,Dial(SIP/sip-phone1,timeout)<br>
 * exten => phone-number2,1,Dial(SIP/sip-phone2,timeout)<br>
 * </code>
 * </p>
 * <h2>special dial plan context</h2>
 * <p>
 * The special dial plan context is generic. It is put together according to the contexts in data storage.
 * For further information see class @{@link DialPlanContext}.
 * </p>
 *
 * @author Jona Braun
 */
@Service
public class ConfigServiceImpl implements ConfigServiceInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigServiceImpl.class);
    private final SipClientValidator sipClientValidator = new SipClientValidator();
    private SipClientConfigurationInterface sipClientConfiguration;
    private DialPlanConfigurationInterface dialPlanConfigurationChanSip;
    private SipClientRepository sipClientRepository;
    private CompanyRepository companyRepository;

    public ConfigServiceImpl(SipClientRepository sipClientRepository, CompanyRepository companyRepository) {
        this.sipClientRepository = sipClientRepository;
        this.companyRepository = companyRepository;
        sipClientConfiguration = new SipClientConfigurationChanSip();
        dialPlanConfigurationChanSip = new DialPlanConfigurationChanSip();
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
        List<SipClient> sipClientList = getSipClientList();
        List<DialPlanContext> dialPlanContextList = getDialPlanContextList();

        ConfigWriter configWriter = new ConfigWriter(sipClientConfiguration, dialPlanConfigurationChanSip);

        String sipClientConf = configWriter.generateSipClientConfiguration(sipClientList);
        String dialPlanConf = configWriter.generateDialPlanConfiguration(dialPlanContextList);

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

    private List<DialPlanContext> getDialPlanContextList() {
        List<DialPlanContext> dialPlanContextList = new ArrayList<>();
        for (Company company : companyRepository.findAll()) {
            DialPlanContext dialPlanContext = new DialPlanContext();
            dialPlanContext.setContextName(company.getName());
            List<DialPlanExtension> dialPlanExtensionList = new ArrayList<>();

            List<SipClient> sipClientList = new ArrayList<>();
            for (ch.zhaw.psit4.data.jpa.entities.SipClient sipClient : sipClientRepository.findByCompany(company)) {
                LOGGER.info("Add sipClient " + sipClient.getLabel());
                SipClient sipClientDomain = sipClientEntityToSipClient(sipClient);
                sipClientList.add(sipClientDomain);
            }

            for (SipClient sipClient : sipClientList) {
                if (!sipClientValidator.isSipClientValid(sipClient)) {
                    continue;
                }

                DialPlanExtension dialPlanExtension = new DialPlanExtension();
                dialPlanExtension.setPhoneNumber(sipClient.getPhoneNumber());
                dialPlanExtension.setPriority("1");
                List<SipClient> sipClients = new ArrayList<>();
                sipClients.add(sipClient);
                DialApp dialApp = new DialApp(DialApp.Technology.SIP, sipClients, "30");
                dialPlanExtension.setDialPlanApplication(dialApp);
                dialPlanExtensionList.add(dialPlanExtension);
            }

            dialPlanContext.setDialPlanExtensionList(dialPlanExtensionList);
            dialPlanContextList.add(dialPlanContext);
        }
        return dialPlanContextList;
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