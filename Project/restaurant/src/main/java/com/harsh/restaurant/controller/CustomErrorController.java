package com.harsh.restaurant.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String errorMessage = "An unexpected error occurred.";

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            if (statusCode == 404) {
                errorMessage = "The page you are looking for does not exist (404).";
            } else if (statusCode == 403) {
                errorMessage = "You do not have permission to access this resource (403).";
            } else if (statusCode == 500) {
                errorMessage = "Internal server error occurred (500). Please try again later.";
            }
        }

        model.addAttribute("errorMessage", errorMessage);
        return "error";
    }
}
