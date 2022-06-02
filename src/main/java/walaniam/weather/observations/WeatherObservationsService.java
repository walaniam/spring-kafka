package walaniam.weather.observations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import walaniam.weather.observations.db.WeatherSnapshot;
import walaniam.weather.observations.db.WeatherSnapshotRepository;

@Slf4j
@RequiredArgsConstructor
@Component
public class WeatherObservationsService {

    private final WeatherSnapshotRepository repository;

    public Page<WeatherSnapshot> findLatest(int count) {
        return repository.findAllWithPagination(Pageable.ofSize(count));
    }
}
