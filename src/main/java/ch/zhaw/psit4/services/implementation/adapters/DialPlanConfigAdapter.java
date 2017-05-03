package ch.zhaw.psit4.services.implementation.adapters;

import ch.zhaw.psit4.data.jpa.entities.DialPlan;
import ch.zhaw.psit4.data.jpa.repositories.*;
import ch.zhaw.psit4.domain.AsteriskUtlities;
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
    private final DialPlanRepository dialPlanRepository;
    private final SipClientRepository sipClientRepository;
    private final DialRepository dialRepository;
    private final SayAlphaRepository sayAlphaRepository;
    private final GotoRepository gotoRepository;

    public DialPlanConfigAdapter(SipClientRepository sipClientRepository,
                                 DialPlanRepository dialPlanRepository,
                                 DialRepository dialRepository,
                                 SayAlphaRepository sayAlphaRepository,
                                 GotoRepository gotoRepository) {
        this.sipClientRepository = sipClientRepository;
        this.dialPlanRepository = dialPlanRepository;
        this.dialRepository = dialRepository;
        this.sayAlphaRepository = sayAlphaRepository;
        this.gotoRepository = gotoRepository;
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
        DialPlanConfigBuilder dialPlanConfigBuilder = new TopLevelContextBuilder(topLevelContextBuilder);

        List<DialPlan> dialPlansNotNullPhoneNr = dialPlanRepository.findAllByPhoneNrNotNull();
        dialPlansNotNullPhoneNr.forEach(dialPlan -> {
            String companyContextName = AsteriskUtlities.toContextIdentifier(dialPlan.getCompany().getName());

            dialPlanConfigBuilder.activateExistingContext(companyContextName);
            DialPlanExtension dialPlanExtension = new DialPlanExtension();
            dialPlanExtension.setPriority(TopLevelContextBuilder.DEFAULT_PRIORITY);
            dialPlanExtension.setPhoneNumber(dialPlan.getPhoneNr());

            String reference = AsteriskUtlities.makeContextIdentifierFromCompanyAndContextName(companyContextName,
                    dialPlan.getTitle());
            reference = reference.replaceAll(" ", "-");
            GotoApp gotoApp = new GotoApp(reference);

            dialPlanConfigBuilder
                    .addNewExtension(dialPlanExtension)
                    .setApplication(gotoApp);
        });

        return dialPlanConfigBuilder;
    }

    private DialPlanDefaultContextPrologBuilder collectDialPlans(DialPlanConfigBuilder companyDialPlanBuilder) {

        DialPlanDefaultContextPrologBuilder dialPlanBuilder = new DialPlanDefaultContextPrologBuilder
                (companyDialPlanBuilder);

        // Collect all dialplans referenced by individual actions.
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

        StreamSupport.stream(gotoRepository.findAll().spliterator(), false)
                .forEach(entry ->
                        dialPlanMap.computeIfAbsent(entry.getDialPlan().getId(),
                                x -> new DialPlanWrapper(entry.getDialPlan()))
                );


        // Get all 'actions' for each referenced dialplan and build the context
        dialPlanMap.forEach(
                (dialPlanId, dialPlanWrapper) -> {
                    DialPlanContext dialPlanContext = new DialPlanContext();
                    dialPlanContext.setContextName(dialPlanWrapper.getUniqueContextName());
                    dialPlanBuilder.addNewContext(dialPlanContext);

                    sayAlphaRepository.findAllByDialPlanId(dialPlanId).forEach(
                            sayAlphaEntry -> {
                                DialPlanExtension dialPlanExtension = new DialPlanExtension();
                                dialPlanExtension.setPhoneNumber("s");
                                dialPlanExtension.setOrdinal(sayAlphaEntry.getPriority());

                                dialPlanBuilder
                                        .addNewExtension(dialPlanExtension)
                                        .setApplication(new SayAlphaApp(sayAlphaEntry.getVoiceMessage()));

                            }
                    );

                    dialRepository.findAllByDialPlanId(dialPlanId).forEach(
                            dialEntry -> {
                                DialPlanExtension dialPlanExtension = new DialPlanExtension();
                                dialPlanExtension.setPhoneNumber("s");
                                dialPlanExtension.setOrdinal(dialEntry.getPriority());


                                DialApp dialApp = new DialApp(
                                        DialApp.Technology.SIP,
                                        convertListOfSipClientEntitiesToDomainSipClients(dialEntry
                                                .getSipClients()),
                                        Integer.toString(dialEntry.getRingingTime()));
                                dialPlanBuilder.addNewExtension(dialPlanExtension)
                                        .setApplication(dialApp);
                            }
                    );

                    gotoRepository.findAllByDialPlanId(dialPlanId).forEach(
                            gotoEntry -> {
                                DialPlanExtension dialPlanExtension = new DialPlanExtension();
                                dialPlanExtension.setPhoneNumber("s");
                                dialPlanExtension.setOrdinal(gotoEntry.getPriority());

                                GotoApp gotoApp = new GotoApp(
                                        AsteriskUtlities.makeContextIdentifierFromCompanyAndContextName(
                                                gotoEntry.getNextDialPlan().getCompany().getName(),
                                                gotoEntry.getNextDialPlan().getTitle()
                                        )
                                );

                                dialPlanBuilder.addNewExtension(dialPlanExtension)
                                        .setApplication(gotoApp);
                            }
                    );
                }
        );

        return dialPlanBuilder;
    }

    private final class DialPlanWrapper {
        private final DialPlan dialPlan;

        DialPlanWrapper(DialPlan dialPlan) {
            this.dialPlan = dialPlan;
        }

        String getUniqueContextName() {
            return AsteriskUtlities.makeContextIdentifierFromCompanyAndContextName(dialPlan.getCompany().getName
                    (), dialPlan.getTitle());
        }
    }


}