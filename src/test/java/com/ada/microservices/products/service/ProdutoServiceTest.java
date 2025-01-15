package com.ada.microservices.products.service;

import com.ada.microservices.products.dto.ProdutoRequestDTO;
import com.ada.microservices.products.dto.ProdutoResponseDTO;
import com.ada.microservices.products.exception.ResourceNotFoundException;
import com.ada.microservices.products.mapper.ProdutoMapper;
import com.ada.microservices.products.messaging.MessagingService;
import com.ada.microservices.products.model.Produto;
import com.ada.microservices.products.repository.ProdutoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Classe de testes unitários para ProdutoService.
 */
public class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private ProdutoMapper produtoMapper;

    @Mock
    private MessagingService messagingService;

    @Mock
    private com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    @InjectMocks
    private ProdutoService produtoService;

    private Produto produto;
    private ProdutoRequestDTO produtoRequestDTO;
    private ProdutoResponseDTO produtoResponseDTO;

    private static final Long PRODUTO_ID = 1L;
    private static final String NOME_PRODUTO = "Produto Teste";
    private static final String CATEGORIA_PRODUTO = "Categoria Teste";
    private static final Double PRECO_PRODUTO = 100.0;
    private static final Integer QUANTIDADE_PRODUTO = 10;
    private static final String DESCRICAO_PRODUTO = "Descrição Teste";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Configuração do objeto Produto
        produto = Produto.builder()
                .id(PRODUTO_ID)
                .nome(NOME_PRODUTO)
                .categoria(CATEGORIA_PRODUTO)
                .preco(PRECO_PRODUTO)
                .quantidade(QUANTIDADE_PRODUTO)
                .descricao(DESCRICAO_PRODUTO)
                .build();

        // Configuração do DTO de requisição
        produtoRequestDTO = new ProdutoRequestDTO(NOME_PRODUTO, CATEGORIA_PRODUTO, PRECO_PRODUTO, QUANTIDADE_PRODUTO);

        // Configuração do DTO de resposta
        produtoResponseDTO = new ProdutoResponseDTO(PRODUTO_ID, NOME_PRODUTO, CATEGORIA_PRODUTO, PRECO_PRODUTO, QUANTIDADE_PRODUTO, DESCRICAO_PRODUTO);
    }

    @Nested
    @DisplayName("Testes para criar produto")
    class CriarProdutoTests {

        @Test
        @DisplayName("Deve criar um produto com sucesso e enviar mensagem para RabbitMQ")
        void testCriarProduto_Sucesso() throws Exception {
            // Arrange
            when(produtoMapper.toEntity(any(ProdutoRequestDTO.class))).thenReturn(produto);
            when(produtoRepository.save(any(Produto.class))).thenReturn(produto);
            when(produtoMapper.toResponseDTO(any(Produto.class))).thenReturn(produtoResponseDTO);
            when(objectMapper.writeValueAsString(any(Produto.class))).thenReturn("{\"id\":1,\"nome\":\"Produto Teste\",\"categoria\":\"Categoria Teste\",\"preco\":100.0,\"quantidade\":10,\"descricao\":\"Descrição Teste\"}");

            // Act
            ProdutoResponseDTO responseDTO = produtoService.criarProduto(produtoRequestDTO);

            // Assert
            assertNotNull(responseDTO, "O responseDTO não deve ser nulo");
            assertEquals(PRODUTO_ID, responseDTO.getId(), "O ID do produto deve ser igual");
            assertEquals(NOME_PRODUTO, responseDTO.getNome(), "O nome do produto deve ser igual");
            assertEquals(CATEGORIA_PRODUTO, responseDTO.getCategoria(), "A categoria do produto deve ser igual");
            assertEquals(PRECO_PRODUTO, responseDTO.getPreco(), "O preço do produto deve ser igual");
            assertEquals(QUANTIDADE_PRODUTO, responseDTO.getQuantidade(), "A quantidade do produto deve ser igual");
            assertEquals(DESCRICAO_PRODUTO, responseDTO.getDescricao(), "A descrição do produto deve ser igual");

            // Verifica se o mapper foi chamado corretamente
            verify(produtoMapper, times(1)).toEntity(produtoRequestDTO);
            verify(produtoMapper, times(1)).toResponseDTO(produto);

            // Verifica se o repositório foi chamado para salvar
            verify(produtoRepository, times(1)).save(produto);

            // Verifica se a mensagem foi serializada corretamente
            verify(objectMapper, times(1)).writeValueAsString(produto);

            // Verifica se o serviço de mensagens foi chamado corretamente
            verify(messagingService, times(1)).sendMessage("produtoExchange", "produto.created", "{\"id\":1,\"nome\":\"Produto Teste\",\"categoria\":\"Categoria Teste\",\"preco\":100.0,\"quantidade\":10,\"descricao\":\"Descrição Teste\"}");
        }

        @Test
        @DisplayName("Deve lançar RuntimeException quando ocorrer erro na serialização da mensagem")
        void testCriarProduto_SerializacaoFalha() throws Exception {
            // Arrange
            when(produtoMapper.toEntity(any(ProdutoRequestDTO.class))).thenReturn(produto);
            when(produtoRepository.save(any(Produto.class))).thenReturn(produto);
            when(objectMapper.writeValueAsString(any(Produto.class))).thenThrow(new JsonProcessingException("Erro de serialização") {});

            // Act & Assert
            RuntimeException exception = assertThrows(RuntimeException.class, () -> produtoService.criarProduto(produtoRequestDTO),
                    "Deve lançar RuntimeException quando ocorrer erro na serialização");

            assertEquals("Erro ao serializar produto para JSON", exception.getMessage());

            // Verifica se o serviço de mensagens não foi chamado devido à falha na serialização
            verify(messagingService, times(0)).sendMessage(anyString(), anyString(), anyString());
        }
    }

    @Nested
    @DisplayName("Testes para buscar produto por ID")
    class BuscarProdutoPorIdTests {

        @Test
        @DisplayName("Deve buscar um produto por ID com sucesso")
        void testBuscarProdutoPorId_Sucesso() {
            // Arrange
            when(produtoRepository.findById(PRODUTO_ID)).thenReturn(Optional.of(produto));
            when(produtoMapper.toResponseDTO(produto)).thenReturn(produtoResponseDTO);

            // Act
            ProdutoResponseDTO responseDTO = produtoService.buscarProdutoPorId(PRODUTO_ID);

            // Assert
            assertNotNull(responseDTO, "O responseDTO não deve ser nulo");
            assertEquals(PRODUTO_ID, responseDTO.getId(), "O ID do produto deve ser igual");
            assertEquals(NOME_PRODUTO, responseDTO.getNome(), "O nome do produto deve ser igual");
            assertEquals(CATEGORIA_PRODUTO, responseDTO.getCategoria(), "A categoria do produto deve ser igual");
            assertEquals(PRECO_PRODUTO, responseDTO.getPreco(), "O preço do produto deve ser igual");
            assertEquals(QUANTIDADE_PRODUTO, responseDTO.getQuantidade(), "A quantidade do produto deve ser igual");
            assertEquals(DESCRICAO_PRODUTO, responseDTO.getDescricao(), "A descrição do produto deve ser igual");

            // Verifica se o repositório foi chamado para buscar
            verify(produtoRepository, times(1)).findById(PRODUTO_ID);

            // Verifica se o mapper foi chamado corretamente
            verify(produtoMapper, times(1)).toResponseDTO(produto);
        }

        @Test
        @DisplayName("Deve lançar ResourceNotFoundException quando o produto não for encontrado")
        void testBuscarProdutoPorId_NotFound() {
            // Arrange
            when(produtoRepository.findById(PRODUTO_ID)).thenReturn(Optional.empty());

            // Act & Assert
            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                    () -> produtoService.buscarProdutoPorId(PRODUTO_ID),
                    "Deve lançar ResourceNotFoundException quando o produto não for encontrado");

            assertEquals("Produto não encontrado com ID: " + PRODUTO_ID, exception.getMessage());

            // Verifica se o mapper não foi chamado devido à exceção
            verify(produtoMapper, times(0)).toResponseDTO(any());
        }
    }

    // Outros testes para atualizar, deletar e listar produtos seriam refatorados de maneira similar
}
