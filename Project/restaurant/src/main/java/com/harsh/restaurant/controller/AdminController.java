package com.harsh.restaurant.controller;

import com.harsh.restaurant.entity.Food;
import com.harsh.restaurant.entity.Order;
import com.harsh.restaurant.entity.OrderStatus;
import com.harsh.restaurant.entity.User;
import com.harsh.restaurant.service.FoodService;
import com.harsh.restaurant.service.OrderService;
import com.harsh.restaurant.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final FoodService foodService;
    private final OrderService orderService;

    @Autowired
    public AdminController(UserService userService, FoodService foodService, OrderService orderService) {
        this.userService = userService;
        this.foodService = foodService;
        this.orderService = orderService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalUsers", userService.getTotalUsersCount());
        model.addAttribute("totalFoods", foodService.getTotalFoodsCount());
        model.addAttribute("totalOrders", orderService.getTotalOrdersCount());
        model.addAttribute("pendingOrders", orderService.getPendingOrdersCount());
        model.addAttribute("recentOrders", orderService.getAllOrders().stream().limit(5).toList());
        return "admin/dashboard";
    }

    @GetMapping("/food")
    public String listFood(Model model) {
        model.addAttribute("foods", foodService.getAllFoods());
        return "admin/food-list";
    }

    @GetMapping("/food/add")
    public String showAddFoodForm(Model model) {
        model.addAttribute("food", new Food());
        model.addAttribute("pageTitle", "Add New Food Item");
        return "admin/food-form";
    }

    @GetMapping("/food/edit/{id}")
    public String showEditFoodForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Food food = foodService.getFoodById(id);
        if (food == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Food item not found");
            return "redirect:/admin/food";
        }
        model.addAttribute("food", food);
        model.addAttribute("pageTitle", "Edit Food Item");
        return "admin/food-form";
    }

    @PostMapping("/food/save")
    public String saveFood(@Valid @ModelAttribute("food") Food food,
                           BindingResult bindingResult,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", food.getId() == null ? "Add New Food Item" : "Edit Food Item");
            return "admin/food-form";
        }

        if (food.getId() == null) {
            foodService.saveFood(food);
            redirectAttributes.addFlashAttribute("successMessage", "Food item added successfully!");
        } else {
            foodService.updateFood(food.getId(), food);
            redirectAttributes.addFlashAttribute("successMessage", "Food item updated successfully!");
        }
        return "redirect:/admin/food";
    }

    @GetMapping("/food/toggle/{id}")
    public String toggleFoodAvailability(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        foodService.toggleAvailability(id);
        redirectAttributes.addFlashAttribute("successMessage", "Food availability updated.");
        return "redirect:/admin/food";
    }

    @GetMapping("/food/delete/{id}")
    public String deleteFood(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            foodService.deleteFood(id);
            redirectAttributes.addFlashAttribute("successMessage", "Food item deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete food item as it is linked to existing orders.");
        }
        return "redirect:/admin/food";
    }

    @GetMapping("/orders")
    public String listOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        model.addAttribute("statuses", OrderStatus.values());
        return "admin/orders";
    }

    @GetMapping("/orders/{id}")
    public String viewOrderDetails(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Order order = orderService.getOrderById(id);
        if (order == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Order not found");
            return "redirect:/admin/orders";
        }
        model.addAttribute("order", order);
        model.addAttribute("statuses", OrderStatus.values());
        return "admin/order-details";
    }

    @PostMapping("/orders/{id}/status")
    public String updateOrderStatus(@PathVariable("id") Long id,
                                    @RequestParam("status") OrderStatus status,
                                    RedirectAttributes redirectAttributes) {
        orderService.updateOrderStatus(id, status);
        redirectAttributes.addFlashAttribute("successMessage", "Order #" + id + " status updated to " + status);
        return "redirect:/admin/orders/" + id;
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/users";
    }
}
