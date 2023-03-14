/**********************************
*              @2023              *
**********************************/
package simplelogin.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import simplelogin.listener.LoginListener;
import simplelogin.listener.LogoutListener;
import simplelogin.service.UserDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private LoginListener authenticationEventListener;

    @Autowired
    private UserDetailService userDetailService;

    @Autowired
    private LogoutListener logoutListener;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        final var login = "/Login.html";
        final var homepage = "/Index.html";
        final var failure = "/Failure.html";
        final var loginPost = "/login";
        final var logoutPost = "/logout";
        final var totalAccess = new String[]{ login, failure};

        // Page security
        http.authorizeRequests()
                .antMatchers(totalAccess)
                .permitAll()
                .anyRequest()
                .authenticated()
            // logout
            .and()
                .logout()
                .logoutUrl(logoutPost)
                .addLogoutHandler(logoutListener)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            // login
            .and()
                .formLogin()
                .loginPage(login)
                .loginProcessingUrl(loginPost)
                .defaultSuccessUrl(homepage)
                .failureUrl(failure)
            // User Details impl
            .and()
                .userDetailsService(userDetailService)
                // Misc options
                .csrf()
                .disable()
                .headers()
                .frameOptions()
                .disable();

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ApplicationListener<AbstractAuthenticationEvent> authLogger() {
        return authenticationEventListener;
    }
}
