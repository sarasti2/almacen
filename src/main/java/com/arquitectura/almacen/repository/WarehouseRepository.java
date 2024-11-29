package com.arquitectura.almacen.repository;

import com.arquitectura.almacen.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
}