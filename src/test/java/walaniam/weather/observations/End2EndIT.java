package walaniam.weather.observations;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;
import walaniam.weather.WireMockInitializer;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {WireMockInitializer.class})
public class End2EndIT {

    @Value("${app.weather.station.endpoint}")
    private String stationEndpoint;
    @Autowired
    private WireMockServer wireMockServer;

    @Test
    void shouldFetchAndStoreWeatherSnapshots() {

    }
}
