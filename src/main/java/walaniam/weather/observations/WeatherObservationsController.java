package walaniam.weather.observations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import walaniam.weather.observations.db.WeatherSnapshot;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "weather/stats/v1")
public class WeatherObservationsController {

    private final WeatherObservationsService observationsService;

    @GetMapping("latest")
    public ResponseEntity<Page<WeatherSnapshot>> getLatest(@RequestParam(defaultValue = "10") int count) {
        var page = observationsService.findLatest(count);
        return ResponseEntity.ok(page);
    }
}
