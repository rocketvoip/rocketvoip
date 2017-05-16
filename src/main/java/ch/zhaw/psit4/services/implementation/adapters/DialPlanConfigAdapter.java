/*
 * Copyright 2017 Jona Braun, Benedikt Herzog, Rafael Ostertag,
 *                Marcel Schöni, Marco Studerus, Martin Wittwer
 *
 * Redistribution and  use in  source and binary  forms, with  or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * 1. Redistributions  of  source code  must retain  the above  copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in  binary form must reproduce  the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation   and/or   other    materials   provided   with   the
 *    distribution.
 *
 * THIS SOFTWARE  IS PROVIDED BY  THE COPYRIGHT HOLDERS  AND CONTRIBUTORS
 * "AS  IS" AND  ANY EXPRESS  OR IMPLIED  WARRANTIES, INCLUDING,  BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES  OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE  ARE DISCLAIMED. IN NO EVENT  SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL,  EXEMPLARY,  OR  CONSEQUENTIAL DAMAGES  (INCLUDING,  BUT  NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE  GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS  INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF  LIABILITY, WHETHER IN  CONTRACT, STRICT LIABILITY,  OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN  ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package ch.zhaw.psit4.services.implementation.adapters;

import ch.zhaw.psit4.data.jpa.entities.DialPlan;
import ch.zhaw.psit4.data.jpa.repositories.*;
import ch.zhaw.psit4.domain.AsteriskUtlities;
import ch.zhaw.psit4.domain.applications.BranchApp;
import ch.zhaw.psit4.domain.applications.DialApp;
import ch.zhaw.psit4.domain.applications.GotoApp;
import ch.zhaw.psit4.domain.applications.SayAlphaApp;
import ch.zhaw.psit4.domain.beans.DialPlanContext;
import ch.zhaw.psit4.domain.beans.DialPlanExtension;
import ch.zhaw.psit4.domain.beans.SipClient;
import ch.zhaw.psit4.domain.builders.DialPlanConfigBuilder;
import ch.zhaw.psit4.domain.builders.DialPlanDefaultContextPrologBuilder;
import ch.zhaw.psit4.domain.builders.TopLevelContextBuilder;
import ch.zhaw.psit4.domain.interfaces.AsteriskContextInterface;

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
    private final BranchRepository branchRepository;

    public DialPlanConfigAdapter(SipClientRepository sipClientRepository,
                                 DialPlanRepository dialPlanRepository,
                                 DialRepository dialRepository,
                                 SayAlphaRepository sayAlphaRepository,
                                 GotoRepository gotoRepository,
                                 BranchRepository branchRepository) {
        this.sipClientRepository = sipClientRepository;
        this.dialPlanRepository = dialPlanRepository;
        this.dialRepository = dialRepository;
        this.sayAlphaRepository = sayAlphaRepository;
        this.gotoRepository = gotoRepository;
        this.branchRepository = branchRepository;
    }

    private static List<SipClient> convertListOfSipClientEntitiesToDomainSipClients(Collection<ch.zhaw.psit4.data.jpa
            .entities.SipClient> entityList) {
        return entityList
                .stream()
                .map(SipClientConfigAdapter::sipClientEntityToSipClient)
                .collect(Collectors.toList());
    }

    public List<? extends AsteriskContextInterface> getDialPlan() {
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

        StreamSupport.stream(branchRepository.findAll().spliterator(), false)
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

                    branchRepository.findAllByDialPlanId(dialPlanId).forEach(
                            branchEntry -> {
                                dialPlanBuilder.setWaitExtenInSeconds(branchEntry.getHangupTime());

                                branchEntry.getBranchesDialPlans().forEach(dialPlanBranchTo -> {
                                    DialPlanExtension dialPlanExtension = new DialPlanExtension();
                                    dialPlanExtension.setPriority("1");
                                    dialPlanExtension.setOrdinal(branchEntry.getPriority());
                                    dialPlanExtension.setPhoneNumber(
                                            Integer.toString(dialPlanBranchTo.getButtonNumber())
                                    );

                                    String branchGotoContextName = AsteriskUtlities
                                            .makeContextIdentifierFromCompanyAndContextName(
                                                    branchEntry.getDialPlan().getCompany().getName(),
                                                    dialPlanBranchTo.getDialPlan().getTitle()
                                            );

                                    BranchApp branchApp = new BranchApp(branchGotoContextName,
                                            "s",
                                            GotoApp.DEFAULT_PRIORITY
                                    );

                                    dialPlanBuilder
                                            .addNewExtension(dialPlanExtension)
                                            .setApplication(branchApp);
                                });
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