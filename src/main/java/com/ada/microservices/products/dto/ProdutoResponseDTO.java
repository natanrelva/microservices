package com.ada.microservices.products.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProdutoResponseDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("nome")
    private String nome;

    @JsonProperty("categoria")
    private String categoria;

    @JsonProperty("preco")
    private Double preco;

    @JsonProperty("quantidade")
    private Integer quantidade;

    @JsonProperty("descricao")
    private String descricao;

    @Override
        public String toString() {
            return "ProdutoResponseDTO{" +
                    "id=" + id +
                    ", nome='" + nome + '\'' +
                    ", categoria='" + categoria + '\'' +
                    ", preco=" + preco +
                    ", quantidade=" + quantidade +
                    ", descricao='" + descricao + '\'' +
                    '}';
        }

    public ProdutoResponseDTO(Long id, String nome, String categoria, Double preco, Integer quantidade, String descricao) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.preco = preco;
        this.quantidade = quantidade;
        this.descricao = descricao;
    }
    public Long getId(){
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getDescricao() {
        return descricao;
    }

}
