package com.harsh.restaurant.service;

import com.harsh.restaurant.dto.Cart;
import com.harsh.restaurant.entity.Order;
import com.harsh.restaurant.entity.OrderStatus;
import com.harsh.restaurant.entity.User;
import java.util.List;

public interface OrderService {
    Order createOrder(User user, Cart cart);
    List<Order> getOrdersByUser(Long userId);
    List<Order> getAllOrders();
    Order getOrderById(Long id);
    Order updateOrderStatus(Long orderId, OrderStatus status);
    long getTotalOrdersCount();
    long getPendingOrdersCount();
}
