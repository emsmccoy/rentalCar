package com.example.rentalCar.repository;

import com.example.rentalCar.model.User;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Repository
public class UserRepository {
    private final DynamoDbTable<User> userTable;

    public UserRepository(DynamoDbEnhancedClient enhancedClient) {
        this.userTable = enhancedClient.table("users", TableSchema.fromBean(User.class));
    }

    // Create or Update a User
    public void save(User user) {
        userTable.putItem(user);
    }

    // Read a User by userId and operation
    public User findById(String userId, String operation) {
        Key key = Key.builder()
                .partitionValue(userId)
                .sortValue(operation)
                .build();
        return userTable.getItem(key);
    }

    // Delete a User by userId and operation
    public void deleteById(String userId, String operation) {
        Key key = Key.builder()
                .partitionValue(userId)
                .sortValue(operation)
                .build();
        userTable.deleteItem(key);
    }
}
