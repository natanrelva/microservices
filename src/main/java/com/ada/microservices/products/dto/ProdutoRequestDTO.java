package com.ada.microservices.products.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProdutoRequestDTO {

    @NotBlank(message = "O nome do produto não pode estar vazio")
    private String nome;

    @NotBlank(message = "A categoria do produto não pode estar vazia")
    private String categoria;

    @NotNull(message = "O preço do produto é obrigatório")
    @Min(value = 0, message = "O preço do produto deve ser maior ou igual a zero")
    private Double preco;

    @NotNull(message = "A quantidade do produto é obrigatória")
    @Min(value = 0, message = "A quantidade do produto deve ser maior ou igual a zero")
    private Integer quantidade;

    public ProdutoRequestDTO(String nome, String categoria, Double preco, Integer quantidade) {
        this.nome = nome;
        this.categoria = categoria;
        this.preco = preco;
        this.quantidade = quantidade;
    }

    // Getters and Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

}
