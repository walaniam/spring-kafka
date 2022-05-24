package walaniam.weather.observations.db;

import javax.persistence.Entity;
import java.time.ZonedDateTime;

@Entity
public class WeatherSnapshot {
    private ZonedDateTime dateTime;
    private String outsideTemperature;
    private String insideTemperature;
    private String pressurehPa;
    private WeatherStation station;
}
