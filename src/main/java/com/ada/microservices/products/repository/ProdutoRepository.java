package com.ada.microservices.products.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ada.microservices.products.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}

