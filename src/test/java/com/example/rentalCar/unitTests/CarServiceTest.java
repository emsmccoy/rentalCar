package com.example.rentalCar.unitTests;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.rentalCar.model.Car;
import com.example.rentalCar.model.CarStatus;
import com.example.rentalCar.repository.CarRepository;
import com.example.rentalCar.service.CarServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

public class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarServiceImpl carService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAvailableCarsByDelegation() {
        Car car1 = new Car();
        car1.setDelegationId("DELEG#001");
        car1.setOperation("car#2022#001");
        car1.setRented(false);
        car1.setCarStatus(CarStatus.AVAILABLE);

        Car car2 = new Car();
        car2.setDelegationId("DELEG#001");
        car2.setOperation("car#2022#002");
        car2.setRented(true);
        car2.setCarStatus(CarStatus.RENTED);

        when(carRepository.findByDelegationId("DELEG#001")).thenReturn(Arrays.asList(car1, car2));

        List<Car> availableCars = carService.getAvailableCarsByDelegation("DELEG#001");

        assertEquals(1, availableCars.size());
        assertEquals("car#2022#001", availableCars.get(0).getOperation());
    }

    @Test
    public void testSaveCar() {
        Car car = new Car();
        car.setDelegationId("DELEG#001");
        car.setOperation("car#2022#003");

        carService.saveCar(car);

        verify(carRepository, times(1)).save(car);
    }
}
