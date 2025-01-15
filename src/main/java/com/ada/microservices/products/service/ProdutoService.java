package com.ada.microservices.products.service;

import com.ada.microservices.products.dto.ProdutoRequestDTO;
import com.ada.microservices.products.dto.ProdutoResponseDTO;
import com.ada.microservices.products.exception.ResourceNotFoundException;
import com.ada.microservices.products.mapper.ProdutoMapper;
import com.ada.microservices.products.messaging.MessagingService;
import com.ada.microservices.products.model.Produto;
import com.ada.microservices.products.repository.ProdutoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço para gerenciar produtos, responsável pelas operações de CRUD e comunicação com serviços de mensagens.
 * 
 * Cumpre os princípios SOLID:
 * - **SRP**: Apenas gerencia operações de produtos e delega outras responsabilidades.
 * - **DIP**: Depende de abstrações (ProdutoMapper e MessagingService) em vez de implementações concretas.
 */
@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final ProdutoMapper produtoMapper;
    private final MessagingService messagingService;
    private final ObjectMapper objectMapper;

    /**
     * Construtor para injeção de dependências.
     *
     * @param produtoRepository Repositório de produtos.
     * @param produtoMapper     Mapper para conversão entre entidades e DTOs.
     * @param messagingService  Serviço de mensagens para comunicação com RabbitMQ.
     * @param objectMapper      ObjectMapper para serialização JSON.
     */
    public ProdutoService(ProdutoRepository produtoRepository,
                          ProdutoMapper produtoMapper,
                          MessagingService messagingService,
                          ObjectMapper objectMapper) {
        this.produtoRepository = produtoRepository;
        this.produtoMapper = produtoMapper;
        this.messagingService = messagingService;
        this.objectMapper = objectMapper;
    }

    /**
     * Cria um novo produto e publica um evento na fila RabbitMQ.
     *
     * @param dto Dados do produto a ser criado.
     * @return DTO de resposta com os dados do produto criado.
     */
    public ProdutoResponseDTO criarProduto(ProdutoRequestDTO dto) {
        // Mapeia DTO para entidade
        Produto produto = produtoMapper.toEntity(dto);
        // Salva o produto e obtém o objeto salvo com ID
        Produto salvo = produtoRepository.save(produto);

        // Serializa o objeto salvo para JSON
        String message = serializeProduto(salvo);
        // Envia a mensagem para RabbitMQ
        messagingService.sendMessage("produtoExchange", "produto.created", message);

        // Mapeia entidade salva para DTO de resposta
        return produtoMapper.toResponseDTO(salvo);
    }

    /**
     * Busca um produto pelo ID.
     *
     * @param id ID do produto.
     * @return DTO de resposta com os dados do produto.
     * @throws ResourceNotFoundException Se o produto não for encontrado.
     */
    public ProdutoResponseDTO buscarProdutoPorId(Long id) {
        Produto produto = obterProdutoPorId(id);
        return produtoMapper.toResponseDTO(produto);
    }

    /**
     * Atualiza um produto existente.
     *
     * @param id  ID do produto a ser atualizado.
     * @param dto Dados atualizados do produto.
     * @return DTO de resposta com os dados do produto atualizado.
     * @throws ResourceNotFoundException Se o produto não for encontrado.
     */
    public ProdutoResponseDTO atualizarProduto(Long id, ProdutoRequestDTO dto) {
        Produto produto = obterProdutoPorId(id);

        // Atualiza os dados do produto
        produto.setNome(dto.getNome());
        produto.setCategoria(dto.getCategoria());
        produto.setPreco(dto.getPreco());
        produto.setQuantidade(dto.getQuantidade());

        // Salva as alterações
        Produto salvo = produtoRepository.save(produto);
        return produtoMapper.toResponseDTO(salvo);
    }

    /**
     * Deleta um produto pelo ID.
     *
     * @param id ID do produto.
     * @throws ResourceNotFoundException Se o produto não for encontrado.
     */
    public void deletarProduto(Long id) {
        Produto produto = obterProdutoPorId(id);
        produtoRepository.delete(produto);
    }

    /**
     * Lista todos os produtos cadastrados.
     *
     * @return Lista de DTOs de resposta com os dados dos produtos.
     */
    public List<ProdutoResponseDTO> listarTodosProdutos() {
        return produtoRepository.findAll()
                .stream()
                .map(produtoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // --- Métodos auxiliares ---

    /**
     * Obtém um produto pelo ID ou lança uma exceção.
     *
     * @param id ID do produto.
     * @return Produto encontrado.
     * @throws ResourceNotFoundException Se o produto não for encontrado.
     */
    private Produto obterProdutoPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + id));
    }

    /**
     * Serializa um objeto Produto para JSON.
     *
     * @param produto Produto a ser serializado.
     * @return String JSON representando o produto.
     * @throws RuntimeException Se ocorrer um erro durante a serialização.
     */
    private String serializeProduto(Produto produto) {
        try {
            return objectMapper.writeValueAsString(produto);
        } catch (JsonProcessingException e) {
            // Envolve a exceção em uma exceção personalizada para melhor controle
            throw new RuntimeException("Erro ao serializar produto para JSON", e);
        }
    }
}
