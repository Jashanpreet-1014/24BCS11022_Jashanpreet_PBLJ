package com.harsh.restaurant.service;

import com.harsh.restaurant.entity.Food;
import com.harsh.restaurant.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodServiceImpl implements FoodService {

    private final FoodRepository foodRepository;

    @Autowired
    public FoodServiceImpl(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    @Override
    public List<Food> getAllFoods() {
        return foodRepository.findAll();
    }

    @Override
    public List<Food> getAvailableFoods() {
        return foodRepository.findByAvailableTrue();
    }

    @Override
    public List<Food> searchFoods(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAvailableFoods();
        }
        return foodRepository.findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(keyword.trim(), keyword.trim());
    }

    @Override
    public Food getFoodById(Long id) {
        return foodRepository.findById(id).orElse(null);
    }

    @Override
    public Food saveFood(Food food) {
        return foodRepository.save(food);
    }

    @Override
    public Food updateFood(Long id, Food foodDetails) {
        Food existingFood = getFoodById(id);
        if (existingFood != null) {
            existingFood.setName(foodDetails.getName());
            existingFood.setDescription(foodDetails.getDescription());
            existingFood.setCategory(foodDetails.getCategory());
            existingFood.setPrice(foodDetails.getPrice());
            existingFood.setImageUrl(foodDetails.getImageUrl());
            existingFood.setAvailable(foodDetails.isAvailable());
            return foodRepository.save(existingFood);
        }
        return null;
    }

    @Override
    public void deleteFood(Long id) {
        foodRepository.deleteById(id);
    }

    @Override
    public void toggleAvailability(Long id) {
        Food food = getFoodById(id);
        if (food != null) {
            food.setAvailable(!food.isAvailable());
            foodRepository.save(food);
        }
    }

    @Override
    public long getTotalFoodsCount() {
        return foodRepository.count();
    }
}
