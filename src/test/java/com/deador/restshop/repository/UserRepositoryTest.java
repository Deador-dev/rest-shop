package com.deador.restshop.repository;

import com.deador.restshop.RestShopApplication;
import com.deador.restshop.initializer.IntegrationTestBase;
import com.deador.restshop.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
//@SpringBootTest(classes = RestShopApplication.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Testcontainers
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Value("${database.name}")
    private static String databaseName;

    @Value("${DB_USERNAME}")
    private static String databaseUsername;

    @Value("${DB_PASSWORD}")
    private static String databasePassword;

//    private static final DockerImageName myImage = DockerImageName.parse("mysql:8.0.33").asCompatibleSubstituteFor("mysql");
    @Container
    private static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>(DockerImageName.parse("mysql:8.0.33"))
            .withDatabaseName(databaseName)
            .withUsername(databaseUsername)
            .withPassword(databasePassword);

    // FIXME: 27.06.2023 need to fix
    @DynamicPropertySource
    private static void setupProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> mySQLContainer.getJdbcUrl());
        registry.add("spring.datasource.driverClassName", () -> mySQLContainer.getDriverClassName());
        registry.add("spring.datasource.username", () -> mySQLContainer.getUsername());
        registry.add("spring.datasource.password", () -> mySQLContainer.getPassword());
    }

    @BeforeAll
    static void beforeAll() {
        System.out.println("Test container status: " + mySQLContainer.isRunning());
    }

    @Test
    void testMySQLContainerIsRunning() {
        assertThat(mySQLContainer.isRunning()).isTrue();
    }

//    @Test
//    void findByRoleName() {
//        System.out.println("IN TEST");
//        Optional<List<User>> userList = userRepository.findByRoleName("ROLE_ADMIN");
//        assertThat(userList.get().size()).isEqualTo(1);
//    }
}