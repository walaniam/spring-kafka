package walaniam.weather.observations;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import walaniam.weather.observations.db.WeatherStation;

import java.util.ArrayList;
import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.weather")
class WeatherStationsConfig {

    private final List<WeatherStation> stations = new ArrayList<>();
}
