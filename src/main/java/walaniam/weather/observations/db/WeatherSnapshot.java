package walaniam.weather.observations.db;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "unique_date_station", columnNames = {"dateTime", "stationId"}))
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
