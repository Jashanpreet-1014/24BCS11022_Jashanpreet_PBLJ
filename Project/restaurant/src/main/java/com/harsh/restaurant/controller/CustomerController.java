package com.harsh.restaurant.controller;

import com.harsh.restaurant.dto.Cart;
import com.harsh.restaurant.entity.Food;
import com.harsh.restaurant.entity.Order;
import com.harsh.restaurant.entity.User;
import com.harsh.restaurant.service.FoodService;
import com.harsh.restaurant.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    private final FoodService foodService;
    private final OrderService orderService;

    @Autowired
    public CustomerController(FoodService foodService, OrderService orderService) {
        this.foodService = foodService;
        this.orderService = orderService;
    }

    private Cart getCart(HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);

        List<Order> recentOrders = orderService.getOrdersByUser(user.getId());
        model.addAttribute("recentOrders", recentOrders.stream().limit(3).toList());
        model.addAttribute("totalOrders", recentOrders.size());

        return "customer/dashboard";
    }

    @GetMapping("/menu")
    public String menu(@RequestParam(value = "search", required = false) String search, Model model, HttpSession session) {
        List<Food> foods;
        if (search != null && !search.trim().isEmpty()) {
            foods = foodService.searchFoods(search);
            model.addAttribute("searchQuery", search);
        } else {
            foods = foodService.getAvailableFoods();
        }
        model.addAttribute("foods", foods);
        model.addAttribute("cart", getCart(session));
        return "customer/menu";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam("foodId") Long foodId,
                            @RequestParam(value = "quantity", defaultValue = "1") int quantity,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        Food food = foodService.getFoodById(foodId);
        if (food != null && food.isAvailable()) {
            Cart cart = getCart(session);
            cart.addItem(food, quantity);
            redirectAttributes.addFlashAttribute("successMessage", food.getName() + " added to cart!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Food item is not available.");
        }
        return "redirect:/customer/menu";
    }

    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model) {
        Cart cart = getCart(session);
        model.addAttribute("cart", cart);
        return "customer/cart";
    }

    @PostMapping("/cart/update")
    public String updateCartQuantity(@RequestParam("foodId") Long foodId,
                                     @RequestParam("quantity") int quantity,
                                     HttpSession session) {
        Cart cart = getCart(session);
        cart.updateQuantity(foodId, quantity);
        return "redirect:/customer/cart";
    }

    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam("foodId") Long foodId, HttpSession session) {
        Cart cart = getCart(session);
        cart.removeItem(foodId);
        return "redirect:/customer/cart";
    }

    @PostMapping("/checkout")
    public String checkout(HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        Cart cart = getCart(session);

        if (cart.getItems().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Your cart is empty!");
            return "redirect:/customer/cart";
        }

        Order order = orderService.createOrder(user, cart);
        cart.clear();

        redirectAttributes.addFlashAttribute("successMessage", "Order #" + order.getId() + " placed successfully!");
        return "redirect:/customer/orders/" + order.getId();
    }

    @GetMapping("/orders")
    public String viewOrders(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        List<Order> orders = orderService.getOrdersByUser(user.getId());
        model.addAttribute("orders", orders);
        return "customer/orders";
    }

    @GetMapping("/orders/{id}")
    public String orderDetails(@PathVariable("id") Long id, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        Order order = orderService.getOrderById(id);

        if (order == null || !order.getUser().getId().equals(user.getId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Order not found.");
            return "redirect:/customer/orders";
        }

        model.addAttribute("order", order);
        return "customer/order-details";
    }
}
