package com.ada.microservices.products.mapper;

import com.ada.microservices.products.dto.ProdutoRequestDTO;
import com.ada.microservices.products.dto.ProdutoResponseDTO;
import com.ada.microservices.products.model.Produto;

/**
 * Interface para mapeamento entre entidades Produto e DTOs.
 */
public interface ProdutoMapper {
    
    /**
     * Mapeia um ProdutoRequestDTO para uma entidade Produto.
     *
     * @param dto DTO de requisição.
     * @return Entidade Produto.
     */
    Produto toEntity(ProdutoRequestDTO dto);
    
    /**
     * Mapeia uma entidade Produto para um ProdutoResponseDTO.
     *
     * @param produto Entidade Produto.
     * @return DTO de resposta.
     */
    ProdutoResponseDTO toResponseDTO(Produto produto);
}
