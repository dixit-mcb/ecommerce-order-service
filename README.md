# E-Commerce Order Service

## Overview

This Spring Boot project provides RESTful APIs for managing orders, including creation, updating, retrieval, and deletion, with search and sort capabilities by customer name and status.

### API Endpoints

#### 1. Create Order
- POST /api/v1/orders (Admin only)

#### 2. Get Orders
- GET /api/v1/orders (Public)
- QueryParam: customerName, sort, status

#### 3. Get Order by ID
- GET /api/v1/orders/{id} (Public)

#### 4. Update Order
- PUT /api/v1/orders/{id} (Admin only)

#### 5. Delete Order
- DELETE /api/v1/orders/{id} (Admin only)

#### 6. Get Order History
- GET /api/v1/orders/customer/{customerName} (Public)

