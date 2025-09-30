# Order Management System

A backend application with Spring Boot

## Project Structure

```
├── backend/          # Spring Boot REST API
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
├── frontend/         # Angular application
└── docker-compose.yaml
```

## Quick Start

### Development Mode

1. **Start Backend** (requires Java 21, MySQL, Redis):
```bash
cd backend
mvn spring-boot:run
```

Visit: http://localhost:4200

### Docker Mode

```bash
docker-compose up --build
```

### Env

```
# App Config
APP_PORT=8080

# Database Config
DATASOURCE_URLmysql
DATABASE_NAME=food
DATABASE_PORT=3306
DATABASE_USERNAME=admin
DATABASE_PASSWORD=123456789

# Redis Config
REDIS_HOSTredis
REDIS_PORT=6379
```

## API Endpoints

- `GET /api/orders` - Get all orders
- `GET /api/orders/{id}` - Get order by ID
- `POST /api/orders` - Create new order
- `PUT /api/orders/{id}/status` - Update order status

## Features

- Create order
- Order status updates
- Redis caching for improved performance
- Docker container
- MySQL database with Flyway migrations

# Test Execution Guide

## How to Run Service Tests

### Run Only OrderServiceTest
```bash
cd backend && mvn test -Dtest=OrderServiceTest
```

### Run All Tests
```bash
cd backend && mvn test
```

### Status ID Mapping (Inferred from Code)
- `1`: Pending
- `2`: Confirmed  
- `3`: In Progress
- `4`: Completed
- `5`: Cancelled

### 1. Updated Business Rules Implementation

The service now properly enforces:

✅ **Sequential Progression Only**:
- 1 → 2 (Pending → Confirmed)
- 2 → 3 (Confirmed → In Progress)  
- 3 → 4 (In Progress → Completed)

✅ **Cancellation Rules**:
- Any status (1-3) → 5 (Cancelled) **BEFORE** completion
- Status 4+ → 5 is **BLOCKED** (cannot cancel after completion)

❌ **Prevented Actions**:
- Backward progression (3→1, 4→2)
- Status skipping (1→3, 2→4)
- Same status updates (2→2, 3→3)
- Cancellation after completion (4→5, 5→5)

## Test Results

```
[INFO] Tests run: 11, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

All 11 tests now pass successfully, covering:
- ✅ Valid sequential progressions
- ✅ Cancellation before completion
- ❌ Backward progression prevention
- ❌ Status skipping prevention  
- ❌ Cancellation after completion prevention
- ❌ Same status update prevention
- ❌ Non-existent order handling