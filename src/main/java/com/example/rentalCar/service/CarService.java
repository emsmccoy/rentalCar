package com.example.rentalCar.service;

import com.example.rentalCar.model.Car;
import java.util.List;

public interface CarService {
    void saveCar(Car car);
    Car getCarById(String delegationId, String operation);
    List<Car> getCarsByDelegation(String delegationId);
    List<Car> getAvailableCarsByDelegation(String delegationId);
    void deleteCar(String delegationId, String operation);
}

