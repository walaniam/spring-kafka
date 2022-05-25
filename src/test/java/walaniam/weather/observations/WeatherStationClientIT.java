package walaniam.weather.observations;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import walaniam.weather.BaseIT;
import walaniam.weather.observations.db.WeatherStation;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class WeatherStationClientIT extends BaseIT {

    @Value("${app.weather.http.client.timeout.millis}")
    private int stationTimeout;

    @Autowired
    private WireMockServer wireMockServer;
    @Autowired
    private WeatherStationsConfig stationsConfig;
    @Autowired
    private WeatherStationClient underTest;
    private WeatherStation station;

    @BeforeEach
    public void beforeTest() {
        station = stationsConfig.getStations().stream()
                .findFirst()
                .orElseThrow();
    }

    @Test
    void shouldFetchAndParseObservationCsv() {

        wireMockServer.stubFor(get(urlEqualTo(station.getPath()))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/plain")
                        .withBody("20220523 135315,15.53,21.54,980.04")
                )
        );

        var snapshot = underTest.fetch(station.getEndpoint());

        assertThat(snapshot.getDateTime().toEpochSecond()).isEqualTo(1653313995L);
        assertThat(snapshot.getOutsideTemperature()).isEqualTo("15.53");
        assertThat(snapshot.getInsideTemperature()).isEqualTo("21.54");
        assertThat(snapshot.getPressureHpa()).isEqualTo("980.04");
    }

    @Test
    void shouldFailOnIllegalCsv() {

        wireMockServer.stubFor(get(urlEqualTo(station.getPath()))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/plain")
                        .withBody("20220523 135315,15.53")
                )
        );

        assertThrows(IllegalArgumentException.class, () -> underTest.fetch(station.getEndpoint()));
    }

    @Test
    void shouldThrowNotFoundException() {

        wireMockServer.stubFor(get(urlEqualTo(station.getPath()))
                .willReturn(aResponse().withStatus(404))
        );

        assertThrows(HttpClientErrorException.NotFound.class, () -> underTest.fetch(station.getEndpoint()));
    }

    @Test
    void shouldThrowResourceAccessExceptionOnTimeout() {

        wireMockServer.stubFor(get(urlEqualTo(station.getPath()))
                .willReturn(aResponse().withFixedDelay(stationTimeout + 500))
        );

        assertThrows(ResourceAccessException.class, () -> underTest.fetch(station.getEndpoint()));
    }
}