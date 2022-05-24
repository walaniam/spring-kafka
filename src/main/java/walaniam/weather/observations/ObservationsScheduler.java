package walaniam.weather.observations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class ObservationsScheduler {

    private final WeatherStationClient client;
    private final String stationEndpoint;

    public ObservationsScheduler(WeatherStationClient client,
                                 @Value("${app.weather.station.endpoint}") String stationEndpoint) {
        this.client = client;
        this.stationEndpoint = stationEndpoint;
    }

    @Scheduled(initialDelay = 10, fixedDelay = 10, timeUnit = TimeUnit.SECONDS)
    public void fetch() {
        log.debug("Scheduled fetching on client {}", client);
        var snapshot = client.fetch(stationEndpoint);
        log.info("Fetched: {}", snapshot);
    }
}
