package com.example.rentalCar.integrationTests;

import com.example.rentalCar.integrationTests.TestcontainersConfig;
import com.example.rentalCar.model.Car;
import com.example.rentalCar.repository.CarRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class CarRepositoryTest extends TestcontainersConfig {

    @Autowired
    private CarRepository carRepository;

    @BeforeAll
    static void waitForTables() {
        DynamoDbWaiter waiter = dynamoDbClient.waiter();
        waiter.waitUntilTableExists(b -> b.tableName("delegation"));
    }

    @Test
    void testSaveAndFindCar() {
        Car car = new Car();
        car.setDelegationId("DELEG#TEST");
        car.setOperation("car#2025#TEST001");
        car.setMake("Toyota");

        carRepository.save(car);

        Car retrieved = carRepository.findById("DELEG#TEST", "car#2025#TEST001");
        assertNotNull(retrieved);
    }
}
