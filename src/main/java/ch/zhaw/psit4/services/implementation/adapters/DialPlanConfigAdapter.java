package ch.zhaw.psit4.services.implementation.adapters;

import ch.zhaw.psit4.data.jpa.entities.DialPlan;
import ch.zhaw.psit4.data.jpa.repositories.DialPlanRepository;
import ch.zhaw.psit4.data.jpa.repositories.DialRepository;
import ch.zhaw.psit4.data.jpa.repositories.SayAlphaRepository;
import ch.zhaw.psit4.data.jpa.repositories.SipClientRepository;
import ch.zhaw.psit4.domain.beans.DialPlanContext;
import ch.zhaw.psit4.domain.beans.DialPlanExtension;
import ch.zhaw.psit4.domain.beans.SipClient;
import ch.zhaw.psit4.domain.builders.DialPlanConfigBuilder;
import ch.zhaw.psit4.domain.builders.DialPlanDefaultContextPrologBuilder;
import ch.zhaw.psit4.domain.builders.TopLevelContextBuilder;
import ch.zhaw.psit4.domain.dialplan.applications.DialApp;
import ch.zhaw.psit4.domain.dialplan.applications.GotoApp;
import ch.zhaw.psit4.domain.dialplan.applications.SayAlphaApp;
import ch.zhaw.psit4.domain.interfaces.DialPlanContextConfigurationInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Helps to gather all dial plan data from the data storage and converts it to the domain specific objects.
 *
 * TODO: Class is getting its own gravitational field. Some refactoring might be in order.
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

    private static List<SipClient> convertListOfSipClientEntitiesToDomainSipClients(Collection<ch.zhaw.psit4.data.jpa
            .entities.SipClient> entityList) {
        return entityList
                .stream()
                .map(SipClientConfigAdapter::sipClientEntityToSipClient)
                .collect(Collectors.toList());
    }

    public List<? extends DialPlanContextConfigurationInterface> getDialPlan() {
        TopLevelContextBuilder topLevelContextBuilder = new TopLevelContextBuilder();

        List<SipClient> sipClients =
                StreamSupport.stream(sipClientRepository.findAll().spliterator(), false)
                        .map(SipClientConfigAdapter::sipClientEntityToSipClient)
                        .collect(Collectors.toList());

        topLevelContextBuilder.perCompanyDialExtensions(sipClients);

        DialPlanConfigBuilder topLevelContextReferences = collectTopLevelContextReferences(topLevelContextBuilder);

        DialPlanDefaultContextPrologBuilder dialPlanDefaultContextPrologBuilder =
                collectDialPlans(topLevelContextReferences);


        return dialPlanDefaultContextPrologBuilder.build();
    }

    private DialPlanConfigBuilder collectTopLevelContextReferences(TopLevelContextBuilder topLevelContextBuilder) {
        DialPlanConfigBuilder dialPlanConfigBuilder = new DialPlanConfigBuilder(topLevelContextBuilder);

        List<DialPlan> dialPlansNotNullPhoneNr = dialPlanRepository.findAllByPhoneNrNotNull();
        dialPlansNotNullPhoneNr.forEach(dialPlan -> {
            // TODO: sorry, that's not the responsibility of the adapter knowing how to translate company names into
            // context names.
            String companyContextName = dialPlan.getCompany().getName().replaceAll(" ", "-");

            dialPlanConfigBuilder.activateExistingContext(companyContextName);
            DialPlanExtension dialPlanExtension = new DialPlanExtension();
            // TODO: again, too much distributed knowledge, aka coupling
            dialPlanExtension.setPriority(TopLevelContextBuilder.DEFAULT_PRIORITY);
            dialPlanExtension.setPhoneNumber(dialPlan.getPhoneNr());

            // TODO: SSDL (Same shit, different line). See above.
            String reference = companyContextName + "-" + dialPlan.getTitle();
            reference = reference.replaceAll(" ", "-");
            GotoApp gotoApp = new GotoApp(reference);

            dialPlanConfigBuilder
                    .addNewExtension(dialPlanExtension)
                    .setApplication(gotoApp);
        });

        return dialPlanConfigBuilder;
    }

    private DialPlanDefaultContextPrologBuilder collectDialPlans(DialPlanConfigBuilder companyDialPlanBuilder) {
        final class DialPlanWrapper {
            private final DialPlan dialPlan;

            private DialPlanWrapper(DialPlan dialPlan) {
                this.dialPlan = dialPlan;
            }

            private String getUniqueContextName() {
                return dialPlan.getCompany().getName() + "-" + dialPlan.getTitle();
            }
        }
        DialPlanDefaultContextPrologBuilder dialPlanBuilder = new DialPlanDefaultContextPrologBuilder
                (companyDialPlanBuilder);

        // Collect all referenced dialplans
        Map<Long, DialPlanWrapper> dialPlanMap = new HashMap<>();

        StreamSupport.stream(sayAlphaRepository.findAll().spliterator(), false)
                .forEach(entry ->
                        dialPlanMap.computeIfAbsent(
                                entry.getDialPlan().getId(),
                                x -> new DialPlanWrapper(entry.getDialPlan()
                                )
                        )
                );

        StreamSupport.stream(dialRepository.findAll().spliterator(), false)
                .forEach(entry ->
                        dialPlanMap.computeIfAbsent(
                                entry.getDialPlan().getId(),
                                x -> new DialPlanWrapper(entry.getDialPlan()
                                )
                        )
                );


        // Get all 'actions' for each referenced dialplan and build the context
        dialPlanMap.forEach(
                (dialPlanId, dialPlanWrapper) -> {
                    DialPlanContext dialPlanContext = new DialPlanContext();
                    dialPlanContext.setContextName(dialPlanWrapper.getUniqueContextName());
                    dialPlanBuilder.addNewContext(dialPlanContext);

                    sayAlphaRepository.findAllByDialPlan_Id(dialPlanId).forEach(
                            sayAlphaEntry -> {
                                DialPlanExtension dialPlanExtension = new DialPlanExtension();
                                dialPlanExtension.setPhoneNumber("s");
                                dialPlanExtension.setPriority(sayAlphaEntry.getPriority());

                                dialPlanBuilder
                                        .addNewExtension(dialPlanExtension)
                                        .setApplication(new SayAlphaApp(sayAlphaEntry.getVoiceMessage()));

                            }
                    );

                    dialRepository.findAllByDialPlan_Id(dialPlanId).forEach(
                            dialEntry -> {
                                DialPlanExtension dialPlanExtension = new DialPlanExtension();
                                dialPlanExtension.setPhoneNumber("s");
                                dialPlanExtension.setPriority(dialEntry.getPriority());


                                DialApp dialApp = new DialApp(
                                        DialApp.Technology.SIP,
                                        convertListOfSipClientEntitiesToDomainSipClients(dialEntry
                                                .getSipClients()),
                                        dialEntry.getRingingTime());
                                dialPlanBuilder.addNewExtension(dialPlanExtension)
                                        .setApplication(dialApp);
                            }
                    );
                }
        );

        return dialPlanBuilder;
    }


}