package com.ada.microservices.products.producer;

import com.ada.microservices.products.config.RabbitMQConfig;
import com.ada.microservices.products.dto.ProdutoResponseDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Produtor responsável por publicar mensagens na fila RabbitMQ para produtos.
 */
@Component
public class ProdutoProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProdutoProducer.class);

    private final RabbitTemplate rabbitTemplate;

    public ProdutoProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Publica uma mensagem na exchange configurada do RabbitMQ.
     */
    public void publish(ProdutoResponseDTO produtoResponseDTO) {
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE_NAME,
                    RabbitMQConfig.ROUTING_KEY,
                    produtoResponseDTO
            );
            LOGGER.info("Mensagem publicada na fila '{}': {}", RabbitMQConfig.QUEUE_NAME, produtoResponseDTO);
        } catch (Exception ex) {
            LOGGER.error("Erro ao publicar mensagem na fila '{}': {}", RabbitMQConfig.QUEUE_NAME, ex.getMessage(), ex);
            throw new RuntimeException("Falha ao publicar mensagem no RabbitMQ", ex);
        }
    }
}
