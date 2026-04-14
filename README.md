# Lead Management API

A simple Lead Management REST API built with Java and Spring Boot, simulating a core CRM module. This service allows managing leads and converting them into customers.

## Tech Stack

- **Java 21**
- **Spring Boot 3.4.4**
- **Spring Data JPA / Hibernate**
- **PostgreSQL** (runtime) / **H2** (tests)
- **Lombok**
- **Jakarta Bean Validation**

## Prerequisites

- Java 21+
- PostgreSQL running on `localhost:5432`
- A database named `leadmanagement`

### Create the database

```sql
CREATE DATABASE leadmanagement;
```

## How to Run

```bash
# Clone the repository
git clone <repository-url>
cd leadmanagement

# Run the application
./mvnw spring-boot:run
```

The API will start on `http://localhost:8080`.

## How to Run Tests

```bash
./mvnw test
```

Tests use an in-memory H2 database and do not require PostgreSQL.

## API Endpoints

### Leads

| Method | Endpoint                  | Description              |
|--------|---------------------------|--------------------------|
| POST   | `/api/leads`              | Create a new lead        |
| GET    | `/api/leads`              | Retrieve all leads       |
| GET    | `/api/leads/{id}`         | Retrieve a single lead   |
| PUT    | `/api/leads/{id}`         | Update a lead            |
| POST   | `/api/leads/{id}/convert` | Convert lead to customer |

### Customers

| Method | Endpoint              | Description               |
|--------|-----------------------|---------------------------|
| GET    | `/api/customers`      | Retrieve all customers    |
| GET    | `/api/customers/{id}` | Retrieve a single customer|

### Example Requests

**Create a Lead:**
```bash
curl -X POST http://localhost:8080/api/leads \
  -H "Content-Type: application/json" \
  -d '{"name": "John Doe", "email": "john@example.com", "phone": "1234567890"}'
```

**Update a Lead:**
```bash
curl -X PUT http://localhost:8080/api/leads/{id} \
  -H "Content-Type: application/json" \
  -d '{"name": "John Doe", "email": "john@example.com", "phone": "0987654321", "status": "CONTACTED"}'
```

**Convert a Lead to Customer:**
```bash
curl -X POST http://localhost:8080/api/leads/{id}/convert
```

### Lead Statuses

- `NEW` - Freshly created lead (default)
- `CONTACTED` - Lead has been contacted
- `QUALIFIED` - Lead is qualified
- `CONVERTED` - Lead has been converted to a customer (set automatically via conversion endpoint)

## Project Architecture

The project follows **clean architecture** with a **package-by-layer** structure:

```
com.pandolar.leadmanagement/
├── config/                          # Spring @Configuration classes
│   └── JpaAuditingConfig.java
│
├── constants/                       # Application-wide constants
│   ├── ApiPaths.java                # Centralized API endpoint paths
│   └── ErrorMessages.java           # Centralized error message strings
│
├── controller/                      # REST controllers (thin — delegates to services)
│   ├── LeadController.java
│   └── CustomerController.java
│
├── dto/                             # Data Transfer Objects (pure data carriers)
│   ├── request/
│   │   ├── CreateLeadRequest.java
│   │   └── UpdateLeadRequest.java
│   └── response/
│       ├── LeadResponse.java
│       ├── CustomerResponse.java
│       └── ErrorResponse.java
│
├── entity/                          # JPA entities and domain enums
│   ├── base/
│   │   └── BaseEntity.java          # Auditing base (UUID, createdAt, updatedAt)
│   ├── enums/
│   │   └── LeadStatus.java
│   ├── Lead.java
│   └── Customer.java
│
├── exception/                       # Custom exceptions + global error handler
│   ├── GlobalExceptionHandler.java  # @RestControllerAdvice
│   ├── ResourceNotFoundException.java
│   └── DuplicateConversionException.java
│
├── mapper/                          # Entity ↔ DTO conversion logic
│   ├── LeadMapper.java
│   └── CustomerMapper.java
│
├── repository/                      # Spring Data JPA repository interfaces
│   ├── LeadRepository.java
│   └── CustomerRepository.java
│
├── service/                         # Service interfaces (contracts)
│   ├── LeadService.java
│   ├── CustomerService.java
│   └── impl/                        # Service implementations (@Service)
│       ├── LeadServiceImpl.java
│       └── CustomerServiceImpl.java
│
├── util/                            # Stateless utility classes
│   ├── StringUtils.java             # Input sanitization (trim, normalize)
│   └── ErrorResponseBuilder.java    # ErrorResponse factory
│
└── LeadmanagementApplication.java
```

### Layer Responsibilities

| Layer | Responsibility | Example |
|-------|---------------|---------|
| **controller/** | HTTP handling, validation, route mapping | `LeadController` |
| **service/** | Business logic interfaces | `LeadService` (interface) |
| **service/impl/** | Business logic implementations, transactions, logging | `LeadServiceImpl` |
| **repository/** | Data access abstraction via Spring Data JPA | `LeadRepository` |
| **entity/** | JPA entities, base classes, enums | `Lead`, `BaseEntity`, `LeadStatus` |
| **dto/** | Pure data carriers — request and response objects | `CreateLeadRequest`, `LeadResponse` |
| **mapper/** | Entity-to-DTO and DTO-to-Entity conversion | `LeadMapper`, `CustomerMapper` |
| **exception/** | Domain exceptions + global handler | `GlobalExceptionHandler` |
| **config/** | Spring configuration | `JpaAuditingConfig` |
| **constants/** | Application-wide constants | `ApiPaths`, `ErrorMessages` |
| **util/** | Reusable stateless helpers | `StringUtils`, `ErrorResponseBuilder` |

### Data Flow

```
HTTP Request
    ↓
Controller (@Valid → validates DTOs)
    ↓
Service Interface → ServiceImpl (@Transactional → business logic)
    ↓                    ↕
Repository          Mapper (DTO ↔ Entity)
    ↓
JPA / Hibernate → PostgreSQL
```

## Design Decisions & Assumptions

1. **Service interface + implementation separation** — `LeadService` (interface) and `LeadServiceImpl` (@Service). Enables clean mocking in tests, supports AOP proxying for transactions, and allows swappable implementations.
2. **UUID primary keys** — Non-sequential, non-guessable identifiers suitable for APIs.
3. **Email uniqueness** — Enforced at the database level to prevent duplicate leads.
4. **Lead-to-Customer conversion** — Creates a new Customer record with data copied from the Lead. The Lead record is preserved (not deleted) for historical tracking. A Lead can only be converted once — enforced both at the application level (status check) and database level (unique constraint on `lead_id`).
5. **Status management** — The `CONVERTED` status cannot be set directly via the update endpoint; it is only set through the conversion endpoint to maintain data integrity.
6. **Denormalized Customer data** — Customer stores its own name, email, and phone fields (copied from Lead at conversion time) so both entities are independently queryable.
7. **DTOs as pure Java records** — Immutable, concise request/response objects with zero logic. All mapping handled by dedicated `@Component` mapper classes.
8. **Constants package** — `ApiPaths` centralizes endpoint paths; `ErrorMessages` centralizes error strings. Avoids hardcoded strings scattered across the codebase.
9. **Global exception handler** — Consistent error response format across all endpoints using `@RestControllerAdvice` and `ErrorResponseBuilder`.
10. **JPA Auditing** — Automatic `createdAt` and `updatedAt` timestamps on all entities via `BaseEntity` and `@EnableJpaAuditing`.
