package walaniam.weather.observations.db;

import lombok.Builder;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Builder
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"dateTime", "stationId"}))
public class WeatherSnapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private ZonedDateTime dateTime;
    private String outsideTemperature;
    private String insideTemperature;
    private String pressureHpa;

    @JoinColumn(name = "stationId", insertable = false, updatable = false)
    @ManyToOne(targetEntity = WeatherStation.class, fetch = FetchType.EAGER)
    private WeatherStation station;

    @Column(nullable = false)
    private Long stationId;
}
