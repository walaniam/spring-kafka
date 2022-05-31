package walaniam.weather.observations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import walaniam.weather.observations.db.WeatherStation;
import walaniam.weather.observations.db.WeatherStationRepository;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Data
@Configuration
@ConfigurationProperties(prefix = "app.weather")
class WeatherStationsConfig {

    private final WeatherStationRepository repository;
    private final List<WeatherStation> stations;

    @PostConstruct
    protected void initialize() {
        saveStations();
    }

    private void saveStations() {
        log.info("Saving stations from static config: {}", stations);
        stations.forEach(station -> {
            try {
                repository.saveAndFlush(station);
            } catch (Exception e) {
                log.warn("Save failed", e);
            }
        });
    }

    public Collection<WeatherStation> getAll() {
        return repository.findAll();
    }
}
