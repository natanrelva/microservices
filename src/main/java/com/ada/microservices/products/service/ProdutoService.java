package com.ada.microservices.products.service;

import com.ada.microservices.products.dto.ProdutoRequestDTO;
import com.ada.microservices.products.dto.ProdutoResponseDTO;
import com.ada.microservices.products.exception.ResourceNotFoundException;
import com.ada.microservices.products.model.Produto;
import com.ada.microservices.products.repository.ProdutoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço para gerenciar produtos.
 */
@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public ProdutoService(ProdutoRepository produtoRepository, RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.produtoRepository = produtoRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Cria um novo produto e publica um evento na fila RabbitMQ.
     *
     * @param dto Dados do produto a ser criado.
     * @return DTO de resposta com os dados do produto criado.
     */
    public ProdutoResponseDTO criarProduto(ProdutoRequestDTO dto) {
        Produto produto = mapRequestToEntity(dto);
        Produto salvo = produtoRepository.save(produto); // Captura o objeto salvo com o ID

        // Convert the saved product to a message and send it to RabbitMQ
        try {
            String message = objectMapper.writeValueAsString(salvo);
            rabbitTemplate.convertAndSend("produtoExchange", "produto.created", message);
        } catch (JsonProcessingException e) {
            // Lidar com a exceção apropriadamente
            throw new RuntimeException("Erro ao serializar mensagem para RabbitMQ", e);
        }

        return mapEntityToResponse(salvo); // Mapear o objeto salvo
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
        return mapEntityToResponse(produto);
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

        Produto salvo = produtoRepository.save(produto);
        return mapEntityToResponse(salvo);
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
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    // --- Métodos auxiliares ---

    /**
     * Mapeia uma entidade Produto para um DTO de resposta.
     *
     * @param produto Entidade Produto.
     * @return DTO de resposta.
     */
    private ProdutoResponseDTO mapEntityToResponse(Produto produto) {
        return new ProdutoResponseDTO(
                produto.getId(),
                produto.getNome(),
                produto.getCategoria(),
                produto.getPreco(),
                produto.getQuantidade(),
                produto.getDescricao()
        );
    }

    /**
     * Mapeia um DTO de requisição para uma entidade Produto.
     *
     * @param dto DTO de requisição.
     * @return Entidade Produto.
     */
    private Produto mapRequestToEntity(ProdutoRequestDTO dto) {
        return Produto.builder()
                .nome(dto.getNome())
                .categoria(dto.getCategoria())
                .preco(dto.getPreco())
                .quantidade(dto.getQuantidade())
                .build();
    }

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
}
