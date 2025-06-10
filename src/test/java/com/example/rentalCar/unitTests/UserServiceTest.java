package com.example.rentalCar.unitTests;

import com.example.rentalCar.model.User;
import com.example.rentalCar.repository.UserRepository;
import com.example.rentalCar.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

    class UserServiceTest {

        @Mock
        private UserRepository userRepository;

        @InjectMocks
        private UserServiceImpl userService;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);
        }

        @Test
        void saveUser() {
            User user = new User();
            user.setUserId("user123");

            userService.saveUser(user);

            verify(userRepository, times(1)).save(user);
        }

        @Test
        void getUserById() {
            String userId = "user123";
            String operation = "user#profile";
            User expectedUser = new User();
            when(userRepository.findById(userId, operation)).thenReturn(expectedUser);

            User result = userService.getUserById(userId, operation);

            assertEquals(expectedUser, result);
            verify(userRepository, times(1)).findById(userId, operation);
        }

        @Test
        void deleteUser() {
            String userId = "user123";
            String operation = "user#profile";

            userService.deleteUser(userId, operation);

            verify(userRepository, times(1)).deleteById(userId, operation);
        }
    }
