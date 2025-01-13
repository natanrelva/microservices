package com.ada.microservices.products.controller;

import com.ada.microservices.products.dto.ProdutoRequestDTO;
import com.ada.microservices.products.dto.ProdutoResponseDTO;
import com.ada.microservices.products.service.ProdutoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ProdutoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProdutoService produtoService;

    @InjectMocks
    private ProdutoController produtoController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(produtoController).build();
    }

    @Test
    public void testCriarProduto() throws Exception {
        ProdutoResponseDTO responseDTO = new ProdutoResponseDTO(1L, "Produto Teste", "Categoria Teste", 10.0, 5, null);

        when(produtoService.criarProduto(any(ProdutoRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Produto Teste\",\"categoria\":\"Categoria Teste\",\"preco\":10.0,\"quantidade\":5}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Produto Teste"))
                .andExpect(jsonPath("$.categoria").value("Categoria Teste"))
                .andExpect(jsonPath("$.preco").value(10.0))
                .andExpect(jsonPath("$.quantidade").value(5))
                .andExpect(jsonPath("$.descricao").doesNotExist());

        verify(produtoService, times(1)).criarProduto(any(ProdutoRequestDTO.class));
    }

    @Test
    public void testObterProduto() {
        ProdutoResponseDTO responseDTO = new ProdutoResponseDTO(1L, "Produto 1", "Categoria 1", 100.0, 10, null);

        when(produtoService.buscarProdutoPorId(anyLong())).thenReturn(responseDTO);

        ResponseEntity<ProdutoResponseDTO> response = produtoController.obterProduto(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    public void testListarTodosProdutos() {
        List<ProdutoResponseDTO> responseDTOs = Arrays.asList(
                new ProdutoResponseDTO(1L, "Produto 1", "Categoria 1", 100.0, 10, "Descrição"),
                new ProdutoResponseDTO(2L, "Produto 2", "Categoria 2", 200.0, 20, "Descrição")
        );

        when(produtoService.listarTodosProdutos()).thenReturn(responseDTOs);

        ResponseEntity<List<ProdutoResponseDTO>> response = produtoController.listarTodosProdutos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTOs, response.getBody());
    }

    @Test
    public void testAtualizarProduto() {
        ProdutoRequestDTO requestDTO = new ProdutoRequestDTO(null, null, null, null);
        ProdutoResponseDTO responseDTO = new ProdutoResponseDTO(1L, "Produto Atualizado", "Categoria 1", 150.0, 5, null);

        when(produtoService.atualizarProduto(anyLong(), any(ProdutoRequestDTO.class))).thenReturn(responseDTO);

        ResponseEntity<ProdutoResponseDTO> response = produtoController.atualizarProduto(1L, requestDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    public void testDeletarProduto() {
        doNothing().when(produtoService).deletarProduto(anyLong());

        ResponseEntity<Void> response = produtoController.deletarProduto(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(produtoService, times(1)).deletarProduto(1L);
    }

    // Other test methods...
}