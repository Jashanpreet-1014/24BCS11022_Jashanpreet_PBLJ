package com.harsh.restaurant.config;

import com.harsh.restaurant.entity.Food;
import com.harsh.restaurant.entity.Role;
import com.harsh.restaurant.entity.User;
import com.harsh.restaurant.repository.FoodRepository;
import com.harsh.restaurant.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final FoodRepository foodRepository;

    @Autowired
    public DataInitializer(UserRepository userRepository, FoodRepository foodRepository) {
        this.userRepository = userRepository;
        this.foodRepository = foodRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Seed default Admin user
        if (!userRepository.existsByEmail("admin@restaurant.com")) {
            User admin = new User("System Admin", "admin@restaurant.com", "admin123", "9876543210", Role.ADMIN);
            userRepository.save(admin);
            System.out.println(">>> Default Admin created: admin@restaurant.com / admin123");
        }

        // Seed default Customer user
        if (!userRepository.existsByEmail("customer@restaurant.com")) {
            User customer = new User("John Customer", "customer@restaurant.com", "customer123", "9123456789", Role.CUSTOMER);
            userRepository.save(customer);
            System.out.println(">>> Default Customer created: customer@restaurant.com / customer123");
        }

        // Seed initial food items if table is empty
        if (foodRepository.count() == 0) {
            Food f1 = new Food(
                    "Paneer Butter Masala",
                    "Rich & creamy cottage cheese gravy cooked with butter, spices and cashew paste.",
                    "Main Course",
                    new BigDecimal("280.00"),
                    "https://images.unsplash.com/photo-1631452180519-c014fe946bc7?w=500&q=80",
                    true
            );
            Food f2 = new Food(
                    "Margherita Pizza",
                    "Classic 10-inch hand-tossed pizza with fresh tomato sauce, mozzarella cheese, and basil.",
                    "Pizza",
                    new BigDecimal("350.00"),
                    "https://images.unsplash.com/photo-1574071318508-1cdbab80d002?w=500&q=80",
                    true
            );
            Food f3 = new Food(
                    "Cheese Veggie Burger",
                    "Crispy vegetable patty topped with melted cheddar cheese, fresh lettuce, tomatoes, and mayo.",
                    "Burgers",
                    new BigDecimal("150.00"),
                    "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=500&q=80",
                    true
            );
            Food f4 = new Food(
                    "Penne Arrabbiata Pasta",
                    "Penne pasta tossed in spicy red chilli, garlic, and tomato marinara sauce.",
                    "Pasta",
                    new BigDecimal("240.00"),
                    "https://images.unsplash.com/photo-1621996346565-e3d5d6281282?w=500&q=80",
                    true
            );
            Food f5 = new Food(
                    "Cheese Garlic Bread",
                    "Oven-baked french bread topped with garlic butter and melted mozzarella.",
                    "Starters",
                    new BigDecimal("180.00"),
                    "https://images.unsplash.com/photo-1619860860774-1e2e17343432?w=500&q=80",
                    true
            );
            Food f6 = new Food(
                    "Iced Cold Coffee",
                    "Chilled brewed espresso blended with creamy milk and vanilla ice cream.",
                    "Beverages",
                    new BigDecimal("120.00"),
                    "https://images.unsplash.com/photo-1517701604599-bb29b565090c?w=500&q=80",
                    true
            );

            foodRepository.saveAll(Arrays.asList(f1, f2, f3, f4, f5, f6));
            System.out.println(">>> Sample Food items initialized successfully!");
        }
    }
}
