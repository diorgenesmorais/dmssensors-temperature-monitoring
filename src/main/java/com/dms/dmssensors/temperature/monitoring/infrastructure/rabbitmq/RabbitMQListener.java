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

import static com.dms.dmssensors.temperature.monitoring.infrastructure.rabbitmq.RabbitMQConfig.QUEUE_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQListener {

    private final TemperatureMonitoringService temperatureMonitoringService;

    @SneakyThrows
    @RabbitListener(queues = QUEUE_NAME, concurrency = "2-3")
    public void listenerMonitoring(@Payload TemperatureLogData message) {
        temperatureMonitoringService.processTemperatureReading(message);

        // Simulate processing time
        Thread.sleep(Duration.ofSeconds(10));
    }
}
