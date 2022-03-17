package walaniam.weather.observations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import walaniam.weather.common.kafka.EventsSender;
import walaniam.weather.observations.dto.WeatherSnapshot;

@Slf4j
@RequiredArgsConstructor
@Component
public class WeatherObservationsService {

    private final EventsSender<WeatherSnapshot> sender;

    public void add(WeatherSnapshot snapshot) {
        log.info("Adding snapshot: {}", snapshot);
        sender.accept(snapshot);
    }
}
