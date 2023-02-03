package com.example.authclient.security.config;

import com.example.authclient.security.keycloak.KeycloakService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    private final KeycloakService keycloakService;

    public SecurityConfig(KeycloakService keycloakService) {
        this.keycloakService = keycloakService;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
    
    @Override
    protected void configure(HttpSecurity http)throws Exception {
        http.oauth2Login()
            .loginPage("/login")
            .permitAll();
        http.authorizeRequests()
            .anyRequest().authenticated();
        http.logout()
            .addLogoutHandler(logoutFromAuthServer())
            .invalidateHttpSession(true)
            .permitAll();
    }

    private LogoutHandler logoutFromAuthServer() {
        return (request, response, authentication) -> {
            keycloakService.logout();
        };
    }
}
