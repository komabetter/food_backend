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

2. **Start Frontend** (requires Node.js):
```bash
cd frontend
npm install
ng serve
```

Visit: http://localhost:4200

### Docker Mode

```bash
docker-compose up --build
```

## API Endpoints

- `GET /api/orders` - Get all orders
- `GET /api/orders/{id}` - Get order by ID
- `POST /api/orders` - Create new order
- `PUT /api/orders/{id}/status` - Update order status

## Features

- Order listing with real-time status updates
- Redis caching for improved performance
- Responsive Angular UI
- Docker containerization
- MySQL database with Flyway migrations