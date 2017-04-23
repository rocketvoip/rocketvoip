package ch.zhaw.psit4.services.implementation;

import ch.zhaw.psit4.domain.ConfigWriter;
import ch.zhaw.psit4.domain.ConfigZipWriter;
import ch.zhaw.psit4.domain.exceptions.InvalidConfigurationException;
import ch.zhaw.psit4.domain.exceptions.ZipFileCreationException;
import ch.zhaw.psit4.domain.interfaces.DialPlanContextConfigurationInterface;
import ch.zhaw.psit4.domain.interfaces.SipClientConfigurationInterface;
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
        List<SipClientConfigurationInterface> sipClientList = sipClientConfigAdapter.getSipClientList();
        List<? extends DialPlanContextConfigurationInterface> contexts = dialPlanConfigAdapter.getDialPlan();

        String sipClientConf = ConfigWriter.generateSipClientConfiguration(sipClientList);
        String dialPlanConf = ConfigWriter.generateDialPlanConfiguration(contexts);

        ConfigZipWriter configZipWriter = new ConfigZipWriter(sipClientConf, dialPlanConf);

        return configZipWriter.writeConfigurationZipFile();
    }

}