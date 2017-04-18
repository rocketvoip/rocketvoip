package ch.zhaw.psit4.services.implementation;

import ch.zhaw.psit4.data.jpa.repositories.CompanyRepository;
import ch.zhaw.psit4.data.jpa.repositories.SipClientRepository;
import ch.zhaw.psit4.domain.ConfigWriter;
import ch.zhaw.psit4.domain.ConfigZipWriter;
import ch.zhaw.psit4.domain.beans.Company;
import ch.zhaw.psit4.domain.beans.DialPlanContext;
import ch.zhaw.psit4.domain.beans.SipClient;
import ch.zhaw.psit4.domain.dialplan.helper.ContextGenerator;
import ch.zhaw.psit4.domain.exceptions.InvalidConfigurationException;
import ch.zhaw.psit4.domain.exceptions.ZipFileCreationException;
import ch.zhaw.psit4.domain.interfaces.DialPlanContextConfigurationInterface;
import ch.zhaw.psit4.domain.interfaces.SipClientConfigurationInterface;
import ch.zhaw.psit4.domain.sipclient.SipClientConfigurationChanSip;
import ch.zhaw.psit4.services.implementation.adapters.DialPlanConfigAdapter;
import ch.zhaw.psit4.services.implementation.adapters.SipClientConfigAdapter;
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
    private final SipClientConfigAdapter sipClientConfigAdapter;
    private final DialPlanConfigAdapter dialPlanConfigAdapter;
    private SipClientConfigurationInterface sipClientConfiguration;

    public ConfigServiceImpl(SipClientRepository sipClientRepository, CompanyRepository companyRepository) {
        this.dialPlanConfigAdapter = new DialPlanConfigAdapter(sipClientRepository, companyRepository);
        this.sipClientConfigAdapter = new SipClientConfigAdapter(sipClientRepository);
        sipClientConfiguration = new SipClientConfigurationChanSip();
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
        ConfigWriter configWriter = new ConfigWriter(sipClientConfiguration);

        List<SipClient> sipClientList = sipClientConfigAdapter.getSipClientList();
        List<DialPlanContextConfigurationInterface> contexts = getDialPlanContexts();

        String sipClientConf = configWriter.generateSipClientConfiguration(sipClientList);
        String dialPlanConf = configWriter.generateDialPlanConfiguration(contexts);

        ConfigZipWriter configZipWriter = new ConfigZipWriter(sipClientConf, dialPlanConf);

        return configZipWriter.writeConfigurationZipFile();
    }

    private List<DialPlanContextConfigurationInterface> getDialPlanContexts() {
        List<DialPlanContext> dialPlanContextList = dialPlanConfigAdapter.getDialPlanContextList();

        List<Company> companyList = dialPlanConfigAdapter.getCompanyDomainList();
        //TODO probably it would be better if this call is in the domain, and a list of companies is passed to the domain
        List<DialPlanContext> defaultContexts = ContextGenerator.getDefaultContexts(companyList);

        List<DialPlanContextConfigurationInterface> contexts = new ArrayList<>();
        contexts.addAll(dialPlanContextList);
        contexts.addAll(defaultContexts);

        return contexts;
    }

}