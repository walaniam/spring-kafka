package walaniam.weather.observations;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import walaniam.weather.BaseIT;

public class End2EndIT extends BaseIT {

    @Autowired
    private WireMockServer wireMockServer;

    @Autowired
    private WeatherStationsConfig stations;

    @Test
    void shouldFetchAndStoreWeatherSnapshots() {

        System.out.println(dbContainer.getJdbcUrl());
    }
}
