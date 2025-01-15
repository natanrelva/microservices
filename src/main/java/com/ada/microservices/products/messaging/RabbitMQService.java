package com.ada.microservices.products.messaging;

import com.ada.microservices.products.exception.MessagingException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

/**
 * Implementação concreta da interface MessagingService usando RabbitMQ.
 */
@Service
public class RabbitMQService implements MessagingService {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Envia uma mensagem para um exchange específico com uma routing key.
     *
     * @param exchange   Nome do exchange.
     * @param routingKey Chave de roteamento.
     * @param message    Mensagem a ser enviada.
     */
    @Override
    public void sendMessage(String exchange, String routingKey, String message) {
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, message);
        } catch (Exception e) {
            // Envolve a exceção em uma exceção personalizada para melhor controle
            throw new MessagingException("Erro ao enviar mensagem para RabbitMQ", e);
        }
    }
}
