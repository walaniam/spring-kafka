package walaniam.weather.config;

import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
@EnableRetry
@EnableScheduling
public class AppConfig {

    @Bean
    public OkHttpClient okHttpClient(@Value("${app.weather.http.client.timeout.millis}") int timeoutMillis) {

        var pool = new ConnectionPool(5, 20, TimeUnit.SECONDS);

        var builder = new OkHttpClient.Builder();
        builder.connectionPool(pool);
        builder.connectTimeout(timeoutMillis, TimeUnit.MILLISECONDS);
        builder.readTimeout(timeoutMillis, TimeUnit.MILLISECONDS);
        builder.writeTimeout(timeoutMillis, TimeUnit.MILLISECONDS);
        builder.retryOnConnectionFailure(true);

        return builder.build();
    }

    @Bean
    public RestTemplate restTemplate(OkHttpClient client) {
        log.info("Initializing RestTemplate with client={}", client);
        var template = new RestTemplate();
        template.setRequestFactory(new OkHttp3ClientHttpRequestFactory(client));
        return template;
    }
}
