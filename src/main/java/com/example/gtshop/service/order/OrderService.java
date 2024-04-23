package com.example.gtshop.service.order;

import com.example.gtshop.dto.OrderDto;
import com.example.gtshop.enums.OrderStatus;
import com.example.gtshop.exceptions.EmptyCartException;
import com.example.gtshop.exceptions.ResourceNotFoundException;
import com.example.gtshop.model.Cart;
import com.example.gtshop.model.Order;
import com.example.gtshop.model.OrderItem;
import com.example.gtshop.model.Product;
import com.example.gtshop.repository.CartRepository;
import com.example.gtshop.repository.OrderRepository;
import com.example.gtshop.repository.ProductRepository;
import com.example.gtshop.repository.UserRepository;
import com.example.gtshop.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartService cartService;
    private final CartRepository cartRepository;
    private final ModelMapper modelMapper;


    @Override
    @Transactional
    public OrderDto placeOrder(Long userId) {

        Cart cart = cartService.getCartByUserId(userId);
        if(cart.getItems().isEmpty()) {
            throw new EmptyCartException("Cannot place an order with an empty cart.");
        }
        Order order = createOrder(cart);
        List<OrderItem> orderItemList = createOrderItems(order, cart);
        order.setOrderItems(new HashSet<>(orderItemList));
        order.setTotalAmount(cart.getTotalAmount());
        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(cart.getId());
        return convertToDto(savedOrder);
    }

    private Order createOrder(Cart cart) {
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());
        return order;

    }

    private List<OrderItem> createOrderItems(Order order, Cart cart){
        return cart.getItems().stream().map(cartItem -> {
            Product product = cartItem.getProduct();
            product.setInventory(product.getInventory() - cartItem.getQuantity());
            productRepository.save(product);
            return new OrderItem(order, product.getBrand(), product, cartItem.getQuantity(), cartItem.getUnitPrice());
        }).toList();
    }


    private BigDecimal calculateTotalAmount(List<OrderItem> orderItemList){
        return orderItemList
                .stream()
                .map(orderItem ->
                        orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public OrderDto getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .map(this :: convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    public List<OrderDto> getUserOrders(Long userId){
        List<Order> orders= orderRepository.findByUserId(userId);
        return orders.stream().map(this :: convertToDto).toList();

    }
    private OrderDto convertToDto(Order order) {
        return modelMapper.map(order, OrderDto.class);
    }
}
