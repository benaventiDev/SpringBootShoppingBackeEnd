package com.example.gtshop.dto;

import com.example.gtshop.model.Category;
import lombok.Data;


import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDto {
    private Long id;
    private String productCode;
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;
    private Category category;
    private List<ImageDto> images;
}
