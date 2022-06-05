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

@Slf4j
@RequiredArgsConstructor
@Component
public class WeatherObservationsService {

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

        log.debug("Saving {}", snapshot);
        weatherSnapshotRepository.saveAndFlush(snapshot);
    }
}
