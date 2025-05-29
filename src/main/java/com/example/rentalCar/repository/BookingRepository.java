package com.example.rentalCar.repository;

import com.example.rentalCar.model.Booking;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class BookingRepository {
    private final DynamoDbTable<Booking> bookingTable;

    public BookingRepository(DynamoDbEnhancedClient enhancedClient) {
        this.bookingTable = enhancedClient.table("users", TableSchema.fromBean(Booking.class));
    }

    // Create or Update a Booking
    public void save(Booking booking) {
        bookingTable.putItem(booking);
    }

    // Read a Booking by userId and operation
    public Booking findById(String userId, String operation) {
        Key key = Key.builder()
                .partitionValue(userId)
                .sortValue(operation)
                .build();
        return bookingTable.getItem(key);
    }

    // Read all Bookings for a specific User (basic query by partition key)
    public List<Booking> findByUserId(String userId) {
        Key key = Key.builder().partitionValue(userId).build();
        QueryConditional queryConditional = QueryConditional.keyEqualTo(key);
        // Convert SdkIterable to List using Stream API
        return bookingTable.query(queryConditional)
                .items()
                .stream()
                .collect(Collectors.toList());
    }

    // Delete a Booking by userId and operation
    public void deleteById(String userId, String operation) {
        Key key = Key.builder()
                .partitionValue(userId)
                .sortValue(operation)
                .build();
        bookingTable.deleteItem(key);
    }
}
