package ch.zhaw.psit4.configuration.spring;

import ch.zhaw.psit4.data.jpa.repositories.*;
import ch.zhaw.psit4.services.implementation.adapters.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Rafael Ostertag
 */
@Configuration
public class ServiceAdapterConfiguration {

    @Bean
    public DialPlanConfigAdapter dialPlanConfigAdapter(SipClientRepository sipClientRepository, DialPlanRepository
            dialPlanRepository, DialRepository dialRepository, SayAlphaRepository sayAlphaRepository) {
        return new DialPlanConfigAdapter(sipClientRepository, dialPlanRepository, dialRepository, sayAlphaRepository);
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
}
