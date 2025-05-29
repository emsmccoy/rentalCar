package com.example.rentalCar.repository;

import com.example.rentalCar.model.Delegation;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.List;

@Repository
public class DelegationRepository {
    private final DynamoDbTable<Delegation> delegationTable;

    public DelegationRepository(DynamoDbEnhancedClient enhancedClient) {
        this.delegationTable = enhancedClient.table("delegation", TableSchema.fromBean(Delegation.class));
    }

    // Create or Update a Delegation
    public void save(Delegation delegation) {
        delegationTable.putItem(delegation);
    }

    // Read a Delegation by delegationId and operation
    public Delegation findById(String delegationId, String operation) {
        Key key = Key.builder()
                .partitionValue(delegationId)
                .sortValue(operation)
                .build();
        return delegationTable.getItem(key);
    }

    // Delete a Delegation by delegationId and operation
    public void deleteById(String delegationId, String operation) {
        Key key = Key.builder()
                .partitionValue(delegationId)
                .sortValue(operation)
                .build();
        delegationTable.deleteItem(key);
    }
}
