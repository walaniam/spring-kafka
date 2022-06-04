package walaniam.weather.observations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import walaniam.weather.observations.db.WeatherSnapshot;
import walaniam.weather.observations.db.WeatherSnapshotRepository;
import walaniam.weather.observations.db.WeatherStation;
import walaniam.weather.observations.db.WeatherStationRepository;
import walaniam.weather.observations.dto.WeatherData;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequiredArgsConstructor
@Component
public class WeatherObservationsService {

    final AtomicInteger counter = new AtomicInteger();

    private final WeatherSnapshotRepository weatherSnapshotRepository;
    private final WeatherStationRepository stationRepository;

    public Page<WeatherSnapshot> findLatest(int count) {
        return weatherSnapshotRepository.findAllWithPagination(Pageable.ofSize(count));
    }

    public void save(String stationHost, WeatherData data) {

        var station = Optional.ofNullable(stationRepository.findByHostName(stationHost))
                .orElseGet(() -> {
                    var s = new WeatherStation();
                    s.setHost(stationHost);
                    return stationRepository.saveAndFlush(s);
                });

        var snapshot = WeatherSnapshot.builder()
                .stationId(station.getId())
                .dateTime(data.getDateTime())
                .outsideTemperature(data.getOutsideTemperature())
                .insideTemperature(data.getInsideTemperature())
                .pressureHpa(data.getPressureHpa())
                .build();

        log.debug("Saving counter={}, {}", counter.incrementAndGet(), snapshot);
        weatherSnapshotRepository.saveAndFlush(snapshot);
    }

//    private long stationId() {
//        var all = stationRepository.findAll();
//        if (all.size() != 1) {
//            // TODO
//            throw new IllegalStateException("More than one station");
//        }
//        return all.get(0).getId();
//    }
}
