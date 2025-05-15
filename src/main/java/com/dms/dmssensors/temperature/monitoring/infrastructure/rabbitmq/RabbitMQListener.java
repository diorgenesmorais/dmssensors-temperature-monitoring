package com.dms.dmssensors.temperature.monitoring.infrastructure.rabbitmq;

import com.dms.dmssensors.temperature.monitoring.api.model.TemperatureLogData;
import io.hypersistence.tsid.TSID;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;

import static com.dms.dmssensors.temperature.monitoring.infrastructure.rabbitmq.RabbitMQConfig.QUEUE_NAME;

@Slf4j
@Component
public class RabbitMQListener {

    @SneakyThrows
    @RabbitListener(queues = QUEUE_NAME)
    public void listenerMonitoring(@Payload TemperatureLogData message, @Headers Map<String, Object> headers) {
        TSID sensorId = message.getSensorId();
        Double value = message.getValue();
        log.info("Temperature updated: sensorId: {} value: {}", sensorId, value);
        log.info("Headers: {}", headers);

        // Simulate processing time
        Thread.sleep(Duration.ofSeconds(10));
    }
}
