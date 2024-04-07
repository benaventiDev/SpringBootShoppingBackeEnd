package com.example.gtshop.repository;

import com.example.gtshop.model.Cart;
import com.example.gtshop.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    void deleteAllByCartId(Long id);
}
