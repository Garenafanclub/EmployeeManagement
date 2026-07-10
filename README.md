# 🏢 Employee Management REST API

A production-grade **Employee Management System** built with Spring Boot 4, featuring JWT-based stateless authentication, role-based access control, Redis caching, and a clean layered architecture — with onboarding emails handed off to a separate Email Notification microservice.

---

## 🛠 Tech Stack

| Layer | Technology |
|---|---|
| Framework | Spring Boot 4.0.6 |
| Language | Java 17 |
| Security | Spring Security 6 + JWT (jjwt 0.12.x) |
| Database | PostgreSQL |
| ORM | Spring Data JPA / Hibernate |
| Caching | Redis (Spring Data Redis + Spring Cache) |
| Mapping | MapStruct 1.6.3 |
| Boilerplate | Lombok |
| API Docs | SpringDoc OpenAPI (Swagger UI) |
| Monitoring | Spring Boot Actuator |
| Build Tool | Gradle 9.4.1 |

---

## ✨ Features

- **JWT Authentication** — Stateless token-based auth with configurable expiry
- **Role-Based Access Control** — `ADMIN`, `USER`, and `GUEST` roles enforced via `@PreAuthorize`
- **Auto Temporary Password** — Cryptographically secure 10-char password generated and returned on employee creation
- **Password Management** — Authenticated users can change their own password
- **Employee CRUD** — Full create, read, update, delete with pagination and sorting
- **Smart Search** — Search employees by name prefix with pagination
- **Department Filtering** — Fetch all employees belonging to a specific department
- **PF Account Management** — Link Provident Fund accounts to employees
- **Redis Caching** — Employee list served from Redis after the first hit; cache auto-evicted on every write
- **Microservice Email Handoff** — New-hire welcome email delegated to a separate Email Notification microservice via `RestClient`
- **Structured Error Responses** — Consistent JSON error format across all endpoints
- **Custom Exception Hierarchy** — `ResourceNotFoundException`, `DuplicateResourceException`, `BadRequestException`
- **Swagger UI** — Interactive API docs auto-generated at `/swagger-ui.html`

---

## 📁 Project Structure

```
src/main/java/com/example/EmpManagement/
│
├── Config/
│   ├── SecurityConfig.java          # Filter chain, CORS, session policy
│   ├── JwtAuthEntryPoint.java       # 401 handler for missing/invalid tokens
│   ├── OpenApiConfig.java           # Swagger UI + global bearer-auth scheme
│   ├── AppConfig.java               # RestClient bean for service-to-service calls
│   └── HashedPass.java              # CLI utility to BCrypt a password
│
├── Controller/
│   ├── AuthController.java          # POST /auth/login, POST /auth/change-password
│   ├── EmpController.java           # CRUD + search + filter endpoints
│   ├── DepController.java           # Department endpoints
│   └── PfAccountController.java     # PF Account endpoints
│
├── Service/
│   ├── jwtSecurity/
│   │   ├── JWTService.java          # Token generation and validation
│   │   ├── JWTAuthenticationFilter.java  # Bearer token interceptor
│   │   └── CustomUserDetailsService.java # Loads User from DB for Spring Security
│   └── Imp/
│       ├── EmpServiceImp.java            # Employee logic + Redis caching
│       ├── DepServiceImp.java
│       ├── PFAccountServiceImp.java
│       └── OnboardingNotificationService.java  # Hands off welcome email to Email microservice
│
├── Model/
│   ├── User.java                    # Implements UserDetails, drives auth
│   ├── Employee.java
│   ├── Department.java
│   ├── PfAccount.java
│   └── Provider.java                # Enum: ADMIN | USER | GUEST
│
├── DTOs/                            # Request and Response DTOs
├── Mapper/                          # MapStruct mappers
├── Repository/                      # Spring Data JPA repositories
└── Exceptions/                      # Custom exceptions + GlobalExceptionHandler
```

---

## 🚀 Getting Started

### Prerequisites

- Java 17+
- PostgreSQL running locally
- Redis running locally on port `6379` (required for caching)
- Gradle (or use the included `./gradlew` wrapper)

### 1. Clone the repository

```bash
git clone https://github.com/Garenafanclub/EmployeeManagement.git
cd EmployeeManagement
```

### 2. Set up PostgreSQL

Create a database named `mayank` (or change the name in `application.yml`):

```sql
CREATE DATABASE mayank;
```

### 3. Configure environment variables

The app requires two mandatory environment variables at startup:

| Variable | Description | Required |
|---|---|---|
| `DB_PASSWORD` | Your PostgreSQL password | ✅ Yes |
| `JWT_SECRET` | Secret key for signing JWT tokens (min 32 chars) | ✅ Yes |
| `DB_URL` | JDBC URL | No (defaults to `localhost:5433/mayank`) |
| `DB_USERNAME` | DB username | No (defaults to `postgres`) |
| `JWT_EXPIRATION_MS` | Token expiry in milliseconds | No (defaults to `3600000` = 1 hour) |

**In IntelliJ:** Run → Edit Configurations → Environment Variables → add:
```
DB_PASSWORD=your_db_password
JWT_SECRET=your-super-secret-key-minimum-32-characters
```

**In terminal:**
```bash
export DB_PASSWORD=your_db_password
export JWT_SECRET=your-super-secret-key-minimum-32-characters
```

> ⚠️ `JWT_SECRET` must be **at least 32 characters**. A shorter key causes a `WeakKeyException` on startup.

### 4. Create the Admin user

The Admin account is seeded directly into the database. First, generate a BCrypt hash for your chosen password using the included utility:

```bash
./gradlew runHashedPass --args="YourAdminPassword"
```

Then insert the admin user into PostgreSQL:

```sql
INSERT INTO users (email, password, provider)
VALUES ('admin@company.com', '<bcrypt_hash_from_above>', 'ADMIN');
```

### 5. Run the application

```bash
./gradlew bootRun
```

The API will be available at `http://localhost:8081/api/v1`
Swagger UI available at `http://localhost:8081/swagger-ui.html`

---

## 🔐 Authentication Flow

All endpoints (except `/auth/login`) require a valid JWT token.

**Step 1 — Login to get a token:**
```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "email": "admin@company.com",
  "password": "YourAdminPassword"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "email": "admin@company.com",
  "role": "ROLE_ADMIN"
}
```

**Step 2 — Use the token on all subsequent requests:**
```http
GET /api/v1/employees
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

---

## 📡 API Reference

### Auth

| Method | Endpoint | Auth Required | Description |
|---|---|---|---|
| `POST` | `/api/v1/auth/login` | ❌ | Login and receive JWT token |
| `POST` | `/api/v1/auth/change-password` | ✅ Any | Change your own password |

### Employees

| Method | Endpoint | Auth Required | Description |
|---|---|---|---|
| `GET` | `/api/v1/employees` | ✅ Any | Get all employees (paginated) |
| `GET` | `/api/v1/employees/{id}` | ✅ Any | Get employee by ID |
| `GET` | `/api/v1/employees/find?email=` | ✅ Any | Get employee by email |
| `GET` | `/api/v1/employees/search?letter=` | ✅ Any | Search employees by name prefix (paginated) |
| `GET` | `/api/v1/employees/department/{id}` | ✅ Any | Get employees by department (paginated) |
| `POST` | `/api/v1/employees` | ✅ ADMIN | Create new employee |
| `PATCH` | `/api/v1/employees/{id}` | ✅ Any | Update employee |
| `DELETE` | `/api/v1/employees/{id}` | ✅ ADMIN | Delete employee |

**Pagination query params:** `PageNumber` (default: 0), `PageSize` (default: 2), `sortBy` (default: `id`), `direction` (`asc`/`desc`)

### Departments

| Method | Endpoint | Auth Required | Description |
|---|---|---|---|
| `GET` | `/api/v1/departments` | ✅ Any | Get all departments |
| `POST` | `/api/v1/departments` | ✅ ADMIN | Create a new department |

### PF Accounts

| Method | Endpoint | Auth Required | Description |
|---|---|---|---|
| `GET` | `/api/v1/pfaccounts` | ✅ ADMIN | Get all PF accounts |
| `POST` | `/api/v1/pfaccounts` | ✅ ADMIN | Create PF account for an employee |

---

## ⚠️ Error Response Format

All errors return a consistent JSON structure:

```json
{
  "timeStamp": "2025-06-19T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Employee not found with id: 99",
  "path": "/api/v1/employees/99"
}
```

| Status | Scenario |
|---|---|
| `401 Unauthorized` | Missing, expired, or invalid JWT |
| `403 Forbidden` | Valid token but insufficient role |
| `404 Not Found` | Resource not found |
| `409 Conflict` | Duplicate email or DB constraint violation |
| `400 Bad Request` | Validation failure (returns field-level map) |
| `500 Internal Server Error` | Unexpected server error |

---

## 👤 Employee Creation Flow

When an admin creates an employee via `POST /api/v1/employees`:

1. Employee record is saved to the `employee` table
2. A `User` record is auto-created in the `users` table with role `USER`
3. A cryptographically secure 10-character temporary password is generated
4. The temporary password is returned **once** in the response — it is not stored in plain text
5. A welcome-email request (with the temp password) is handed off to the **Email Notification microservice** (`http://localhost:8083/api/v1/notifications/welcome`) — employee creation never fails even if that service is down
6. The employee uses this password for their first login, then changes it via `/auth/change-password`

---

## ⚡ Redis Caching

The employee list is cached in Redis to avoid repeated database hits:

- `@Cacheable("employees")` — first call hits PostgreSQL; repeat calls are served straight from Redis
- `@CacheEvict("employees", allEntries = true)` — every write (create/update/delete) clears the cache so reads never go stale
- Connection configured in `application.yml` (`localhost:6379`)

Start Redis on Linux/WSL:

```bash
sudo service redis-server start
redis-cli ping   # should reply: PONG
```

---

## 🧪 Testing

| Layer | Test class | Approach |
|---|---|---|
| Controller | `EmpControllerTest` | WebMvc slice test |
| Service | `EmpServiceTest` | Unit tests with mocked dependencies |
| Repository | `EmpRepoTest` | Data JPA test on in-memory H2 |

Run the full suite:

```bash
./gradlew test
```

---

## 🔗 GitHub

[github.com/Garenafanclub/EmployeeManagement](https://github.com/Garenafanclub/EmployeeManagement)
