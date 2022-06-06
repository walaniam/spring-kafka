package walaniam.weather.observations.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherStationRepository extends JpaRepository<WeatherStation, Long> {

    @Query("SELECT s FROM WeatherStation s WHERE s.host = :host")
    WeatherStation findByHostName(String host);
}
