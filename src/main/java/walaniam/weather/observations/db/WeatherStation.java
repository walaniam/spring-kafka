package walaniam.weather.observations.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.*;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"host"}))
public class WeatherStation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String host;

    private Integer port;
    private String path;
    private String protocol;
    private String location;

    public Optional<String> getEndpoint() {

        if (protocol == null || port == null) {
            return Optional.empty();
        }

        var uriBuilder = UriComponentsBuilder.newInstance()
                .host(host)
                .port(port)
                .scheme(protocol);
        if (path != null) {
            uriBuilder.path(path);
        }

        return Optional.of(uriBuilder.toUriString());
    }
}
