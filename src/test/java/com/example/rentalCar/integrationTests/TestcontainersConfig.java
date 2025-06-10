package com.example.rentalCar.integrationTests;

import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.retry.backoff.FixedDelayBackoffStrategy;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;



import java.time.Duration;

public abstract class TestcontainersConfig {
    @Container
    static LocalStackContainer localStack = new LocalStackContainer(
            DockerImageName.parse("localstack/localstack:4.3.0")
    ).withServices(LocalStackContainer.Service.DYNAMODB);

    static DynamoDbClient dynamoDbClient;

    @BeforeAll
    static void setup() {
        localStack.start();
        dynamoDbClient = DynamoDbClient.builder()
                .endpointOverride(localStack.getEndpointOverride(LocalStackContainer.Service.DYNAMODB))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("dummy", "dummy")))
                .region(Region.of(localStack.getRegion()))
                .build();
        createTables();
        waitForTables();
    }

    static void createTables() {
        try {
            // Create 'delegation' table (for Car and Delegation entities)
            dynamoDbClient.createTable(CreateTableRequest.builder()
                    .tableName("delegation")
                    .keySchema(
                            KeySchemaElement.builder().attributeName("delegationId").keyType(KeyType.HASH).build(),
                            KeySchemaElement.builder().attributeName("operation").keyType(KeyType.RANGE).build()
                    )
                    .attributeDefinitions(
                            AttributeDefinition.builder().attributeName("delegationId").attributeType(ScalarAttributeType.S).build(),
                            AttributeDefinition.builder().attributeName("operation").attributeType(ScalarAttributeType.S).build()
                    )
                    .provisionedThroughput(ProvisionedThroughput.builder()
                            .readCapacityUnits(5L)
                            .writeCapacityUnits(5L)
                            .build())
                    .build());
        } catch (ResourceInUseException e) {
            System.out.println("Table 'delegation' already exists");
        }
    }

    static void waitForTables() {
        DynamoDbWaiter waiter = dynamoDbClient.waiter();
        waiter.waitUntilTableExists(
                b -> b.tableName("delegation"),  // Request builder
                o -> o.maxAttempts(10)           // Waiter config override
                        .backoffStrategy(FixedDelayBackoffStrategy.create(Duration.ofSeconds(1)))
        );
    }
}
