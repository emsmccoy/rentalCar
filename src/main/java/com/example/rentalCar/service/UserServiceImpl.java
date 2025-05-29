package com.example.rentalCar.service;

import com.example.rentalCar.model.User;
import com.example.rentalCar.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User getUserById(String userId, String operation) {
        return userRepository.findById(userId, operation);
    }

    @Override
    public void deleteUser(String userId, String operation) {
        userRepository.deleteById(userId, operation);
    }
}

