package com.ada.microservices.products.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    private String descricao;

    public String getDescricao() {
        return this.descricao;
    }

}
