package com.ada.microservices.products.service;

import com.ada.microservices.products.dto.ProdutoRequestDTO;
import com.ada.microservices.products.dto.ProdutoResponseDTO;
import com.ada.microservices.products.exception.ResourceNotFoundException;
import com.ada.microservices.products.model.Produto;
import com.ada.microservices.products.repository.ProdutoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ProdutoService produtoService;

    private Produto produto;
    private ProdutoRequestDTO produtoRequestDTO;

    private static final Long PRODUTO_ID = 1L;
    private static final String NOME_PRODUTO = "Produto Teste";
    private static final String CATEGORIA_PRODUTO = "Categoria Teste";
    private static final Double PRECO_PRODUTO = 100.0;
    private static final Integer QUANTIDADE_PRODUTO = 10;
    private static final String DESCRICAO_PRODUTO = "Descrição Teste";
    private static final String EXCHANGE = "produtoExchange";
    private static final String ROUTING_KEY = "produto.created";

    @BeforeEach
    void setUp() {
        produto = Produto.builder()
                .id(PRODUTO_ID)
                .nome(NOME_PRODUTO)
                .categoria(CATEGORIA_PRODUTO)
                .preco(PRECO_PRODUTO)
                .quantidade(QUANTIDADE_PRODUTO)
                .descricao(DESCRICAO_PRODUTO)
                .build();

        produtoRequestDTO = new ProdutoRequestDTO(NOME_PRODUTO, CATEGORIA_PRODUTO, PRECO_PRODUTO, QUANTIDADE_PRODUTO);
    }

    @Nested
    @DisplayName("Testes para criar produto")
    class CriarProdutoTests {

        @Test
        @DisplayName("Deve criar um produto com sucesso e enviar mensagem para RabbitMQ")
        void testCriarProduto_Sucesso() throws Exception {
            // Arrange
            when(produtoRepository.save(any(Produto.class))).thenReturn(produto);
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

            // Verifica se o RabbitTemplate foi chamado corretamente
            verify(rabbitTemplate, times(1)).convertAndSend(eq(EXCHANGE), eq(ROUTING_KEY), anyString());

            // Opcional: Verificar o conteúdo da mensagem enviada
            ArgumentCaptor<String> mensagemCaptor = ArgumentCaptor.forClass(String.class);
            verify(rabbitTemplate).convertAndSend(eq(EXCHANGE), eq(ROUTING_KEY), mensagemCaptor.capture());
            String mensagemEnviada = mensagemCaptor.getValue();
            assertNotNull(mensagemEnviada, "A mensagem enviada não deve ser nula");
            assertFalse(mensagemEnviada.isEmpty(), "A mensagem enviada não deve estar vazia");
            // Aqui você pode adicionar mais asserções sobre o conteúdo da mensagem
            assertEquals("{\"id\":1,\"nome\":\"Produto Teste\",\"categoria\":\"Categoria Teste\",\"preco\":100.0,\"quantidade\":10,\"descricao\":\"Descrição Teste\"}", mensagemEnviada, "A mensagem enviada deve ser igual ao esperado");
        }
    }

    @Nested
    @DisplayName("Testes para listar produtos")
    class ListarProdutosTests {

        @Test
        @DisplayName("Deve listar todos os produtos com sucesso")
        void testListarProdutos_Sucesso() {
            // Arrange
            when(produtoRepository.findAll()).thenReturn(Collections.singletonList(produto));

            // Act
            List<ProdutoResponseDTO> produtos = produtoService.listarTodosProdutos();

            // Assert
            assertNotNull(produtos, "A lista de produtos não deve ser nula");
            assertEquals(1, produtos.size(), "Deve retornar exatamente um produto");
            ProdutoResponseDTO produtoResponse = produtos.get(0);
            assertEquals(PRODUTO_ID, produtoResponse.getId(), "O ID do produto deve ser igual");
            assertEquals(NOME_PRODUTO, produtoResponse.getNome(), "O nome do produto deve ser igual");
            assertEquals(CATEGORIA_PRODUTO, produtoResponse.getCategoria(), "A categoria do produto deve ser igual");
            assertEquals(PRECO_PRODUTO, produtoResponse.getPreco(), "O preço do produto deve ser igual");
            assertEquals(QUANTIDADE_PRODUTO, produtoResponse.getQuantidade(), "A quantidade do produto deve ser igual");
            assertEquals(DESCRICAO_PRODUTO, produtoResponse.getDescricao(), "A descrição do produto deve ser igual");
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não houver produtos")
        void testListarProdutos_Vazio() {
            // Arrange
            when(produtoRepository.findAll()).thenReturn(Collections.emptyList());

            // Act
            List<ProdutoResponseDTO> produtos = produtoService.listarTodosProdutos();

            // Assert
            assertNotNull(produtos, "A lista de produtos não deve ser nula");
            assertTrue(produtos.isEmpty(), "A lista de produtos deve estar vazia");
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
        }

        @Test
        @DisplayName("Deve lançar ResourceNotFoundException quando o produto não for encontrado")
        void testBuscarProdutoPorId_NotFound() {
            // Arrange
            when(produtoRepository.findById(PRODUTO_ID)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(ResourceNotFoundException.class, () -> produtoService.buscarProdutoPorId(PRODUTO_ID),
                    "Deve lançar ResourceNotFoundException quando o produto não for encontrado");
        }
    }

    @Nested
    @DisplayName("Testes para atualizar produto")
    class AtualizarProdutoTests {

        @Test
        @DisplayName("Deve atualizar um produto com sucesso")
        void testAtualizarProduto_Sucesso() {
            // Arrange
            ProdutoRequestDTO atualizacaoDTO = new ProdutoRequestDTO("Produto Atualizado", "Categoria Atualizada", 150.0, 20);
            Produto produtoAtualizado = Produto.builder()
                    .id(PRODUTO_ID)
                    .nome(atualizacaoDTO.getNome())
                    .categoria(atualizacaoDTO.getCategoria())
                    .preco(atualizacaoDTO.getPreco())
                    .quantidade(atualizacaoDTO.getQuantidade())
                    .descricao(DESCRICAO_PRODUTO)
                    .build();

            when(produtoRepository.findById(PRODUTO_ID)).thenReturn(Optional.of(produto));
            when(produtoRepository.save(any(Produto.class))).thenReturn(produtoAtualizado);

            // Act
            ProdutoResponseDTO responseDTO = produtoService.atualizarProduto(PRODUTO_ID, atualizacaoDTO);

            // Assert
            assertNotNull(responseDTO, "O responseDTO não deve ser nulo");
            assertEquals(PRODUTO_ID, responseDTO.getId(), "O ID do produto deve ser igual");
            assertEquals(atualizacaoDTO.getNome(), responseDTO.getNome(), "O nome do produto deve ser atualizado");
            assertEquals(atualizacaoDTO.getCategoria(), responseDTO.getCategoria(), "A categoria do produto deve ser atualizada");
            assertEquals(atualizacaoDTO.getPreco(), responseDTO.getPreco(), "O preço do produto deve ser atualizado");
            assertEquals(atualizacaoDTO.getQuantidade(), responseDTO.getQuantidade(), "A quantidade do produto deve ser atualizada");
            assertEquals(DESCRICAO_PRODUTO, responseDTO.getDescricao(), "A descrição do produto deve permanecer igual");
        }

        @Test
        @DisplayName("Deve lançar ResourceNotFoundException ao tentar atualizar um produto inexistente")
        void testAtualizarProduto_NotFound() {
            // Arrange
            ProdutoRequestDTO atualizacaoDTO = new ProdutoRequestDTO("Produto Atualizado", "Categoria Atualizada", 150.0, 20);
            when(produtoRepository.findById(PRODUTO_ID)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(ResourceNotFoundException.class,
                    () -> produtoService.atualizarProduto(PRODUTO_ID, atualizacaoDTO),
                    "Deve lançar ResourceNotFoundException ao tentar atualizar um produto inexistente");
        }
    }

    @Nested
    @DisplayName("Testes para deletar produto")
    class DeletarProdutoTests {

        @Test
        @DisplayName("Deve deletar um produto com sucesso")
        void testDeletarProduto_Sucesso() {
            // Arrange
            when(produtoRepository.findById(PRODUTO_ID)).thenReturn(Optional.of(produto));
            doNothing().when(produtoRepository).delete(produto);

            // Act
            produtoService.deletarProduto(PRODUTO_ID);

            // Assert
            verify(produtoRepository, times(1)).delete(produto);
        }

        @Test
        @DisplayName("Deve lançar ResourceNotFoundException ao tentar deletar um produto inexistente")
        void testDeletarProduto_NotFound() {
            // Arrange
            when(produtoRepository.findById(PRODUTO_ID)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(ResourceNotFoundException.class,
                    () -> produtoService.deletarProduto(PRODUTO_ID),
                    "Deve lançar ResourceNotFoundException ao tentar deletar um produto inexistente");
        }
    }
}
