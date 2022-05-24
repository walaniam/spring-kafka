package walaniam.weather.observations.db;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
public class WeatherSnapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private ZonedDateTime dateTime;
    private String outsideTemperature;
    private String insideTemperature;
    private String pressureHpa;

    @JoinColumn(name = "station_id", insertable = false, updatable = false)
    @ManyToOne(targetEntity = WeatherStation.class, fetch = FetchType.EAGER)
    private WeatherStation station;

    @Column(name = "station_id")
    private Long stationId;
}
