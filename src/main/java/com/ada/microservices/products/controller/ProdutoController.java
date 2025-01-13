package com.ada.microservices.products.controller;

import com.ada.microservices.products.dto.ProdutoRequestDTO;
import com.ada.microservices.products.dto.ProdutoResponseDTO;
import com.ada.microservices.products.service.ProdutoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/produtos", produces = "application/json")
public class ProdutoController {

    private final ProdutoService produtoService;

    @Autowired
    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    /**
     * Creates a new product.
     *
     * @param produtoRequestDTO the product request data transfer object
     * @return the response entity containing the created product data transfer object
     */
    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> criarProduto(@RequestBody ProdutoRequestDTO produtoRequestDTO) {
        ProdutoResponseDTO responseDTO = produtoService.criarProduto(produtoRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    /**
     * Obtém um produto pelo seu ID.
     *
     * @param id o ID do produto a ser obtido
     * @return ResponseEntity contendo o ProdutoResponseDTO do produto obtido
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> obterProduto(@PathVariable Long id) {
        ProdutoResponseDTO responseDTO = produtoService.buscarProdutoPorId(id);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Lista todos os produtos.
     *
     * @return ResponseEntity contendo a lista de ProdutoResponseDTO
     */
    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> listarTodosProdutos() {
        List<ProdutoResponseDTO> produtos = produtoService.listarTodosProdutos();
        return ResponseEntity.ok(produtos);
    }

    /**
     * Atualiza um produto existente.
     *
     * @param id o ID do produto a ser atualizado
     * @param produtoRequestDTO os dados do produto a serem atualizados
     * @return ResponseEntity contendo o ProdutoResponseDTO do produto atualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> atualizarProduto(@PathVariable Long id, @RequestBody ProdutoRequestDTO produtoRequestDTO) {
        ProdutoResponseDTO responseDTO = produtoService.atualizarProduto(id, produtoRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Deletes a product by its ID.
     *
     * @param id the ID of the product to be deleted
     * @return ResponseEntity with no content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable Long id) {
        produtoService.deletarProduto(id);
        return ResponseEntity.noContent().build();
    }
}