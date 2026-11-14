package com.harsh.restaurant.controller;

import com.harsh.restaurant.entity.Role;
import com.harsh.restaurant.entity.User;
import com.harsh.restaurant.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            if (user.getRole() == Role.ADMIN) {
                return "redirect:/admin/dashboard";
            } else {
                return "redirect:/customer/dashboard";
            }
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm(@RequestParam(value = "error", required = false) String error,
                                @RequestParam(value = "success", required = false) String success,
                                HttpSession session,
                                Model model) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            return user.getRole() == Role.ADMIN ? "redirect:/admin/dashboard" : "redirect:/customer/dashboard";
        }
        if (error != null) {
            model.addAttribute("errorMessage", error);
        }
        if (success != null) {
            model.addAttribute("successMessage", success);
        }
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam("email") String email,
                               @RequestParam("password") String password,
                               HttpSession session,
                               Model model) {
        User user = userService.authenticate(email, password);
        if (user == null) {
            model.addAttribute("errorMessage", "Invalid email or password");
            return "login";
        }

        session.setAttribute("user", user);

        if (user.getRole() == Role.ADMIN) {
            return "redirect:/admin/dashboard";
        } else {
            return "redirect:/customer/dashboard";
        }
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            return user.getRole() == Role.ADMIN ? "redirect:/admin/dashboard" : "redirect:/customer/dashboard";
        }
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@Valid @ModelAttribute("user") User user,
                                  BindingResult bindingResult,
                                  Model model) {
        if (userService.existsByEmail(user.getEmail())) {
            bindingResult.rejectValue("email", "error.user", "Email is already registered. Please login.");
        }

        if (bindingResult.hasErrors()) {
            return "register";
        }

        user.setRole(Role.CUSTOMER);
        userService.registerUser(user);
        return "redirect:/login?success=Registration+successful!+Please+login.";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?success=Logged+out+successfully.";
    }
}
