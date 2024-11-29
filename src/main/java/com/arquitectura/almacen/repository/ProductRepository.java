package com.arquitectura.almacen.repository;

import com.arquitectura.almacen.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
