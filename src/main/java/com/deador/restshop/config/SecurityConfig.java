package com.deador.restshop.config;

import com.deador.restshop.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(1)
public class SecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/", "/category/*", "/categories", "/smartphone/*", "/smartphones",
                        "/signup", "/user/*", "/shop/**", "/search/**", "/verify*")
                .permitAll()
                .antMatchers(HttpMethod.POST, "/signup").permitAll()
                // TODO: 13.04.2023 .antMatchers(/swagger)
                .antMatchers(HttpMethod.GET, "/users/**").hasRole("ADMIN")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
//                .loginPage("/login")
                .permitAll()
                .defaultSuccessUrl("/shop")
                // FIXME: 13.04.2023 need to use ?
//                .usernameParameter("email")
//                .passwordParameter("password")
                .and()
                .logout()
                // FIXME: 20.04.2023 need to change /logout to /signout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/signin")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .and()
                .csrf().disable().cors();

        return http.build();
    }
}
