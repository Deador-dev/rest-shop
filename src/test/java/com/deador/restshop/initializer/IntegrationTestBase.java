package com.deador.restshop.initializer;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.ContextConfiguration;

import javax.transaction.Transactional;

@ContextConfiguration(initializers = MySQLInitializer.Initializer.class)
@Transactional
public abstract class IntegrationTestBase {
    @BeforeAll
    static void init() {
        MySQLInitializer.mySQLContainer.start();
    }
}
