package walaniam.weather.observations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import walaniam.weather.observations.dto.WeatherSnapshot;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "weather/observations/v1")
public class WeatherObservationsController {

    private final WeatherObservationsService observationsService;

    @PostMapping
    public ResponseEntity<Void> addSnapshot(@RequestBody WeatherSnapshot snapshot) {
        observationsService.add(snapshot);
        return ResponseEntity.ok().build();
    }
}
