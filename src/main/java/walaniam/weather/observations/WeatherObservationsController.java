package walaniam.weather.observations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import walaniam.weather.observations.dto.WeatherData;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "weather/observations/v1")
public class WeatherObservationsController {

    private final WeatherObservationsService observationsService;

    @PostMapping(consumes = "text/plain")
    public ResponseEntity<Void> post(@RequestHeader("W_STATION_HOST") String stationHost, @RequestBody String body) {
        log.info("Request: {}", body);
        var data = WeatherData.of(body);
        try {
            observationsService.save(stationHost, data);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Save error " + e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
