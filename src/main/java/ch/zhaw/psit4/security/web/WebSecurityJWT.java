package ch.zhaw.psit4.security.web;

import ch.zhaw.psit4.security.auxiliary.SecurityConstants;
import ch.zhaw.psit4.security.jwt.TokenAuthenticationService;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Setup Web Security using JWT
 *
 * @author Rafael Ostertag
 */
@EnableWebSecurity
public class WebSecurityJWT extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final TokenAuthenticationService tokenAuthenticationService;
    private final PasswordEncoder passwordEncoder;

    public WebSecurityJWT(UserDetailsService userDetailsService, TokenAuthenticationService
            tokenAuthenticationService, PasswordEncoder passwordEncoder) {
        super(true);
        this.userDetailsService = userDetailsService;
        this.tokenAuthenticationService = tokenAuthenticationService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers(HttpMethod.OPTIONS, "/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .cors().and()
                .authorizeRequests()
                .antMatchers("/v1/login").permitAll()
                .antMatchers("/v1/sipclients/**").hasRole(SecurityConstants.COMPANY_ADMIN_ROLE_NAME)
                .antMatchers("/v1/companies/**").hasRole(SecurityConstants.CONFIG_ADMIN_ROLE_NAME)
                .antMatchers("/v1/configuration/**").hasRole(SecurityConstants.CONFIG_ADMIN_ROLE_NAME)
                .anyRequest().authenticated();

        http.addFilterBefore(
                new StatelessLoginFilter(
                        "/v1/login",
                        tokenAuthenticationService,
                        userDetailsService,
                        authenticationManager()),
                UsernamePasswordAuthenticationFilter.class
        );

        http.addFilterBefore(
                new StatelessAuthenticationFilter(tokenAuthenticationService),
                UsernamePasswordAuthenticationFilter.class
        );
    }
}
