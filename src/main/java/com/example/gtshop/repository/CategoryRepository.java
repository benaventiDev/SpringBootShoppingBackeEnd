package com.example.gtshop.repository;

import com.example.gtshop.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Category findByName(String name);

    boolean existsByName(String name);

    Category findByName(String name);
}
