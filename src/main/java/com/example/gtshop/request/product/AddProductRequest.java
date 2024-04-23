package com.example.gtshop.request.product;

import com.example.gtshop.model.Category;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddProductRequest {
    private Long id;
    private String name;
    private String productCode;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;
    private Category Category;
}
