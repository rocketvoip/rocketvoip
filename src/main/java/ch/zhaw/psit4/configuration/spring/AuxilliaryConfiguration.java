package ch.zhaw.psit4.configuration.spring;

import ch.zhaw.psit4.data.jpa.auxiliary.CreateInitialAdministrator;
import ch.zhaw.psit4.data.jpa.repositories.AdminRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Rafael Ostertag
 */
@Configuration
public class AuxilliaryConfiguration {
    @Bean
    public CreateInitialAdministrator createInitialAdministrator(AdminRepository adminRepository) {
        return new CreateInitialAdministrator(adminRepository);
    }
}
