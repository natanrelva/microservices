package com.ada.microservices.products.dto;

import org.junit.jupiter.api.Test;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;


public class ProdutoRequestDTOTest {

    private final Validator validator;

    public ProdutoRequestDTOTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidProdutoRequestDTO() {
        ProdutoRequestDTO produto = new ProdutoRequestDTO("Produto1", "Categoria1", 10.0, 5);
        Set<ConstraintViolation<ProdutoRequestDTO>> violations = validator.validate(produto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testNomeNotBlank() {
        ProdutoRequestDTO produto = new ProdutoRequestDTO("", "Categoria1", 10.0, 5);
        Set<ConstraintViolation<ProdutoRequestDTO>> violations = validator.validate(produto);
        assertFalse(violations.isEmpty());
        assertEquals("O nome do produto não pode estar vazio", violations.iterator().next().getMessage());
    }

    @Test
    public void testCategoriaNotBlank() {
        ProdutoRequestDTO produto = new ProdutoRequestDTO("Produto1", "", 10.0, 5);
        Set<ConstraintViolation<ProdutoRequestDTO>> violations = validator.validate(produto);
        assertFalse(violations.isEmpty());
        assertEquals("A categoria do produto não pode estar vazia", violations.iterator().next().getMessage());
    }

    @Test
    public void testPrecoNotNull() {
        ProdutoRequestDTO produto = new ProdutoRequestDTO("Produto1", "Categoria1", null, 5);
        Set<ConstraintViolation<ProdutoRequestDTO>> violations = validator.validate(produto);
        assertFalse(violations.isEmpty());
        assertEquals("O preço do produto é obrigatório", violations.iterator().next().getMessage());
    }

    @Test
    public void testPrecoMinValue() {
        ProdutoRequestDTO produto = new ProdutoRequestDTO("Produto1", "Categoria1", -1.0, 5);
        Set<ConstraintViolation<ProdutoRequestDTO>> violations = validator.validate(produto);
        assertFalse(violations.isEmpty());
        assertEquals("O preço do produto deve ser maior ou igual a zero", violations.iterator().next().getMessage());
    }

    @Test
    public void testQuantidadeNotNull() {
        ProdutoRequestDTO produto = new ProdutoRequestDTO("Produto1", "Categoria1", 10.0, null);
        Set<ConstraintViolation<ProdutoRequestDTO>> violations = validator.validate(produto);
        assertFalse(violations.isEmpty());
        assertEquals("A quantidade do produto é obrigatória", violations.iterator().next().getMessage());
    }

    @Test
    public void testQuantidadeMinValue() {
        ProdutoRequestDTO produto = new ProdutoRequestDTO("Produto1", "Categoria1", 10.0, -1);
        Set<ConstraintViolation<ProdutoRequestDTO>> violations = validator.validate(produto);
        assertFalse(violations.isEmpty());
        assertEquals("A quantidade do produto deve ser maior ou igual a zero", violations.iterator().next().getMessage());
    }
}