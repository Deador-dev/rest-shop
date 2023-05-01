package com.deador.restshop.config;

import com.deador.restshop.security.CustomUserDetailsService;
import com.deador.restshop.security.JwtFilter;
import com.deador.restshop.security.RestAuthenticationEntryPoint;
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
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(1)
public class SecurityConfig {
    private final JwtFilter jwtFilter;
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public SecurityConfig(JwtFilter jwtFilter,
                          CustomUserDetailsService customUserDetailsService) {
        this.jwtFilter = jwtFilter;
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
                .csrf().disable().cors()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().authenticationEntryPoint(new RestAuthenticationEntryPoint())
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/", "/category/*", "/categories", "/smartphone/*", "/smartphones",
                        "/user/*", "/shop/**", "/search/**", "/verify*").permitAll()
                .antMatchers("/admin/**", "/users/**").hasRole("ADMIN")
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/signout")).logoutSuccessUrl("/signin")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("accessToken");
        return http.build();
    }
}
