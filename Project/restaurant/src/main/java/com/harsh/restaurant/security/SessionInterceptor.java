package com.harsh.restaurant.security;

import com.harsh.restaurant.entity.Role;
import com.harsh.restaurant.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SessionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (uri.startsWith("/admin")) {
            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/login?error=Please+login+to+access+admin+dashboard");
                return false;
            }
            if (user.getRole() != Role.ADMIN) {
                response.sendRedirect(request.getContextPath() + "/customer/dashboard?error=Access+Denied");
                return false;
            }
        }

        if (uri.startsWith("/customer")) {
            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/login?error=Please+login+first");
                return false;
            }
        }

        return true;
    }
}
