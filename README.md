# Employee Management Platform

A Spring Boot backend for employee, department, user, and PF account management, built to demonstrate secure REST APIs and asynchronous service-to-service communication.

> **What makes this project interesting:** employee onboarding is decoupled from notification processing. The Employee Service starts the notification workflow, the Notification Service processes it asynchronously, and a secured webhook reports completion back to the Employee Service.

[![Java](https://img.shields.io/badge/Java-17-orange)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.6-brightgreen)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-database-336791)](https://www.postgresql.org/)
[![Redis](https://img.shields.io/badge/Redis-cache-red)](https://redis.io/)
[![Gradle](https://img.shields.io/badge/Gradle-build-02303A)](https://gradle.org/)

## What it does

- Employee CRUD with pagination, sorting, search, and department filtering
- Department management
- Provident Fund account management
- JWT-based stateless authentication
- Role-based authorization (`ADMIN`, `USER`, `GUEST`)
- BCrypt password handling and password change flow
- Redis caching for employee reads
- PostgreSQL persistence through Spring Data JPA/Hibernate
- MapStruct DTO ↔ entity mapping
- Swagger/OpenAPI documentation
- Structured exception handling
- Asynchronous notification processing through a separate microservice

## Architecture

```mermaid
flowchart LR
    C[Client / Admin] -->|JWT| E[Employee Management\n:8081]
    E -->|notification.requested\nPOST + JSON| N[Email Notification\n:8083]
    N -->|202 Accepted| E
    N -->|@Async| P[Process Notification]
    P --> M[Mock Email Service]
    P -->|notification.completed\nPOST + Webhook Secret| E
```

### Services

**Employee Management** is the core service. It owns employees, users, departments, authentication, authorization, persistence, and the notification workflow.

**Email Notification Service** is a separate Spring Boot service that receives notification requests, processes them asynchronously, dispatches the email, and sends a completion webhook back to Employee Management.

Repository: [`Garenafanclub/email-notification-service`](https://github.com/Garenafanclub/email-notification-service)

## Asynchronous notification flow

When an admin creates an employee:

```text
1. Client
   |
   | POST /api/v1/employees
   v
2. Employee Service
   |-- save Employee
   |-- create User
   |-- generate temporary password
   |-- create notification.requested
   |
   | HTTP POST
   v
3. Notification Service
   |
   |--> 202 Accepted
   |
   |--> @Async processing
         |
         +--> send email
         |
         +--> notification.completed
                 |
                 | HTTP POST + X-Webhook-Secret
                 v
4. Employee Service
   |
   |--> validate webhook secret
   |--> receive completion event
   +--> return 200 OK
```

### Why `202 Accepted`?

`202 Accepted` means the Notification Service accepted the request for processing; it does **not** mean the email has already completed.

This lets Employee Management continue instead of blocking on a potentially slow notification operation.

### Why `@Async`?

The webhook request is handled on the HTTP thread, while notification work runs on an executor thread. In a debugger you can see the transition from a request thread such as `http-nio-8083-exec-1` to an executor thread such as `task-1`.

### Why a webhook?

The completion webhook removes the need for Employee Management to poll the Notification Service repeatedly asking whether the operation finished.

## Event contract

The services use explicit event models rather than passing arbitrary payloads.

### `notification.requested`

Contains:

- `eventId` — identifies the event message
- `operationId` — correlates the complete notification workflow
- `eventType`
- `occurredAt`
- `data.employeeId`
- `data.email`
- `data.departmentId`
- `data.temporaryPassword`

### `notification.completed`

Contains:

- `eventId`
- `operationId` — the same operation ID from the original request
- `eventType`
- `occurredAt`
- `data.employeeId`
- `data.status`

`operationId` is the key correlation value across both services.

## Security model

There are two different authentication boundaries in this system.

### User → Employee Service

Users authenticate with JWT:

```text
POST /api/v1/auth/login
        |
        v
      JWT
        |
        v
Authorization: Bearer <token>
```

### Notification Service → Employee Service

The completion webhook uses a dedicated service-to-service secret:

```http
X-Webhook-Secret: <shared-secret>
```

The webhook secret is separate from the JWT signing secret because it authenticates a **trusted service**, not a human user.

> The current implementation uses a shared secret. HMAC signing, replay protection, retries, and idempotency are planned reliability/security improvements.

## Tech stack

| Area | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 4.0.6 |
| Web | Spring MVC |
| Security | Spring Security + JWT |
| Database | PostgreSQL |
| ORM | Spring Data JPA / Hibernate |
| Cache | Redis / Spring Cache |
| Mapping | MapStruct 1.6.3 |
| Boilerplate | Lombok |
| API docs | SpringDoc OpenAPI / Swagger UI |
| Monitoring | Spring Boot Actuator |
| HTTP client | Spring `RestClient` |
| Async | Spring `@Async` |
| Build | Gradle |

## Getting started

### Prerequisites

- Java 17+
- PostgreSQL
- Redis on `localhost:6379`
- Git

### 1. Clone

```bash
git clone https://github.com/Garenafanclub/EmployeeManagement.git
cd EmployeeManagement
```

### 2. Create the database

```sql
CREATE DATABASE mayank;
```

### 3. Configure environment variables

Required:

```text
DB_PASSWORD=your_db_password
JWT_SECRET=your-secret-key-at-least-32-characters
```

Optional:

```text
DB_URL=jdbc:postgresql://localhost:5432/mayank
DB_USERNAME=postgres
JWT_EXPIRATION_MS=3600000
NOTIFICATION_WEBHOOK_URL=http://localhost:8083/api/v1/webhooks/notification-requested
WEBHOOK_SECRET=my-local-webhook-secret
```

Do **not** commit real secrets.

### 4. Seed an admin user

Generate a BCrypt hash with the included utility:

```bash
./gradlew runHashedPass --args="YourAdminPassword"
```

Then insert the user into PostgreSQL using the generated hash:

```sql
INSERT INTO users (email, password, provider)
VALUES ('admin@company.com', '<bcrypt_hash>', 'ADMIN');
```

### 5. Start Employee Management

```bash
./gradlew bootRun
```

API:

```text
http://localhost:8081/api/v1
```

Swagger UI:

```text
http://localhost:8081/swagger-ui.html
```

### 6. Start the Notification Service

```bash
git clone https://github.com/Garenafanclub/email-notification-service.git
cd email-notification-service
./gradlew bootRun
```

Notification Service runs on:

```text
http://localhost:8083
```

Set the same `WEBHOOK_SECRET` value in both services.

## API quick reference

### Authentication

| Method | Endpoint | Auth |
|---|---|---|
| `POST` | `/api/v1/auth/login` | Public |
| `POST` | `/api/v1/auth/change-password` | JWT |

### Employees

| Method | Endpoint | Auth |
|---|---|---|
| `GET` | `/api/v1/employees` | JWT |
| `GET` | `/api/v1/employees/{id}` | JWT |
| `GET` | `/api/v1/employees/find?email=` | JWT |
| `GET` | `/api/v1/employees/search?letter=` | JWT |
| `GET` | `/api/v1/employees/department/{id}` | JWT |
| `POST` | `/api/v1/employees` | ADMIN |
| `PATCH` | `/api/v1/employees/{id}` | JWT |
| `DELETE` | `/api/v1/employees/{id}` | ADMIN |

### Departments

| Method | Endpoint | Auth |
|---|---|---|
| `GET` | `/api/v1/departments` | JWT |
| `POST` | `/api/v1/departments` | ADMIN |

### PF accounts

| Method | Endpoint | Auth |
|---|---|---|
| `GET` | `/api/v1/pfaccounts` | ADMIN |
| `POST` | `/api/v1/pfaccounts` | ADMIN |

### Internal webhooks

```text
Employee Management
  POST /api/v1/webhooks/notification-requested

Notification Service
  POST /api/v1/webhooks/notification-completed
```

## Testing

Run the Employee Management test suite with:

```bash
./gradlew test
```

The repository includes controller, service, and repository-level tests.

For the distributed notification flow, verify:

```text
employee.created
      -> notification.requested
      -> 202 Accepted
      -> @Async processing
      -> email dispatch
      -> notification.completed
      -> secured webhook callback
      -> 200 OK
```

## Current limitations

This project currently demonstrates the communication pattern and security boundary, but it is not intended to claim production-grade reliability yet.

Planned improvements:

- Idempotent webhook processing
- Retry and backoff strategy
- Timeouts and failure handling
- HMAC webhook signatures + replay protection
- Transactional Outbox pattern
- Durable message broker integration
- Distributed tracing / OpenTelemetry
- Real email provider integration

## Repository ecosystem

This service is part of a small multi-repository backend system:

- **Employee Management:** [`Garenafanclub/EmployeeManagement`](https://github.com/Garenafanclub/EmployeeManagement)
- **Email Notification Service:** [`Garenafanclub/email-notification-service`](https://github.com/Garenafanclub/email-notification-service)

## Contributing

Contributions and improvements are welcome.

For changes:

1. Create a feature branch.
2. Keep the change focused.
3. Add or update tests where appropriate.
4. Update documentation when behavior changes.
5. Run `./gradlew test` before opening a pull request.

## Author

**Mayank**

Built as a hands-on backend engineering project exploring Spring Boot, Spring Security, JWT, PostgreSQL, Redis, asynchronous processing, webhooks, and service-to-service communication.

---

If you find the project useful, feel free to ⭐ the repository.
