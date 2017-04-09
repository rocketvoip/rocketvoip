package ch.zhaw.psit4.security.config;

import ch.zhaw.psit4.data.jpa.repositories.AdminRepository;
import ch.zhaw.psit4.security.auxiliary.SecurityConstants;
import ch.zhaw.psit4.security.auxiliary.UserDetailsServiceImpl;
import ch.zhaw.psit4.security.jwt.TokenAuthenticationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

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

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.addExposedHeader("Content-Type");
        config.addExposedHeader(SecurityConstants.AUTH_HEADER_NAME);
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}
