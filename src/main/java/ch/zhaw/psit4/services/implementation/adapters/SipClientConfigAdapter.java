package ch.zhaw.psit4.services.implementation.adapters;

import ch.zhaw.psit4.data.jpa.repositories.SipClientRepository;
import ch.zhaw.psit4.domain.beans.SipClient;
import ch.zhaw.psit4.domain.interfaces.SipClientConfigurationInterface;

import java.util.ArrayList;
import java.util.List;

import static ch.zhaw.psit4.services.implementation.ConfigServiceImpl.sipClientEntityToSipClient;

/**
 * Helps to gather all sip client data from the data storage and converts it to the domain specific objects.
 *
 * @author Jona Braun
 */
public class SipClientConfigAdapter {
    private final SipClientRepository sipClientRepository;

    public SipClientConfigAdapter(SipClientRepository sipClientRepository) {
        this.sipClientRepository = sipClientRepository;
    }

    /**
     * Gets all sip clients from the repository and converts them into a domain specific
     * sip client list.
     *
     * @return the domain specific sip clients
     */
    public List<SipClientConfigurationInterface> getSipClientList() {
        List<SipClientConfigurationInterface> sipClientList = new ArrayList<>();
        for (ch.zhaw.psit4.data.jpa.entities.SipClient sipClient : sipClientRepository.findAll()) {
            SipClient sipClientDomain = sipClientEntityToSipClient(sipClient);
            sipClientList.add(sipClientDomain);
        }
        return sipClientList;
    }
}
