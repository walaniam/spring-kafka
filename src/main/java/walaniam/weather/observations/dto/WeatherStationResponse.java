package walaniam.weather.observations.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class WeatherStationResponse {
    private ZonedDateTime dateTime;
    private String outsideTemperature;
    private String insideTemperature;
    private String pressurehPa;
}
