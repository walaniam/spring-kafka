package walaniam.weather.observations.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"host", "port", "path"}))
public class WeatherStation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String host;
    @Column(nullable = false)
    private int port;
    @Column(nullable = false)
    private String path;
    @Column(nullable = false)
    private String protocol;
    private String location;

    public String getEndpoint() {
        return UriComponentsBuilder.newInstance()
                .host(host)
                .port(port)
                .path(path)
                .scheme(protocol)
                .toUriString();
    }
}
