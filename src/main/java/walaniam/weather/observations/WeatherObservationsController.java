package walaniam.weather.observations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import walaniam.weather.observations.dto.WeatherData;

import javax.validation.constraints.Pattern;

@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping(path = "weather/observations/v1")
public class WeatherObservationsController {

    private static final String HOST_PATTERN = "(((?!-)[A-Za-z0-9-]{1,63}(?<!-)\\.)+[A-Za-z]{2,6}" // Domain name
            + "|"
            + "localhost" // localhost
            + "|"
            + "(([0-9]{1,3}\\.){3})[0-9]{1,3})$"; // Ip

    private final WeatherObservationsService observationsService;

    @PostMapping(consumes = "text/plain")
    public ResponseEntity<Void> post(
            @Pattern(regexp = HOST_PATTERN, message = "Invalid DNS name / IP")
            @RequestHeader("W_STATION_HOST") String stationHost,
            @RequestBody String body) {
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
