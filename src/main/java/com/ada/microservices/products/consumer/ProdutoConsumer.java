package com.ada.microservices.products.consumer;

import com.ada.microservices.products.config.RabbitMQConfig;
import com.ada.microservices.products.dto.ProdutoResponseDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Consumidor de mensagens da fila RabbitMQ para produtos.
 */
@Component
public class ProdutoConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProdutoConsumer.class);

    /**
     * Consome mensagens da fila configurada no RabbitMQ.
     */
    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void consumeMessage(ProdutoResponseDTO produto) {
        LOGGER.info("Mensagem recebida da fila '{}': {}", RabbitMQConfig.QUEUE_NAME, produto);
    }
}
