package ch.zhaw.psit4.testsupport.fixtures.database;

import ch.zhaw.psit4.data.jpa.repositories.*;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

/**
 * SpringBean configuration for database fixtures
 *
 * @author Rafael Ostertag
 */
@TestConfiguration
public class BeanConfiguration {
    @TestConfiguration
    public static class Configuration {
        @Bean
        @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
        public static DatabaseFixtureBuilder databaseFixtureBuilder(CompanyRepository companyRepository,
                                                                    AdminRepository adminRepository,
                                                                    SipClientRepository sipClientRepository,
                                                                    DialPlanRepository dialPlanRepository,
                                                                    DialRepository dialRepository,
                                                                    SayAlphaRepository sayAlphaRepository) {
            return new DatabaseFixtureBuilder(companyRepository, adminRepository, sipClientRepository,
                    dialPlanRepository, dialRepository, sayAlphaRepository);
        }
    }
}
