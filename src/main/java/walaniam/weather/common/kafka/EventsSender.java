package walaniam.weather.common.kafka;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Function;

@RequiredArgsConstructor(staticName = "of")
@AllArgsConstructor(staticName = "of")
@Slf4j
public class EventsSender<T> implements Consumer<T> {

    private final KafkaTemplate<String, T> kafkaTemplate;
    private final String eventsTopic;
    private final Function<T, String> keyFunction;
    private Function<T, Integer> partitionFunction;

    @Override
    public void accept(T event) {
        var key = keyFunction.apply(event);
        var partition = (partitionFunction != null) ? partitionFunction.apply(event) : null;
        log.debug("Sending [{}] on partition [{}]", key, partition);
        try {
            var sendResult = kafkaTemplate.send(eventsTopic, partition, key, event).get();
            log.info("Sent record: {}", sendResult);
        } catch (InterruptedException e) {
            log.error("Failed to send", e);
        } catch (ExecutionException e) {
            log.error("Failed to send", e);
        }
    }
}
