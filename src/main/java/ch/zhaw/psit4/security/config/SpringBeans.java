package ch.zhaw.psit4.security.config;

import ch.zhaw.psit4.data.jpa.repositories.AdminRepository;
import ch.zhaw.psit4.security.auxilliary.UserDetailsServiceImpl;
import ch.zhaw.psit4.security.jwt.TokenAuthenticationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Rafael Ostertag
 */
@Configuration
public class SpringBeans {
    @Bean
    public UserDetailsService userDetailsService(AdminRepository adminRepository) {
        return new UserDetailsServiceImpl(adminRepository);
    }

    @Bean
    public TokenAuthenticationService tokenAuthenticationService(UserDetailsService userDetailsService) {
        // TODO: Figure out what this "secretkey" is doing.
        return new TokenAuthenticationService("secretkey", userDetailsService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
