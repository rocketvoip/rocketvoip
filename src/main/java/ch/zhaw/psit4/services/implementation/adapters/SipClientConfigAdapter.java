package ch.zhaw.psit4.services.implementation.adapters;

import ch.zhaw.psit4.data.jpa.repositories.SipClientRepository;
import ch.zhaw.psit4.domain.SipClientConfigBuilder;
import ch.zhaw.psit4.domain.beans.SipClient;
import ch.zhaw.psit4.domain.interfaces.SipClientConfigurationInterface;

import java.util.List;

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
     * Gets all sip clients from the repository and converts them into a domain specific
     * sip client list.
     *
     * @return the domain specific sip clients
     */
    public List<SipClientConfigurationInterface> getSipClientList() {
        SipClientConfigBuilder sipClientConfigBuilder = new SipClientConfigBuilder();
        sipClientRepository.findAll().forEach(x -> sipClientConfigBuilder.addSipClient(sipClientEntityToSipClient(x)));
        return sipClientConfigBuilder.build();
    }
}
