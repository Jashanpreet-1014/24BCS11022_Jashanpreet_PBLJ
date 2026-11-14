package com.harsh.restaurant.repository;

import com.harsh.restaurant.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
    List<Food> findByAvailableTrue();
    List<Food> findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(String name, String category);
}
