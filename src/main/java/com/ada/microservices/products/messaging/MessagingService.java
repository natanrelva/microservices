package com.ada.microservices.products.messaging;

/**
 * Interface para serviços de mensagens.
 */
public interface MessagingService {
    
    /**
     * Envia uma mensagem para um exchange específico com uma routing key.
     *
     * @param exchange   Nome do exchange.
     * @param routingKey Chave de roteamento.
     * @param message    Mensagem a ser enviada.
     */
    void sendMessage(String exchange, String routingKey, String message);
}
