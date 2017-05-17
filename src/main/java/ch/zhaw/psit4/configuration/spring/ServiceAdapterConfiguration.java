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

package ch.zhaw.psit4.configuration.spring;

import ch.zhaw.psit4.data.jpa.repositories.*;
import ch.zhaw.psit4.services.implementation.ActionServiceImpl;
import ch.zhaw.psit4.services.implementation.adapters.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rafael Ostertag
 */
@Configuration
public class ServiceAdapterConfiguration {

    @Bean
    public ActionServiceImpl actionServiceImpl(SayAlphaAdapter sayAlphaAdapter,
                                               DialAdapter dialAdapter,
                                               GotoAdapter gotoAdapter,
                                               BranchAdapter branchAdapter) {
        List<ActionAdapterInterface> actionAdapterInterfaceList = new ArrayList<>();
        actionAdapterInterfaceList.add(dialAdapter);
        actionAdapterInterfaceList.add(sayAlphaAdapter);
        actionAdapterInterfaceList.add(gotoAdapter);
        actionAdapterInterfaceList.add(branchAdapter);
        return new ActionServiceImpl(actionAdapterInterfaceList);
    }

    @Bean
    public DialPlanConfigAdapter dialPlanConfigAdapter(SipClientRepository sipClientRepository,
                                                       DialPlanRepository dialPlanRepository,
                                                       DialRepository dialRepository,
                                                       SayAlphaRepository sayAlphaRepository,
                                                       GotoRepository gotoRepository,
                                                       BranchRepository branchRepository) {
        return new DialPlanConfigAdapter(
                sipClientRepository,
                dialPlanRepository,
                dialRepository,
                sayAlphaRepository,
                gotoRepository,
                branchRepository);
    }

    @Bean
    public SipClientConfigAdapter sipClientConfigAdapter(SipClientRepository sipClientRepository) {
        return new SipClientConfigAdapter(sipClientRepository);
    }

    @Bean
    public SayAlphaAdapter sayAlphaAdapter(SayAlphaRepository sayAlphaRepository) {
        return new SayAlphaAdapter(sayAlphaRepository);
    }

    @Bean
    public DialAdapter dialAdapter(DialRepository dialRepository) {
        return new DialAdapter(dialRepository);
    }

    @Bean
    public GotoAdapter gotoAdapter(GotoRepository gotoRepository, DialPlanRepository dialPlanRepository) {
        return new GotoAdapter(gotoRepository, dialPlanRepository);
    }

    @Bean
    public BranchAdapter brancAdapter(BranchRepository branchRepository, DialPlanRepository dialPlanRepository,
                                      BranchDialPlanRepository branchDialPlanRepository) {
        return new BranchAdapter(branchRepository, dialPlanRepository, branchDialPlanRepository);
    }
}
