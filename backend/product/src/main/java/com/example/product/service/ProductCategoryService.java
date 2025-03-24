package com.example.product.service;

import com.example.product.model.ProductCategory;
import com.example.product.repository.ProductCategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class ProductCategoryService {

    private final ProductCategoryRepository repository;
    private final String uploadDir = "product/src/main/resources/static/uploads/";

    public ProductCategoryService(ProductCategoryRepository repository) {
        this.repository = repository;
    }

    public List<ProductCategory> getAll() {
        return  repository.findAll();
    }

    public Optional<ProductCategory> getCategoryById(Integer id) {
        return repository.findById(id);
    }


    public ProductCategory create(String categoryName, MultipartFile file) throws IOException {

        String filePath = saveImage(file);

        ProductCategory category = new ProductCategory();
        category.setCategoryName(categoryName);
        category.setIconImage(filePath);
        return repository.save(category);
    }

    public ProductCategory update(Integer id, String categoryName, MultipartFile file) throws IOException {
        Optional<ProductCategory> optionalCategory = repository.findById(id);

        if (optionalCategory.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category with id " + id + " not found");
        }
        ProductCategory category = optionalCategory.get();
        category.setCategoryName(categoryName);

        if (file != null && !file.isEmpty()) {
            String filePath = saveImage(file);
            category.setIconImage(filePath);
        }

        return repository.save(category);
    }

    public void delete(Integer id) {
        Optional<ProductCategory> optionalCategory = repository.findById(id);

        if (optionalCategory.isPresent()) {
            String iconImage = optionalCategory.get().getIconImage();
            if (iconImage != null) {
                Path filePath = Paths.get(iconImage);

                try {
                    Files.deleteIfExists(filePath);
                } catch (IOException ex) {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                            "Error deleting the file: " + filePath.toString());
                }
            }
            repository.delete(optionalCategory.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found with id " + id);
        }
    }


    private String saveImage(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir + fileName);

        try {
            Files.copy(file.getInputStream(), filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return filePath.toString();
    }
}
