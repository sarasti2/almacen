package com.arquitectura.almacen.service;

import com.arquitectura.almacen.model.Product;
import com.arquitectura.almacen.model.Warehouse;
import com.arquitectura.almacen.repository.WarehouseRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WarehouseService {
    private final WarehouseRepository warehouseRepository;

    public WarehouseService(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    public Optional<Warehouse> getWarehouseById(Long id) {
        return warehouseRepository.findById(id);
    }

    public Warehouse saveWarehouse(Warehouse warehouse) {
        return warehouseRepository.save(warehouse);
    }
}