package walaniam.weather.observations;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import walaniam.weather.BaseIT;
import walaniam.weather.observations.db.WeatherSnapshotRepository;
import walaniam.weather.observations.db.WeatherStation;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class ObservationsFetcherIT extends BaseIT {

    @Autowired
    private WireMockServer wireMockServer;
    @Autowired
    private WeatherStationsConfig stationsConfig;
    @Autowired
    private ObservationsFetcher scheduler;
    @Autowired
    private WeatherSnapshotRepository repository;

    @Test
    void fetchAndSave() {

        // given
        stationsConfig.getAll().forEach(station -> {
            var outsideTemp = outsideTemperatureOf(station);
            wireMockServer.stubFor(get(urlEqualTo(station.getPath()))
                    .willReturn(aResponse()
                            .withHeader("Content-Type", "text/plain")
                            .withBody(String.format("20220523 135315,%s,21.54,980.04", outsideTemp))
                    )
            );
        });

        // when
        scheduler.fetchAndSave();

        // then
        assertThat(repository.count()).isEqualTo(stationsConfig.getAll().size());
        repository.findAll().forEach(snapshot -> {
            var station = snapshot.getStation();
            assertThat(snapshot.getOutsideTemperature()).isEqualTo(outsideTemperatureOf(station));
        });
    }

    private static String outsideTemperatureOf(WeatherStation station) {
        return (station.getId() + 10) + ".53";
    }
}