package ch.zhaw.psit4.services.implementation.adapters;

import ch.zhaw.psit4.data.jpa.repositories.DialPlanRepository;
import ch.zhaw.psit4.data.jpa.repositories.DialRepository;
import ch.zhaw.psit4.data.jpa.repositories.SayAlphaRepository;
import ch.zhaw.psit4.data.jpa.repositories.SipClientRepository;
import ch.zhaw.psit4.domain.beans.SipClient;
import ch.zhaw.psit4.domain.builders.CompanyDialPlanBuilder;
import ch.zhaw.psit4.domain.interfaces.DialPlanContextConfigurationInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Helps to gather all dial plan data from the data storage and converts it to the domain specific objects.
 *
 * @author Jona Braun
 */
public class DialPlanConfigAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(DialPlanConfigAdapter.class);
    private final DialPlanRepository dialPlanRepository;
    private final SipClientRepository sipClientRepository;
    private final DialRepository dialRepository;
    private final SayAlphaRepository sayAlphaRepository;

    public DialPlanConfigAdapter(SipClientRepository sipClientRepository,
                                 DialPlanRepository dialPlanRepository,
                                 DialRepository dialRepository,
                                 SayAlphaRepository sayAlphaRepository) {
        this.sipClientRepository = sipClientRepository;
        this.dialPlanRepository = dialPlanRepository;
        this.dialRepository = dialRepository;
        this.sayAlphaRepository = sayAlphaRepository;
    }


    public List<? extends DialPlanContextConfigurationInterface> getDialPlan() {
        CompanyDialPlanBuilder companyDialPlanBuilder = new CompanyDialPlanBuilder();

        List<SipClient> sipClients =
                StreamSupport.stream(sipClientRepository.findAll().spliterator(), false)
                        .map(SipClientConfigAdapter::sipClientEntityToSipClient)
                        .collect(Collectors.toList());

        companyDialPlanBuilder.perCompanyDialExtensions(sipClients);
        return companyDialPlanBuilder.build();
    }
}