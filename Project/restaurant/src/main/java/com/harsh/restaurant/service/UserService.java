package com.harsh.restaurant.service;

import com.harsh.restaurant.entity.User;
import java.util.List;

public interface UserService {
    User registerUser(User user);
    User authenticate(String email, String password);
    boolean existsByEmail(String email);
    List<User> getAllUsers();
    User getUserById(Long id);
    long getTotalUsersCount();
}
