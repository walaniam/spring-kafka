package walaniam.weather.observations.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class WeatherSnapshot {
    private long timestamp;
    private String temperatureValue;
    private TemperatureScale scale;
}
