package com.deador.restshop.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.deador.restshop")
public class ApplicationConfig {
//    @Bean
//    public PasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
