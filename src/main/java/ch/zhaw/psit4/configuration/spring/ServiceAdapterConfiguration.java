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
