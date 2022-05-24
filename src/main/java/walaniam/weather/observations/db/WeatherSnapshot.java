package walaniam.weather.observations.db;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.ZonedDateTime;

@Entity
public class WeatherSnapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private ZonedDateTime dateTime;
    private String outsideTemperature;
    private String insideTemperature;
    private String pressurehPa;
    private WeatherStation station;
}
