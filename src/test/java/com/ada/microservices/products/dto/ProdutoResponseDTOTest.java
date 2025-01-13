package com.ada.microservices.products.dto;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProdutoResponseDTOTest {

    @Test
    public void testProdutoResponseDTOConstructorAndGetters() {
        Long id = 1L;
        String nome = "Produto A";
        String categoria = "Categoria A";
        Double preco = 100.0;
        Integer quantidade = 10;

        ProdutoResponseDTO produto = new ProdutoResponseDTO(id, nome, categoria, preco, quantidade, categoria);

        assertEquals(id, produto.getId());
        assertEquals(nome, produto.getNome());
        assertEquals(categoria, produto.getCategoria());
        assertEquals(preco, produto.getPreco());
        assertEquals(quantidade, produto.getQuantidade());
    }

    @Test
    public void testSetters() {
        ProdutoResponseDTO produto = new ProdutoResponseDTO(1L, "Produto A", "Categoria A", 100.0, 10, null);

        Long newId = 2L;
        String newNome = "Produto B";
        String newCategoria = "Categoria B";
        Double newPreco = 200.0;
        Integer newQuantidade = 20;

        produto.setId(newId);
        produto.setNome(newNome);
        produto.setCategoria(newCategoria);
        produto.setPreco(newPreco);
        produto.setQuantidade(newQuantidade);

        assertEquals(newId, produto.getId());
        assertEquals(newNome, produto.getNome());
        assertEquals(newCategoria, produto.getCategoria());
        assertEquals(newPreco, produto.getPreco());
        assertEquals(newQuantidade, produto.getQuantidade());
        assertEquals(null, produto.getDescricao());

    }
}
