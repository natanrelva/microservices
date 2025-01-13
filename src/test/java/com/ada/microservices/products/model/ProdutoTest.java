package com.ada.microservices.products.model;

import static org.junit.jupiter.api.Assertions.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Set;

public class ProdutoTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testProdutoValid() {
        Produto produto = Produto.builder()
                .nome("Produto Teste")
                .categoria("Categoria Teste")
                .preco(10.0)
                .quantidade(5)
                .build();

        Set<ConstraintViolation<Produto>> violations = validator.validate(produto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testNomeNotBlank() {
        Produto produto = Produto.builder()
                .nome("")
                .categoria("Categoria Teste")
                .preco(10.0)
                .quantidade(5)
                .build();

        Set<ConstraintViolation<Produto>> violations = validator.validate(produto);
        assertFalse(violations.isEmpty());
        assertEquals("O nome do produto não pode estar vazio", violations.iterator().next().getMessage());
    }

    @Test
    public void testCategoriaNotBlank() {
        Produto produto = Produto.builder()
                .nome("Produto Teste")
                .categoria("")
                .preco(10.0)
                .quantidade(5)
                .build();

        Set<ConstraintViolation<Produto>> violations = validator.validate(produto);
        assertFalse(violations.isEmpty());
        assertEquals("A categoria do produto não pode estar vazia", violations.iterator().next().getMessage());
    }

    @Test
    public void testPrecoNotNull() {
        Produto produto = Produto.builder()
                .nome("Produto Teste")
                .categoria("Categoria Teste")
                .preco(null)
                .quantidade(5)
                .build();

        Set<ConstraintViolation<Produto>> violations = validator.validate(produto);
        assertFalse(violations.isEmpty());
        assertEquals("O preço do produto é obrigatório", violations.iterator().next().getMessage());
    }

    @Test
    public void testPrecoMinValue() {
        Produto produto = Produto.builder()
                .nome("Produto Teste")
                .categoria("Categoria Teste")
                .preco(-1.0)
                .quantidade(5)
                .build();

        Set<ConstraintViolation<Produto>> violations = validator.validate(produto);
        assertFalse(violations.isEmpty());
        assertEquals("O preço do produto deve ser maior ou igual a zero", violations.iterator().next().getMessage());
    }

    @Test
    public void testQuantidadeNotNull() {
        Produto produto = Produto.builder()
                .nome("Produto Teste")
                .categoria("Categoria Teste")
                .preco(10.0)
                .quantidade(null)
                .build();

        Set<ConstraintViolation<Produto>> violations = validator.validate(produto);
        assertFalse(violations.isEmpty());
        assertEquals("A quantidade do produto é obrigatória", violations.iterator().next().getMessage());
    }

    @Test
    public void testQuantidadeMinValue() {
        Produto produto = Produto.builder()
                .nome("Produto Teste")
                .categoria("Categoria Teste")
                .preco(10.0)
                .quantidade(-1)
                .build();

        Set<ConstraintViolation<Produto>> violations = validator.validate(produto);
        assertFalse(violations.isEmpty());
        assertEquals("A quantidade do produto deve ser maior ou igual a zero", violations.iterator().next().getMessage());
    }
}