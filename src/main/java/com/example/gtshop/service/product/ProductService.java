package com.example.gtshop.service.product;

import com.example.gtshop.dto.ImageDto;
import com.example.gtshop.dto.ProductDto;
import com.example.gtshop.exceptions.AlreadyExistsException;
import com.example.gtshop.exceptions.ResourceNotFoundException;
import com.example.gtshop.model.Category;
import com.example.gtshop.model.Image;
import com.example.gtshop.model.Product;
import com.example.gtshop.repository.CategoryRepository;
import com.example.gtshop.repository.ImageRepository;
import com.example.gtshop.repository.ProductRepository;
import com.example.gtshop.request.product.AddProductRequest;
import com.example.gtshop.request.product.UpdateProductRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public Product addProduct(AddProductRequest request) {

        if(productExists(request.getName(), request.getBrand())){
            throw new AlreadyExistsException(request.getBrand() + " " + request.getName() + " already exists");
        }
        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName())).orElseGet(
                () -> {
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                }
        );
        request.setCategory(category);
        return productRepository.save(createProduct(request, category));
    }
    private boolean productExists(String name, String brand){
        return productRepository.existsByNameAndBrand(name, brand);
    }

    private Product createProduct(AddProductRequest request, Category category) {
        return new Product(
                request.getName(),
                request.getProductCode(),
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
    public Product updateProduct(UpdateProductRequest request, Long productId) {
        return productRepository.findByProductCode(request.getProductCode()).map(
                        existingProduct -> updateExistingProduct(existingProduct, request)
                ).map(productRepository::save)
                .orElseThrow(() -> new ResourceNotFoundException("Product Not found"));
    }

    private Product updateExistingProduct(Product existingProduct, UpdateProductRequest request) {
        existingProduct.setName(request.getName());
        existingProduct.setProductCode(request.getProductCode());
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
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
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

    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products) {
        return products.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public ProductDto convertToDto(Product product) {
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos = images.stream().map(image -> modelMapper.map(image, ImageDto.class)).toList();
        productDto.setImages(imageDtos);
        return productDto;
    }

}
