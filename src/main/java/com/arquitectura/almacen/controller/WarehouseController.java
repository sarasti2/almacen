package com.arquitectura.almacen.controller;

import com.arquitectura.almacen.model.Product;
import com.arquitectura.almacen.model.Warehouse;
import com.arquitectura.almacen.repository.WarehouseRepository;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v1/warehouses")
public class WarehouseController {

    private final WarehouseRepository warehouseRepository;

    public WarehouseController(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    // Obtener productos de un almacén
    @GetMapping("/{id}/products")
    public ResponseEntity<?> getProductsByWarehouse(@PathVariable Long id) {
        Optional<Warehouse> warehouseOpt = warehouseRepository.findById(id);
        if (warehouseOpt.isPresent()) {
            Warehouse warehouse = warehouseOpt.get();
            List<EntityModel<Product>> products = warehouse.getProducts().stream()
                    .map(product -> EntityModel.of(product,
                            linkTo(methodOn(WarehouseController.class).getWarehouseById(id)).withRel("warehouse")))
                    .toList();
            return ResponseEntity.ok(CollectionModel.of(products));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Crear un nuevo almacén
    @PostMapping
    public ResponseEntity<EntityModel<Warehouse>> createWarehouse(@RequestBody Warehouse warehouse) {
        if (warehouse.getProducts() != null) {
            for (Product product : warehouse.getProducts()) {
                product.setWarehouse(warehouse);
            }
        }

        Warehouse savedWarehouse = warehouseRepository.save(warehouse);

        EntityModel<Warehouse> model = EntityModel.of(savedWarehouse);
        model.add(linkTo(methodOn(WarehouseController.class).getWarehouseById(savedWarehouse.getId())).withSelfRel());
        model.add(linkTo(methodOn(WarehouseController.class).getAllWarehouses()).withRel("all-warehouses"));

        return ResponseEntity
                .created(linkTo(methodOn(WarehouseController.class).getWarehouseById(savedWarehouse.getId())).toUri())
                .body(model);
    }

    // Obtener todos los almacenes
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Warehouse>>> getAllWarehouses() {
        List<Warehouse> warehouses = warehouseRepository.findAll();

        List<EntityModel<Warehouse>> warehouseModels = warehouses.stream()
                .map(warehouse -> EntityModel.of(warehouse,
                        linkTo(methodOn(WarehouseController.class).getWarehouseById(warehouse.getId())).withSelfRel()))
                .toList();

        return ResponseEntity.ok(CollectionModel.of(warehouseModels,
                linkTo(methodOn(WarehouseController.class).getAllWarehouses()).withSelfRel()));
    }

    // Obtener un almacén por ID
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Warehouse>> getWarehouseById(@PathVariable Long id) {
        Optional<Warehouse> warehouseOpt = warehouseRepository.findById(id);
        if (warehouseOpt.isPresent()) {
            Warehouse warehouse = warehouseOpt.get();

            EntityModel<Warehouse> model = EntityModel.of(warehouse);
            model.add(linkTo(methodOn(WarehouseController.class).getWarehouseById(id)).withSelfRel());
            model.add(linkTo(methodOn(WarehouseController.class).getAllWarehouses()).withRel("all-warehouses"));
            model.add(linkTo(methodOn(WarehouseController.class).getProductsByWarehouse(id)).withRel("products"));

            return ResponseEntity.ok(model);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
