package walaniam.weather.observations;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import walaniam.weather.common.kafka.EventsSender;
import walaniam.weather.observations.dto.WeatherSnapshot;

@Configuration
public class KafkaConfig {

    @Value("${app.weather.topic.name}")
    private String topic;

    @Value("${app.weather.topic.partitions}")
    private int partitions;

    @Bean
    public NewTopic dataTopic() {
        return new NewTopic(topic, partitions, (short) 1);
    }

    @Bean
    public EventsSender<WeatherSnapshot> weatherSnapshotEventsSender(KafkaTemplate<String, WeatherSnapshot> kafkaTemplate) {
        return EventsSender.of(kafkaTemplate, topic, snapshot -> String.valueOf(snapshot.getTimestamp()), null);
    }
}
