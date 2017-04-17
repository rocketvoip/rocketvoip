package ch.zhaw.psit4.services.implementation;

import ch.zhaw.psit4.data.jpa.repositories.CompanyRepository;
import ch.zhaw.psit4.data.jpa.repositories.SipClientRepository;
import ch.zhaw.psit4.domain.ConfigWriter;
import ch.zhaw.psit4.domain.ConfigZipWriter;
import ch.zhaw.psit4.domain.beans.Company;
import ch.zhaw.psit4.domain.beans.DialPlanContext;
import ch.zhaw.psit4.domain.beans.SipClient;
import ch.zhaw.psit4.domain.dialplan.DialPlanConfigurationChanSip;
import ch.zhaw.psit4.domain.dialplan.helper.ContextGenerator;
import ch.zhaw.psit4.domain.exceptions.InvalidConfigurationException;
import ch.zhaw.psit4.domain.exceptions.ZipFileCreationException;
import ch.zhaw.psit4.domain.interfaces.DialPlanConfigurationInterface;
import ch.zhaw.psit4.domain.interfaces.SipClientConfigurationInterface;
import ch.zhaw.psit4.domain.sipclient.SipClientConfigurationChanSip;
import ch.zhaw.psit4.services.implementation.helper.DialPlanConfigHelper;
import ch.zhaw.psit4.services.implementation.helper.SipClientConfigHelper;
import ch.zhaw.psit4.services.interfaces.ConfigServiceInterface;
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
 * See class {@link DialPlanConfigurationChanSip} for further information about the structure of the file.
 *
 * @author Jona Braun
 */
@Service
public class ConfigServiceImpl implements ConfigServiceInterface {
    private final SipClientConfigHelper sipClientConfigHelper;
    private final DialPlanConfigHelper dialPlanConfigHelper;
    private SipClientConfigurationInterface sipClientConfiguration;
    private DialPlanConfigurationInterface dialPlanConfiguration;

    public ConfigServiceImpl(SipClientRepository sipClientRepository, CompanyRepository companyRepository) {
        this.dialPlanConfigHelper = new DialPlanConfigHelper(sipClientRepository, companyRepository);
        this.sipClientConfigHelper = new SipClientConfigHelper(sipClientRepository);
        sipClientConfiguration = new SipClientConfigurationChanSip();
        dialPlanConfiguration = new DialPlanConfigurationChanSip();
    }

    /**
     * Converts a sip client entity of the data storage into a domain specific sip client.
     *
     * @param sipClient the sip client entity of the data storage
     * @return the domain specific sip client
     */
    public static SipClient sipClientEntityToSipClient(ch.zhaw.psit4.data.jpa.entities.SipClient sipClient) {
        SipClient sipClientDomain = new SipClient();
        sipClientDomain.setUsername(sipClient.getLabel());
        sipClientDomain.setPhoneNumber(sipClient.getPhoneNr());
        sipClientDomain.setCompany(sipClient.getCompany().getName());
        sipClientDomain.setSecret(sipClient.getSecret());
        sipClientDomain.setId(sipClient.getId());

        return sipClientDomain;
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
        ConfigWriter configWriter = new ConfigWriter(sipClientConfiguration, dialPlanConfiguration);

        List<SipClient> sipClientList = sipClientConfigHelper.getSipClientList();
        List<DialPlanContext> contexts = getDialPlanContexts();

        String sipClientConf = configWriter.generateSipClientConfiguration(sipClientList);
        String dialPlanConf = configWriter.generateDialPlanConfiguration(contexts);

        ConfigZipWriter configZipWriter = new ConfigZipWriter(sipClientConf, dialPlanConf);

        return configZipWriter.writeConfigurationZipFile();
    }

    private List<DialPlanContext> getDialPlanContexts() {
        List<DialPlanContext> dialPlanContextList = dialPlanConfigHelper.getDialPlanContextList();

        List<Company> companyList = dialPlanConfigHelper.getCompanyDomainList();
        //TODO probably it would be better if this call is in the domain, and a list of companies is passed to the domain
        List<DialPlanContext> defaultContexts = ContextGenerator.getDefaultContexts(companyList);

        List<DialPlanContext> contexts = new ArrayList<>();
        contexts.addAll(dialPlanContextList);
        contexts.addAll(defaultContexts);

        return contexts;
    }

}