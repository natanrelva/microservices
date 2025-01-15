package com.ada.consumer.products.consumer;

import com.ada.consumer.products.config.RabbitMQConfig;
import com.ada.consumer.products.dto.ProdutoDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Consumidor dedicado para mensagens relacionadas a produtos no RabbitMQ.
 */
@Component
public class ProdutoConsumer {

    private final Logger logger;

    // Construtor padrão para produção
    public ProdutoConsumer() {
        this(LoggerFactory.getLogger(ProdutoConsumer.class));
    }

    // Construtor para injeção de Logger (facilita testes)
    public ProdutoConsumer(Logger logger) {
        this.logger = logger;
    }

    /**
     * Consome mensagens da fila configurada no RabbitMQ.
     *
     * @param produto Dados do produto consumidos da fila.
     */
    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void consumeMessage(ProdutoDTO produto) {
        logger.info("Mensagem recebida da fila '{}': {}", RabbitMQConfig.QUEUE_NAME, produto);

        try {
            processProduto(produto);
            logger.info("Mensagem processada com sucesso: {}", produto);
        } catch (IllegalArgumentException ex) {
            logger.error("Erro ao processar a mensagem: {}", produto, ex);
        }
    }

    /**
     * Executa a lógica de negócios para o produto consumido.
     *
     * @param produto Dados do produto.
     */
    private void processProduto(ProdutoDTO produto) {
        if (produto == null || produto.getNome() == null) {
            throw new IllegalArgumentException("Dados do produto inválidos: " + produto);
        }
        logger.info("Processando produto: ID={}, Nome={}, Categoria={}",
                produto.getId(), produto.getNome(), produto.getCategoria());
    }
}
