package com.ada.microservices.products.mapper;

import com.ada.microservices.products.dto.ProdutoRequestDTO;
import com.ada.microservices.products.dto.ProdutoResponseDTO;
import com.ada.microservices.products.model.Produto;
import org.springframework.stereotype.Component;

/**
 * Implementação concreta da interface ProdutoMapper.
 */
@Component
public class ProdutoMapperImpl implements ProdutoMapper {

    /**
     * Mapeia um ProdutoRequestDTO para uma entidade Produto.
     *
     * @param dto DTO de requisição.
     * @return Entidade Produto.
     */
    @Override
    public Produto toEntity(ProdutoRequestDTO dto) {
        return Produto.builder()
                .nome(dto.getNome())
                .categoria(dto.getCategoria())
                .preco(dto.getPreco())
                .quantidade(dto.getQuantidade())
                .build();
    }

    /**
     * Mapeia uma entidade Produto para um ProdutoResponseDTO.
     *
     * @param produto Entidade Produto.
     * @return DTO de resposta.
     */
    @Override
    public ProdutoResponseDTO toResponseDTO(Produto produto) {
        return new ProdutoResponseDTO(
                produto.getId(),
                produto.getNome(),
                produto.getCategoria(),
                produto.getPreco(),
                produto.getQuantidade(),
                produto.getDescricao()
        );
    }
}
