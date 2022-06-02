package walaniam.weather.observations.db;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherSnapshotRepository extends JpaRepository<WeatherSnapshot, Long> {

    @Query(value = "SELECT s FROM WeatherSnapshot s ORDER BY dateTime DESC")
    Page<WeatherSnapshot> findAllWithPagination(Pageable pageable);
}
