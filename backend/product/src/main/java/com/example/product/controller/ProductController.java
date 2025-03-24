package com.example.product.controller;

import com.example.product.model.Product;
import com.example.product.model.dto.ProductDTO;
import com.example.product.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    @Autowired
    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<Product> products = service.getAll();
        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Empty list");
        }
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id) {
        Optional<ProductDTO> product = service.findById(id);
        if (product.isPresent()) {
            return ResponseEntity.ok(product.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found with id: " + id);
    }

    @GetMapping("/by-category")
    public ResponseEntity<List<Product>> getProductsByCategoryId(@RequestParam Integer categoryId) {
        List<Product> products = service.findByCategoryId(categoryId);
        if (products.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(products);
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid ProductDTO product) {
        try {
            ProductDTO createdProduct = service.create(product);
            return ResponseEntity.status(HttpStatus.CREATED).body("Product created successfully");
        }catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An Error Occurred: " + ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody @Valid ProductDTO product) {
        if (!id.equals(product.id())) {
            System.out.println(id);
            System.out.println(product.id());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID in the URL does not match the ID in the body");
        }

        Optional<ProductDTO> optional = service.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found!");
        }

        try {
            ProductDTO updateProduct = service.update(product);
            return ResponseEntity.ok(updateProduct);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

        @PutMapping("/{id}/update-stock")
        public ResponseEntity<?> updateStock(@PathVariable Integer id, @RequestParam int newStock) {
           try {
               Product updateProduct = service.updateStock(id, newStock);
               return ResponseEntity.ok("Stock updated successfully, New Stock: " + updateProduct.getStock());
           }catch (ResponseStatusException ex) {
               return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
           }
        }



    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        try {
            service.delete(id);
            return ResponseEntity.ok("Id " + id + " deleted successfully");
        }catch (ResponseStatusException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getReason());
        }
    }
}
