package com.example.product.controller;

import com.example.product.model.ProductCategory;
import com.example.product.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
public class ProductCategoryController {

    private final ProductCategoryService service;

    @Autowired
    public ProductCategoryController(ProductCategoryService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<ProductCategory> categories = service.getAll();

        if (categories.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Empty List!");
        }
        for (ProductCategory category : categories) {
            category.setIconImage("/uploads/" + Paths.get(category.getIconImage()).getFileName().toString());
        }
        return ResponseEntity.ok(categories);
    }



    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Integer id) {
        Optional<ProductCategory> optionalCategory = service.getCategoryById(id);
        if (optionalCategory.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id not found");
        }
        ProductCategory category = optionalCategory.get();
        return ResponseEntity.ok(category);
    }


    @PostMapping
    public ResponseEntity<ProductCategory> create(
            @RequestParam("categoryName") String categoryName,
            @RequestParam("iconImage")MultipartFile file) {
        try {
            ProductCategory category = service.create(categoryName, file);
            return ResponseEntity.status(HttpStatus.CREATED).body(category);
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Integer id,
            @RequestParam("categoryName") String categoryName,
            @RequestParam(value = "iconImage", required = false) MultipartFile file) {
        try {
            ProductCategory category = service.update(id, categoryName, file);
            return ResponseEntity.ok(category);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category with id: " + id + " not found");
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            service.delete(id);
            return ResponseEntity.ok("Id " + id + " deleted successfully");
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        }
    }
}
