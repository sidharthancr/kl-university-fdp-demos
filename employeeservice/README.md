# Employee Service

A Spring Boot microservice for managing employees, secured with JWT-based authentication and role-based authorization. Built as the capstone demo for the **KL University FDP — SOA Programming & Microservices** (Day 1: REST CRUD · Day 2: Security).

## Tech stack

| | |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 4.1.0 (Web MVC, Data JPA, Validation, Security) |
| Auth | JWT (jjwt 0.12.6, HS256) + BCrypt |
| Database | MySQL 8 |
| Build | Maven (wrapper included) |

## Features

- Full **CRUD** REST API for employees (`GET`, `POST`, `PUT`, `PATCH`, `DELETE`).
- **JWT authentication** — `/api/auth/login` and `/api/auth/register` return a signed token.
- **Role-based authorization** — `USER` can read; `ADMIN` can read and write.
- Bean validation, a global exception handler, and seed data on first boot.

## Prerequisites

- JDK 17+
- MySQL running on `localhost:3306` (the schema `employee_db` is created automatically)
- No need to install Maven — use the bundled `./mvnw`

## Configuration

Defaults live in [`src/main/resources/application.properties`](src/main/resources/application.properties):

| Property | Default |
|----------|---------|
| `server.port` | `9090` |
| `spring.datasource.url` | `jdbc:mysql://localhost:3306/employee_db?createDatabaseIfNotExist=true` |
| `spring.datasource.username` / `password` | `root` / `12345678` |
| `app.jwt.secret` | demo secret (≥ 32 bytes — **override in production**) |
| `app.jwt.expiration-ms` | `3600000` (1 hour) |

Override any value with an environment variable or `--flag`, e.g. `./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8085`.

## Run

```bash
./mvnw spring-boot:run
```

The app starts on **http://localhost:9090**. On first boot it seeds:

- **Accounts:** `admin / admin123` (ADMIN) and `user / user123` (USER)
- **Sample employees:** Ada Lovelace, Alan Turing, Grace Hopper, Dennis Ritchie, Margaret Hamilton, Linus Torvalds

## API reference

### Auth (public)

| Method | Path | Body | Returns |
|--------|------|------|---------|
| POST | `/api/auth/register` | `{ "username", "password", "role" }` | `201` + `{ token, tokenType }` |
| POST | `/api/auth/login` | `{ "username", "password" }` | `200` + `{ token, tokenType }` |

### Employees (require `Authorization: Bearer <token>`)

| Method | Path | Role | Success |
|--------|------|------|---------|
| GET | `/api/employees` | USER / ADMIN | `200` |
| GET | `/api/employees/{id}` | USER / ADMIN | `200` / `404` |
| POST | `/api/employees` | ADMIN | `201` |
| PUT | `/api/employees/{id}` | ADMIN | `200` (full replace) |
| PATCH | `/api/employees/{id}` | ADMIN | `200` (partial update) |
| DELETE | `/api/employees/{id}` | ADMIN | `204` |

**Employee shape:** `{ id, firstName, lastName, email (unique), jobTitle, departmentId }`

**Authorization outcomes:** missing/invalid token → `401 Unauthorized`; valid token but insufficient role → `403 Forbidden`.

## Quick start with curl

```bash
BASE=http://localhost:9090

# 1) Log in as admin and capture the token
TOKEN=$(curl -s -X POST $BASE/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"username":"admin","password":"admin123"}' | sed -E 's/.*"token":"([^"]+)".*/\1/')

# 2) List employees
curl -s $BASE/api/employees -H "Authorization: Bearer $TOKEN"

# 3) Create an employee (ADMIN only)
curl -i -X POST $BASE/api/employees \
  -H "Authorization: Bearer $TOKEN" -H 'Content-Type: application/json' \
  -d '{"firstName":"Asha","lastName":"Rao","email":"asha.rao@klu.edu","jobTitle":"Engineer"}'
```

## Project structure

```
src/main/java/in/kluniversity/fsd/employeeservice/
├─ EmployeeserviceApplication.java   # entry point
├─ controller/EmployeeController      # REST endpoints
├─ service/EmployeeService            # business logic (incl. PATCH)
├─ repository/                        # Employee + AppUser JPA repos
├─ entity/                            # Employee, AppUser
├─ dto/EmployeePatchRequest           # partial-update payload
├─ exception/                         # EmployeeNotFoundException + global handler
├─ security/                          # JwtService, AppUserDetailsService,
│                                     #   JwtAuthenticationFilter, SecurityConfig
├─ auth/                              # AuthController + request/response records
└─ config/                           # DataSeeder (employees), UserSeeder (accounts)
```

## Notes

- This is a **teaching demo**: the JWT secret and DB password are committed for convenience. Replace them (and lock down `/api/auth/register`) before any real deployment.
- Part of the FDP teaching set; see the slides and demo guide in the [course repository](https://github.com/sidharthancr/kl-university-fdp-soa-microservices).
