package com.harsh.restaurant.service;

import com.harsh.restaurant.dto.Cart;
import com.harsh.restaurant.dto.CartItem;
import com.harsh.restaurant.entity.Food;
import com.harsh.restaurant.entity.Order;
import com.harsh.restaurant.entity.OrderItem;
import com.harsh.restaurant.entity.OrderStatus;
import com.harsh.restaurant.entity.User;
import com.harsh.restaurant.repository.FoodRepository;
import com.harsh.restaurant.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final FoodRepository foodRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, FoodRepository foodRepository) {
        this.orderRepository = orderRepository;
        this.foodRepository = foodRepository;
    }

    @Override
    @Transactional
    public Order createOrder(User user, Cart cart) {
        if (cart == null || cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("Cannot place order with empty cart.");
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(cart.getTotalAmount());
        order.setStatus(OrderStatus.PENDING);

        for (CartItem cartItem : cart.getItems()) {
            Food food = foodRepository.findById(cartItem.getFoodId())
                    .orElseThrow(() -> new IllegalArgumentException("Food item not found: " + cartItem.getFoodName()));
            OrderItem orderItem = new OrderItem(order, food, cartItem.getQuantity(), cartItem.getPrice());
            order.addOrderItem(orderItem);
        }

        return orderRepository.save(order);
    }

    @Override
    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUserIdOrderByOrderDateDesc(userId);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAllByOrderByOrderDateDesc();
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = getOrderById(orderId);
        if (order != null) {
            order.setStatus(status);
            return orderRepository.save(order);
        }
        return null;
    }

    @Override
    public long getTotalOrdersCount() {
        return orderRepository.count();
    }

    @Override
    public long getPendingOrdersCount() {
        return orderRepository.countByStatus(OrderStatus.PENDING);
    }
}
