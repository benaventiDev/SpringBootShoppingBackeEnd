package com.example.gtshop.service.product;

import com.example.gtshop.exceptions.ResourceNotFoundException;
import com.example.gtshop.model.Category;
import com.example.gtshop.model.Product;
import com.example.gtshop.repository.CategoryRepository;
import com.example.gtshop.repository.ProductRepository;
import com.example.gtshop.service.product.request.AddProductRequest;
import com.example.gtshop.service.product.request.ProductUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Product addProduct(AddProductRequest request) {
        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName())).orElseGet(
                () -> {
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                }
        );
        request.setCategory(category);
        return productRepository.save(createProduct(request, category));
    }

    private Product createProduct(AddProductRequest request, Category category) {
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category
        );
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() ->  new ResourceNotFoundException("Product Not found"));
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id).
                ifPresentOrElse(productRepository::delete, () -> {throw new ResourceNotFoundException("Product Not found");});
    }

    @Override
    public Product updateProduct(ProductUpdateRequest request, Long productId) {
        return productRepository.findById(productId).map(existingProduct -> updateExistingProduct(existingProduct, request))
                .map(productRepository:: save)
                .orElseThrow(() -> new ResourceNotFoundException("Product Not found"));
    }

    private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request) {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());
        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;

    }


    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategoryId(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsbyBrandAndCategory(String brand, String category) {
        return productRepository.findByCategoryNameAndBrand(brand, category);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsbyBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }

    @Override
    public Long countProductsByCategory(String category) {
        return productRepository.countByCategoryName(category);
    }
}
