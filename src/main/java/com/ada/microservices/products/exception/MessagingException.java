package com.ada.microservices.products.exception;

/**
 * Exceção personalizada para erros no serviço de mensagens.
 */
public class MessagingException extends RuntimeException {
    public MessagingException(String message, Throwable cause) {
        super(message, cause);
    }
}
