package walaniam.weather.observations.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherStation {
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String address;
    private String location;
}
