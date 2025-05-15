package com.dms.dmssensors.temperature.monitoring.infrastructure.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "temperature-monitoring.process-temperature.v1.q";

    @Bean
    RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    Queue temperatureMonitoringQueue() {
        return QueueBuilder.durable(QUEUE_NAME)
                .build();
    }

    FanoutExchange exchange() {
        return ExchangeBuilder.fanoutExchange("temperature-processing.temperature-received.v1.e")
                .build();
    }

    @Bean
    Binding binding() {
        return BindingBuilder.bind(temperatureMonitoringQueue()).to(exchange());
    }

    @Bean
    Jackson2JsonMessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
