package com.example.product.service;

import com.example.product.model.Product;
import com.example.product.model.ProductCategory;
import com.example.product.model.dto.ProductDTO;
import com.example.product.repository.ProductCategoryRepository;
import com.example.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository repository;

    private final ProductCategoryRepository categoryRepository;

    @Autowired
    public ProductService(ProductRepository repository, ProductCategoryRepository categoryRepository) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
    }


    public List<Product> getAll() {
        return repository.findAll();
    }

    public Optional<ProductDTO> findById(Integer id) {
        return repository.findById(id)
                .map(product -> new ProductDTO(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getStock(),
                        product.getCategory().getId(),
                        product.getCreated()
                ));
    }

    public List<Product> findByCategoryId(Integer categoryId) {
        return repository.findByCategoryId(categoryId);
    }


    public ProductDTO create(ProductDTO productDTO) {
        Optional<ProductCategory>  optionalProduct = categoryRepository.findById(productDTO.categoryId());
        if (optionalProduct.isEmpty()) {
            throw new EntityNotFoundException("Category not found with id " + productDTO.categoryId());
        }

        ProductCategory category = optionalProduct.get();

        Product product = new Product();
        product.setName(productDTO.name());
        product.setDescription(productDTO.description());
        product.setPrice(productDTO.price());
        product.setStock(productDTO.stock());
        product.setCategory(category);
        product.setCreated(productDTO.created());

        Product createdProduct = repository.save(product);

        return new ProductDTO(
                createdProduct.getId(),
                createdProduct.getName(),
                createdProduct.getDescription(),
                createdProduct.getPrice(),
                createdProduct.getStock(),
                createdProduct.getCategory().getId(),
                createdProduct.getCreated()
        );
    }

    public ProductDTO update(ProductDTO product) {
        Optional<Product> existingProduct = repository.findById(product.id());
        if (existingProduct.isEmpty()) {
            throw new EntityNotFoundException("Product with id: " + product.id() + " not found");
        }
            Product productToUpdate = existingProduct.get();

            if (!categoryRepository.existsById(product.categoryId())) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category Id does not exist");
            }

            productToUpdate.setName(product.name());
            productToUpdate.setDescription(product.description());
            productToUpdate.setPrice(product.price());
            productToUpdate.setStock(product.stock());

            ProductCategory category = categoryRepository.findById(product.categoryId()).get();
            productToUpdate.setCategory(category);

            Product updatedProduct = repository.save(productToUpdate);

            return new ProductDTO(
                    updatedProduct.getId(),
                    updatedProduct.getName(),
                    updatedProduct.getDescription(),
                    updatedProduct.getPrice(),
                    updatedProduct.getStock(),
                    updatedProduct.getCategory().getId(),
                    updatedProduct.getCreated()
            );
    }

    public Product updateStock(Integer id, int newStock) {
        Optional<Product> optionalProduct = repository.findById(id);
        if (!optionalProduct.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found with id: " + id);
        }
        Product product = optionalProduct.get();
        product.setStock(newStock);
        return repository.save(product);
    }

    public void delete(Integer id) {
        Optional<Product> product = repository.findById(id);
        if (product.isPresent()) {
            repository.delete(product.get());
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found with id " + id);
        }
    }
}
