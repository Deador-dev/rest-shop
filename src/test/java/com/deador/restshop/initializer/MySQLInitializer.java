package com.deador.restshop.initializer;

import lombok.experimental.UtilityClass;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.MySQLContainer;

@UtilityClass
public class MySQLInitializer {
    public static MySQLContainer<?> mySQLContainer = new MySQLContainer<>(" 8.0.32");

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                            "spring.datasource.url=" + mySQLContainer.getJdbcUrl(),
                            "spring.datasource.username=" + mySQLContainer.getUsername(),
                            "spring.datasource.password=" + mySQLContainer.getPassword(),
                            "spring.liquibase.enabled=true",
                            "spring.liquibase.change-log=classpath:db/changelog/db.changelog-master.xml")
                    .applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}
