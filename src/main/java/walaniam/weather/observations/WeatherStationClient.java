package walaniam.weather.observations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import walaniam.weather.observations.dto.WeatherStationResponse;

import java.util.regex.Pattern;

import static walaniam.weather.common.time.DateTimeUtils.fromUtcString;

@Slf4j
@RequiredArgsConstructor
@Service
public class WeatherStationClient {

    private final RestTemplate restTemplate;

    @Retryable(value = ResourceAccessException.class, maxAttempts = 3, backoff = @Backoff(delay = 250))
    public WeatherStationResponse fetch(String stationEndpoint) {

        log.info("Fetching snapshot from: {}", stationEndpoint);
        ResponseEntity<String> response = restTemplate.getForEntity(stationEndpoint, String.class);
        log.info("Response: {}", response);

        var csv = response.getBody();
        var data =  Pattern.compile(",").splitAsStream(csv)
                .map(String::trim)
                .toArray(size -> new String[size]);

        if (data.length != 4) {
            throw new IllegalArgumentException("Incorrect csv: " + csv);
        }

        return WeatherStationResponse.builder()
                .dateTime(fromUtcString(data[0]))
                .outsideTemperature(data[1])
                .insideTemperature(data[2])
                .pressureHpa(data[3])
                .build();
    }
}
