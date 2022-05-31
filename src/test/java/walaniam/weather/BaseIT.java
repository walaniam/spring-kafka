package walaniam.weather;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {WireMockInitializer.class})
@Slf4j
public abstract class BaseIT {

    @Container
    protected static final MySQLContainer<?> dbContainer = new MySQLContainer<>(
            DockerImageName.parse("mysql/mysql-server:8.0-amd64").asCompatibleSubstituteFor("mysql"))
            .withLogConsumer(new Slf4jLogConsumer(log))
            .withAccessToHost(true)
            .withUsername("inmemory")
            .withPassword("inmemory");

    @DynamicPropertySource
    static void applyTestProperties(DynamicPropertyRegistry registry) {
        // mysql
        registry.add("spring.datasource.url", dbContainer::getJdbcUrl);
        registry.add("spring.datasource.password", dbContainer::getPassword);
        registry.add("spring.datasource.username", dbContainer::getUsername);
        // app
        registry.add("app.weather.http.client.timeout.millis", () -> 500);
    }
}
