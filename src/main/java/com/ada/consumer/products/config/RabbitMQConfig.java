package com.ada.consumer.products.config;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração do RabbitMQ para o microsserviço consumidor.
 */
@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "produtoQueue";

    /**
     * Declaração da fila para consumo de mensagens.
     */
    @Bean
    public org.springframework.amqp.core.Queue produtoQueue() {
        return new org.springframework.amqp.core.Queue(QUEUE_NAME, true); // Persistente
    }

    /**
     * Configura o conversor de mensagens para JSON.
     */
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Configura o RabbitTemplate para usar o conversor JSON.
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }
}
