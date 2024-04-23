package com.example.gtshop.service.order;

import com.example.gtshop.dto.OrderDto;
import com.example.gtshop.model.Order;

import java.util.List;

public interface IOrderService {
    OrderDto placeOrder(Long userId);
    OrderDto getOrder(Long orderId);
    List<OrderDto> getUserOrders(Long userId);
}
