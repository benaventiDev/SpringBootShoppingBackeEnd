package com.example.gtshop.service.cart;

import com.example.gtshop.dto.CartItemDto;
import com.example.gtshop.model.CartItem;

public interface ICartItemService {
    void addItemToCart(Long cartId, Long productId, int quantity);
    void removeItemFromCart(Long cartId, Long productId);
    void updateItemQuantity(Long cartId, Long productId, int quantity);

    CartItem getCartItem(Long cartId, Long productId);
    CartItemDto convertToCartitemDto(CartItem cartItem);
}
