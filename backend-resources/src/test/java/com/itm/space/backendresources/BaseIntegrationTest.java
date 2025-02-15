package com.itm.space.backendresources;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public abstract class BaseIntegrationTest {
    @Container
    static DockerComposeContainer<?> environment =
            new DockerComposeContainer<>(new File("src/test/docker-compose-test.yml"))
                    .withExposedService("db", 5432)
                    .withExposedService("kc", 8080);

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {

        String jdbcUrl = "jdbc:postgresql://" + environment.getServiceHost("db", 5432)
                + ":" + environment.getServicePort("db", 5432) + "/keycloak_db";
        registry.add("spring.datasource.url", () -> jdbcUrl);
        registry.add("spring.datasource.username", () -> "my_admin");
        registry.add("spring.datasource.password", () -> "my_password");
        registry.add("keycloak.realm", () -> "ITM");
    }
}
