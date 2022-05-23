package walaniam.weather.observations;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import walaniam.weather.WireMockInitializer;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ContextConfiguration(initializers = {WireMockInitializer.class})
class WeatherStationClientIT {

    @Value("${app.weather.http.client.timeout.millis}")
    private int stationTimeout;

    @Autowired
    private WireMockServer wireMockServer;
    @Autowired
    private WeatherStationClient stationClient;

    @Test
    void shouldFetchAndParseObservationCsv() {

        wireMockServer.stubFor(get(urlEqualTo("/"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/plain")
                        .withBody("20220523 135315,15.53,21.54,980.04")
                )
        );

        var snapshot = stationClient.fetch();

        assertThat(snapshot.getDateTime().toEpochSecond()).isEqualTo(1653313995L);
        assertThat(snapshot.getOutsideTemperature()).isEqualTo("15.53");
        assertThat(snapshot.getInsideTemperature()).isEqualTo("21.54");
        assertThat(snapshot.getPressurehPa()).isEqualTo("980.04");
    }

    @Test
    void shouldFailOnIllegalCsv() {

        wireMockServer.stubFor(get(urlEqualTo("/"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/plain")
                        .withBody("20220523 135315,15.53")
                )
        );

        assertThrows(IllegalArgumentException.class, () -> stationClient.fetch());
    }

    @Test
    void shouldThrowNotFoundException() {

        wireMockServer.stubFor(get(urlEqualTo("/"))
                .willReturn(aResponse().withStatus(404))
        );

        assertThrows(HttpClientErrorException.NotFound.class, () -> stationClient.fetch());
    }

    @Test
    void shouldThrowResourceAccessExceptionOnTimeout() {

        wireMockServer.stubFor(get(urlEqualTo("/"))
                .willReturn(aResponse().withFixedDelay(stationTimeout + 500))
        );

        assertThrows(ResourceAccessException.class, () -> stationClient.fetch());
    }
}