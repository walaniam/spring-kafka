package walaniam.weather.observations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import walaniam.weather.observations.db.WeatherStation;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class ObservationsScheduler {

    private final WeatherStationClient client;
    private final WeatherStationsConfig stationsConfig;

    @Scheduled(initialDelay = 10, fixedDelay = 10, timeUnit = TimeUnit.SECONDS)
    public void fetch() {
        log.debug("Scheduled execution, fetching with client {}", client);
        stationsConfig.getStations().stream().map(WeatherStation::getEndpoint).forEach(endpoint -> {
            var snapshot = client.fetch(endpoint);
            log.info("Fetched: {}", snapshot);
        });
    }
}
