package walaniam.weather.observations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import walaniam.weather.observations.db.WeatherSnapshot;
import walaniam.weather.observations.db.WeatherSnapshotRepository;
import walaniam.weather.observations.db.WeatherStation;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class ObservationsFetcher {

    private final WeatherStationClient client;
    private final WeatherStationsConfig stationsConfig;
    private final WeatherSnapshotRepository repository;

    @Scheduled(initialDelay = 10, fixedDelay = 60, timeUnit = TimeUnit.SECONDS)
    public void fetchAndSave() {
        log.debug("Scheduled execution, fetching with client {}", client);

        var futures = stationsConfig.getAll().stream()
                .map(this::asyncFetch)
                .toArray(size -> new CompletableFuture[size]);

        CompletableFuture.allOf(futures).join();

        var snapshots = Arrays.stream(futures)
                .map(CompletableFuture::join)
                .map(WeatherSnapshot.class::cast)
                .collect(Collectors.toList());

        repository.saveAllAndFlush(snapshots);
    }

    private CompletableFuture<WeatherSnapshot> asyncFetch(WeatherStation station) {
        return CompletableFuture.supplyAsync(() -> {
            var endpoint = station.getEndpoint();
            var response = client.fetch(endpoint);
            return WeatherSnapshot.builder()
                    .stationId(station.getId())
                    .dateTime(response.getDateTime())
                    .outsideTemperature(response.getOutsideTemperature())
                    .insideTemperature(response.getInsideTemperature())
                    .pressureHpa(response.getPressureHpa())
                    .build();
        });
    }
}
