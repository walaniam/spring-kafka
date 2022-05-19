package walaniam.weather.observations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Slf4j
@Component
public class ObservationsScheduler {

    private final WeatherStationClient client;

    @Scheduled(fixedDelay = 10, timeUnit = TimeUnit.SECONDS)
    public void fetch() {
        var snapshot = client.fetch();
        log.info("Fetched: {}", snapshot);
    }
}
