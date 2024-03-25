package com.example.gtshop.service.product;

import com.example.gtshop.model.Product;
import com.example.gtshop.service.product.request.AddProductRequest;
import com.example.gtshop.service.product.request.ProductUpdateRequest;

import java.util.List;

public interface IProductService {
    Product addProduct(AddProductRequest request);
    Product getProductById(Long id);
    void deleteProductById(Long id);
    Product updateProduct(ProductUpdateRequest product, Long productId);
    List<Product> getAllProducts();
    List<Product> getProductsByCategoryId(String category);
    List<Product> getProductsByBrand(String brand);
    List<Product> getProductsbyBrandAndCategory(String brand, String category);
    List<Product> getProductsByName(String name);
    List<Product> getProductsbyBrandAndName(String brand, String name);
    Long countProductsByBrandAndName(String brand, String name);
    Long countProductsByCategory(String category);

}
