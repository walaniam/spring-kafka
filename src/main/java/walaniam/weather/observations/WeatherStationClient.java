package walaniam.weather.observations;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import walaniam.weather.observations.dto.WeatherSnapshot;

import java.util.regex.Pattern;

import static walaniam.weather.common.time.DateTimeUtils.fromUtcString;

@Slf4j
@Service
@ToString(of = "stationEndpoint")
public class WeatherStationClient {

    private final RestTemplate restTemplate;
    private final String stationEndpoint;

    public WeatherStationClient(RestTemplate restTemplate,
                                @Value("${app.weather.station.endpoint}") String stationEndpoint) {
        this.restTemplate = restTemplate;
        this.stationEndpoint = stationEndpoint;
    }

    @Retryable(value = ResourceAccessException.class, maxAttempts = 3, backoff = @Backoff(delay = 250))
    public WeatherSnapshot fetch() {

        log.info("Fetching snapshot from: {}", stationEndpoint);
        ResponseEntity<String> response = restTemplate.getForEntity(stationEndpoint, String.class);
        log.info("Response: {}", response);

        var csv = response.getBody();
        String[] data =  Pattern.compile(",").splitAsStream(csv)
                .map(String::trim)
                .toArray(size -> new String[size]);

        if (data.length != 4) {
            throw new IllegalArgumentException("Incorrect csv: " + csv);
        }

        return WeatherSnapshot.builder()
                .dateTime(fromUtcString(data[0]))
                .outsideTemperature(data[1])
                .insideTemperature(data[2])
                .pressurehPa(data[3])
                .build();
    }
}
