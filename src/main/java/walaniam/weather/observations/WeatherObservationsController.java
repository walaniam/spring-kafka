package walaniam.weather.observations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import walaniam.weather.observations.dto.WeatherData;

@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping(path = "weather/observations/v1")
public class WeatherObservationsController {

    private final WeatherObservationsService observationsService;

    @PostMapping(consumes = "text/plain")
    public ResponseEntity<Void> post(@RequestBody String body) {
        log.info("Request: {}", body);
        var data = WeatherData.of(body);
        try {
            var stationHost = "192.168.0.137"; // todo
            observationsService.save(stationHost, data);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Save error " + e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
