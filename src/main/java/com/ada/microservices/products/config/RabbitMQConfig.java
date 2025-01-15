package com.ada.microservices.products.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração do RabbitMQ para gerenciamento de mensagens relacionadas a produtos.
 */
@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "produtoQueue";
    public static final String EXCHANGE_NAME = "produtoExchange";
    public static final String ROUTING_KEY = "produto.created";

    /**
     * Cria e configura a fila persistente para mensagens de produtos.
     */
    @Bean
    public Queue produtoQueue() {
        return new Queue(QUEUE_NAME, true); // Fila persistente
    }

    /**
     * Cria e configura a exchange do tipo 'topic' para mensagens de produtos.
     */
    @Bean
    public TopicExchange produtoExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    /**
     * Liga a fila à exchange com a routing key especificada.
     */
    @Bean
    public Binding produtoBinding(Queue produtoQueue, TopicExchange produtoExchange) {
        return BindingBuilder
                .bind(produtoQueue)
                .to(produtoExchange)
                .with(ROUTING_KEY);
    }
}
