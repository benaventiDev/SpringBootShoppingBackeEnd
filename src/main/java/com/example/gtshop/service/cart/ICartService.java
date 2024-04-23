package com.example.gtshop.service.cart;

import com.example.gtshop.dto.CartDto;
import com.example.gtshop.model.Cart;
import com.example.gtshop.model.User;

import java.math.BigDecimal;

public interface ICartService {

    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);

    Cart initializeNewCart(User user);
    Cart getCartByUserId(Long userId);
    CartDto convertToCartDto(Cart cart);
}
