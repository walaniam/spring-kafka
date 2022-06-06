package walaniam.weather.observations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import walaniam.weather.observations.dto.WeatherData;

@Slf4j
@RequiredArgsConstructor
@Service
public class WeatherStationClient {

    private final RestTemplate restTemplate;

    @Retryable(value = ResourceAccessException.class, maxAttempts = 3, backoff = @Backoff(delay = 250))
    public WeatherData fetch(String stationEndpoint) {

        log.info("Fetching snapshot from: {}", stationEndpoint);
        ResponseEntity<String> response = restTemplate.getForEntity(stationEndpoint, String.class);
        log.info("Response: {}", response);

        var csv = response.getBody();
        return WeatherData.of(csv);
    }
}
