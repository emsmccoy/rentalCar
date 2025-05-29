package com.example.rentalCar.service;

import com.example.rentalCar.model.Car;
import com.example.rentalCar.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;

    public CarServiceImpl(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public void saveCar(Car car) {
        carRepository.save(car);
    }

    @Override
    public Car getCarById(String delegationId, String operation) {
        return carRepository.findById(delegationId, operation);
    }

    @Override
    public List<Car> getCarsByDelegation(String delegationId) {
        return carRepository.findByDelegationId(delegationId);
    }

    @Override
    public List<Car> getAvailableCarsByDelegation(String delegationId) {
        List<Car> allCars = carRepository.findByDelegationId(delegationId);
        return allCars.stream()
                .filter(car -> !car.isRented() && car.getCarStatus() == com.example.rentalCar.model.CarStatus.AVAILABLE)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCar(String delegationId, String operation) {
        carRepository.deleteById(delegationId, operation);
    }
}

