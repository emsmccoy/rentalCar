# Renting Car Application Backend

## Overview

Spring Boot backend for a car rental system using AWS DynamoDB, following a layered architecture with repository and service layers. Features include CRUD operations for cars, users, bookings, and delegations with DynamoDB single-table design.

**Technologies**:
- Java 21
- Spring Boot 3.5.0
- AWS SDK for DynamoDB 2.31.46
- Docker (DynamoDB Local)
- Testcontainers 1.19.7

---

## PR Submission Checklist

### **Completed Tasks**:

- [x] Define data structure
- [x] DynamoDB Local setup with Docker
- [x] Repository layer implementation for Car/Booking/User/Delegation
- [x] Service layer with business logic
- [x] Integration tests using Testcontainers

### **Testing**:
- [x] Unit tests for service layer (Mockito)
- [x] Integration tests for repository layer

---

## Estimated Time for Tasks

### Core Implementation

| Task | Estimated Time | Actual Time | Impediments | New Concepts |
| --- | --- | --- | --- | --- |
| DynamoDB Local config | 2h | 3h | Port conflicts | Docker networking |
| Repository implementation | 4h | 6h | `TableSchema` errors | DynamoDB Enhanced Client |
| Service layer development | 3h | 4h | Business logic separation | Spring dependency injection |
| Testcontainers setup | 3h | 5h | Waiter timeouts | LocalStack integration |
| Error resolution | 4h | 6h | Missing constructors | AWS SDK annotations |
| **Total** | **16h** | **24h** |  |  |

---

## DynamoDB Data Structure

### 1. **`delegation` Table**
- **Partition Key**: `delegationId` (String)
- **Sort Key**: `operation` (String)
- **Entities Stored**:
    - **Delegations**: Represents rental locations/branches
        - `operation = "delegation#info"`
    - **Cars**: Represents vehicles available at a delegation
        - `operation = "car#{year}#{carId}"` (e.g., `car#2025#001`)

#### Example Item Structure:
```plaintext
+--------------+-------------------+----------+-------+-----+
| delegationId | operation         | name     | make  | ... |
+--------------+-------------------+----------+-------+-----+
| DELEG#001    | delegation#info   | Paris HQ | null  | ... |
| DELEG#001    | car#2025#001      | null     | Toyota| ... |
+--------------+-------------------+----------+-------+-----+
```

### 2. **`users` Table**
- **Partition Key**: `userId` (String)
- **Sort Key**: `operation` (String)
- **Entities Stored**:
    - **Users**: Customer/Admin profiles
        - `operation = "user#profile"`
    - **Bookings**: Rental reservations
        - `operation = "booking#{date}#{bookingId}"` (e.g., `booking#20250601#B001`)

#### Example Item Structure:
```plaintext
+----------+---------------------+----------+------------+-----+
| userId   | operation           | email    | startDate  | ... |
+----------+---------------------+----------+------------+-----+
| USER#001 | user#profile        | test@ex.co| null       | ... |
| USER#001 | booking#20250601#B1 | null     | 2025-06-01 | ... |
+----------+---------------------+----------+------------+-----+
```

---

## Key Design Principles

### 1. **Composite Primary Keys**
- Enables hierarchical data organization through key concatenation:
  ```java
  // Delegation entity
  delegationId = "DELEG#001"
  operation = "delegation#info"

  // Car entity under delegation
  delegationId = "DELEG#001" 
  operation = "car#2025#001"
  ```

### 2. **Single-Table Design Pattern**
- Stores multiple entity types in one table using differentiated sort keys
- Enables efficient queries like:
  ```java
  // Get all cars in delegation DELEG#001
  QueryConditional.query(b -> b
      .partitionValue("DELEG#001")
      .sortBeginsWith("car#"));
  ```

### 3. **Entity Annotations**
Example Car entity class:
```java
@DynamoDbBean
public class Car {
    private String delegationId;  // PK
    private String operation;     // SK
    private String make;
    private String model;
    private boolean rented;

    @DynamoDbPartitionKey
    public String getDelegationId() { return delegationId; }
    
    @DynamoDbSortKey
    public String getOperation() { return operation; }
    
    // Required no-args constructor
    public Car() {}
}
```

---

## Access Patterns Supported

| Pattern                          | Query Example                          |
|----------------------------------|----------------------------------------|
| Get delegation details           | `PK=DELEG#001, SK=delegation#info`     |
| List all cars in delegation      | `PK=DELEG#001, SK begins_with "car#"`  |
| Get user profile                 | `PK=USER#001, SK=user#profile`         |
| List user's bookings             | `PK=USER#001, SK begins_with "booking#"` |

---

This structure optimizes for:
1. **Cost Efficiency**: Reduces RCU/WCU consumption through smart key design
2. **Performance**: Enables fast lookups via exact key queries
3. **Scalability**: Handles high velocity data writes with hash key distribution
4. **Flexibility**: Accommodates new entity types through sort key prefixes

For future extensions, consider adding Global Secondary Indexes (GSIs) for common query patterns like searching cars by availability status or bookings by date range.

---

## Error Documentation and Solutions

### Error: `BeanCreationException: No default constructor`

**Corresponding Task:** Repository Implementation  
**Description:** AWS SDK couldn't instantiate DynamoDB entities  
**Error Trace:**
- **Component:** BookingRepository
- **File:** Booking.java
- **Line:** 39
- **Stack Trace:**

```
Caused by: java.lang.NoSuchMethodException: com.example.rentalCar.model.Booking.()
```

**Possible Causes:**
- Missing no-args constructor in `@DynamoDbBean` entities
- Lombok `@NoArgsConstructor` not properly configured

**Solution:**
```
@DynamoDbBean
public class Booking {
public Booking() {} // Added no-args constructor
// ... existing code ...
}
```

**Explanation:**  
DynamoDB Enhanced Client requires a public no-argument constructor for reflection-based instantiation.

---

### Error: `ResourceNotFoundException: Non-existent table`

**Corresponding Task:** Testcontainers Setup  
**Description:** Tables not created before test execution  
**Error Trace:**
- **Component:** CarRepositoryIT
- **File:** TestcontainersConfig.java
- **Line:** 95
- **Stack Trace:**
```
software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException
```

**Possible Causes:**
- Missing table creation logic
- Incorrect waiter configuration

**Solution:**
```
static void createTables() {
dynamoDbClient.createTable(CreateTableRequest.builder()
.tableName("delegation")
.keySchema(KeySchemaElement.builder()...)
.build());
}
```

**Explanation:**  
Tables must be explicitly created in DynamoDB Local before operations. Added manual table creation in Testcontainers config.

---

## Future Improvements

1. **REST API Controllers**
   - Implement endpoints for car booking and user management
2. **Authentication**
   - Add AWS Cognito security
3. **Advanced Queries**
   - Implement GSI for date-range bookings
4. **CI/CD Pipeline**
   - Automate testing with GitHub Actions
5. **Frontend Integration**
   - Develop React frontend with API consumption
