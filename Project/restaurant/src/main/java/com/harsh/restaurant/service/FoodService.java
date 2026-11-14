package com.harsh.restaurant.service;

import com.harsh.restaurant.entity.Food;
import java.util.List;

public interface FoodService {
    List<Food> getAllFoods();
    List<Food> getAvailableFoods();
    List<Food> searchFoods(String keyword);
    Food getFoodById(Long id);
    Food saveFood(Food food);
    Food updateFood(Long id, Food foodDetails);
    void deleteFood(Long id);
    void toggleAvailability(Long id);
    long getTotalFoodsCount();
}
