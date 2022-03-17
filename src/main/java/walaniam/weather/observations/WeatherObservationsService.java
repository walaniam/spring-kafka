package walaniam.weather.observations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import walaniam.weather.observations.dto.WeatherSnapshot;

@Slf4j
@Component
public class WeatherObservationsService {

    public void add(WeatherSnapshot snapshot) {
        log.info("Adding snapshot: {}", snapshot);
    }
}
