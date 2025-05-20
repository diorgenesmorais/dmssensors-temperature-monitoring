package com.dms.dmssensors.temperature.monitoring.infrastructure.rabbitmq;

import com.dms.dmssensors.temperature.monitoring.api.model.TemperatureLogData;
import com.dms.dmssensors.temperature.monitoring.domain.service.TemperatureMonitoringService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Duration;

import static com.dms.dmssensors.temperature.monitoring.infrastructure.rabbitmq.RabbitMQConfig.QUEUE_ALERTING;
import static com.dms.dmssensors.temperature.monitoring.infrastructure.rabbitmq.RabbitMQConfig.QUEUE_PROCESS_TEMPERATURE;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQListener {

    private final TemperatureMonitoringService temperatureMonitoringService;

    @RabbitListener(queues = QUEUE_PROCESS_TEMPERATURE, concurrency = "2-3")
    public void listenerMonitoring(@Payload TemperatureLogData message) {
        temperatureMonitoringService.processTemperatureReading(message);
    }

    @SneakyThrows
    @RabbitListener(queues = QUEUE_ALERTING, concurrency = "2-3")
    public void listenerAlerting(@Payload TemperatureLogData message) {
        log.info("Received alerting: SensorID {} Temp: {}", message.getSensorId(), message.getValue());

        // Simulate processing time
        Thread.sleep(Duration.ofSeconds(10));
    }
}
