package com.ada.microservices.products.service;

import com.ada.microservices.products.dto.ProdutoRequestDTO;
import com.ada.microservices.products.dto.ProdutoResponseDTO;
import com.ada.microservices.products.model.Produto;
import com.ada.microservices.products.repository.ProdutoRepository;
import com.ada.microservices.products.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    @Autowired
    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public ProdutoResponseDTO criarProduto(ProdutoRequestDTO dto) {
        Produto produto = Produto.builder()
                .nome(dto.getNome())
                .categoria(dto.getCategoria())
                .preco(dto.getPreco())
                .quantidade(dto.getQuantidade())
                .build();
        produto = produtoRepository.save(produto);
        return entityToDTO(produto);
    }

    public ProdutoResponseDTO buscarProdutoPorId(Long id) {
        Produto produto = produtoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + id));
        return entityToDTO(produto);
    }

    public ProdutoResponseDTO atualizarProduto(Long id, ProdutoRequestDTO dto) {
        Produto produto = produtoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + id));
        produto.setNome(dto.getNome());
        produto.setCategoria(dto.getCategoria());
        produto.setPreco(dto.getPreco());
        produto.setQuantidade(dto.getQuantidade());
        produto = produtoRepository.save(produto);
        return entityToDTO(produto);
    }

    public void deletarProduto(Long id) {
        Produto produto = produtoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + id));
        produtoRepository.delete(produto);
    }


    private ProdutoResponseDTO entityToDTO(Produto produto) {
        return new ProdutoResponseDTO(
                produto.getId(),
                produto.getNome(),
                produto.getCategoria(),
                produto.getPreco(),
                produto.getQuantidade(),
                produto.getDescricao()
        );
    }

    public List<ProdutoResponseDTO> listarTodosProdutos() {
        return produtoRepository.findAll().stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());
    }
}
