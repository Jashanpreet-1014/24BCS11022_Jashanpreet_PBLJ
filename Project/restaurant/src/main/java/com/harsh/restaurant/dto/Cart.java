package com.harsh.restaurant.dto;

import com.harsh.restaurant.entity.Food;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<CartItem> items = new ArrayList<>();

    public Cart() {
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void addItem(Food food, int quantity) {
        for (CartItem item : items) {
            if (item.getFoodId().equals(food.getId())) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        items.add(new CartItem(food.getId(), food.getName(), food.getPrice(), quantity, food.getImageUrl()));
    }

    public void updateQuantity(Long foodId, int quantity) {
        if (quantity <= 0) {
            removeItem(foodId);
            return;
        }
        for (CartItem item : items) {
            if (item.getFoodId().equals(foodId)) {
                item.setQuantity(quantity);
                return;
            }
        }
    }

    public void removeItem(Long foodId) {
        items.removeIf(item -> item.getFoodId().equals(foodId));
    }

    public void clear() {
        items.clear();
    }

    public BigDecimal getTotalAmount() {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : items) {
            total = total.add(item.getSubtotal());
        }
        return total;
    }

    public int getTotalItems() {
        int count = 0;
        for (CartItem item : items) {
            count += item.getQuantity();
        }
        return count;
    }
}
