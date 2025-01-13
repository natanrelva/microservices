package com.ada.microservices.products.service;

import com.ada.microservices.products.dto.ProdutoRequestDTO;
import com.ada.microservices.products.dto.ProdutoResponseDTO;
import com.ada.microservices.products.model.Produto;
import com.ada.microservices.products.repository.ProdutoRepository;
import com.ada.microservices.products.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoService produtoService;

    private Produto produto;
    private ProdutoRequestDTO produtoRequestDTO;

    @BeforeEach
    void setUp() {
        produto = Produto.builder()
                .id(1L)
                .nome("Produto Teste")
                .categoria("Categoria Teste")
                .preco(100.0)
                .quantidade(10)
                .build();

        produtoRequestDTO = new ProdutoRequestDTO("Produto Teste", "Categoria Teste", 100.0, 10);
        produtoRequestDTO.setNome("Produto Teste");
        produtoRequestDTO.setCategoria("Categoria Teste");
        produtoRequestDTO.setPreco(100.0);
        produtoRequestDTO.setQuantidade(10);
    }

    @Test
    void testCriarProduto() {
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

        ProdutoResponseDTO responseDTO = produtoService.criarProduto(produtoRequestDTO);

        assertNotNull(responseDTO);
        assertEquals(produto.getId(), responseDTO.getId());
        assertEquals(produto.getNome(), responseDTO.getNome());
        assertEquals(produto.getCategoria(), responseDTO.getCategoria());
        assertEquals(produto.getPreco(), responseDTO.getPreco());
        assertEquals(produto.getQuantidade(), responseDTO.getQuantidade());
    }

    @Test
    void testListarProdutos() {
        when(produtoRepository.findAll()).thenReturn(Arrays.asList(produto));

        List<ProdutoResponseDTO> produtos = produtoService.listarTodosProdutos();

        assertNotNull(produtos);
        assertEquals(1, produtos.size());
        assertEquals(produto.getId(), produtos.get(0).getId());
    }

    @Test
    void testBuscarProdutoPorId() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        ProdutoResponseDTO responseDTO = produtoService.buscarProdutoPorId(1L);

        assertNotNull(responseDTO);
        assertEquals(produto.getId(), responseDTO.getId());
    }

    @Test
    void testBuscarProdutoPorIdNotFound() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> produtoService.buscarProdutoPorId(1L));
    }

    @Test
    void testAtualizarProduto() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

        ProdutoResponseDTO responseDTO = produtoService.atualizarProduto(1L, produtoRequestDTO);

        assertNotNull(responseDTO);
        assertEquals(produto.getId(), responseDTO.getId());
        assertEquals(produto.getNome(), responseDTO.getNome());
    }

    @Test
    void testAtualizarProdutoNotFound() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> produtoService.atualizarProduto(1L, produtoRequestDTO));
    }

    @Test
    void testDeletarProduto() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        doNothing().when(produtoRepository).delete(produto);

        produtoService.deletarProduto(1L);

        verify(produtoRepository, times(1)).delete(produto);
    }

    @Test
    void testDeletarProdutoNotFound() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> produtoService.deletarProduto(1L));
    }
}