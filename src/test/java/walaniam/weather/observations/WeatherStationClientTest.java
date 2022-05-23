package walaniam.weather.observations;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WeatherStationClientTest {

    private static final int PORT = 9999;

    @Rule
    public final WireMockRule wireMockRule = new WireMockRule(PORT);

    @Test
    void fetch() {
    }
}