package com.example.rentalCar.service;

import com.example.rentalCar.model.User;

public interface UserService {
    void saveUser(User user);
    User getUserById(String userId, String operation);
    void deleteUser(String userId, String operation);
}

