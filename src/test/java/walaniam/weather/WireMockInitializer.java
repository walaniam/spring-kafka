package walaniam.weather;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;

@Slf4j
public class WireMockInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {

        log.info("Initializing wiremock server");

        var server = new WireMockServer(new WireMockConfiguration().dynamicPort());
        server.start();
        var port = server.port();

        applicationContext.getBeanFactory().registerSingleton("wireMockServer", server);

        log.info("wiremock server started on port {}", port);

        TestPropertyValues
                .of("app.weather.station.endpoint=http://127.0.0.1:" + port)
                .applyTo(applicationContext);

        applicationContext.addApplicationListener(event -> {
            if (event instanceof ContextClosedEvent) {
                log.info("Stopping wiremock server");
                server.stop();
            }
        });
    }
}
