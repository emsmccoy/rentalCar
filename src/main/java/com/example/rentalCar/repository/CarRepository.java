package com.example.rentalCar.repository;

import com.example.rentalCar.model.Car;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CarRepository {
    private final DynamoDbTable<Car> carTable;

    public CarRepository(DynamoDbEnhancedClient enhancedClient) {
        this.carTable = enhancedClient.table("delegation", TableSchema.fromBean(Car.class));
    }

    // Create or Update a Car
    public void save(Car car) {
        carTable.putItem(car);
    }

    // Read a Car by delegationId and operation
    public Car findById(String delegationId, String operation) {
        Key key = Key.builder()
                .partitionValue(delegationId)
                .sortValue(operation)
                .build();
        return carTable.getItem(key);
    }

    // Read all Cars for a specific Delegation (basic query by partition key)
    public List<Car> findByDelegationId(String delegationId) {
        Key key = Key.builder().partitionValue(delegationId).build();
        QueryConditional queryConditional = QueryConditional.keyEqualTo(key);
        // Convert SdkIterable to List using Stream API
        return carTable.query(queryConditional)
                .items()
                .stream()
                .collect(Collectors.toList());
    }

    // Delete a Car by delegationId and operation
    public void deleteById(String delegationId, String operation) {
        Key key = Key.builder()
                .partitionValue(delegationId)
                .sortValue(operation)
                .build();
        carTable.deleteItem(key);
    }
}
