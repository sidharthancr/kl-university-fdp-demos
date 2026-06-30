# KL University FDP — Demos (SOA & Microservices)

Incrementally-built microservices stack for the KL University FDP on **SOA Programming & Microservices**.
Each service is a standalone Spring Boot app, package base `in.kluniversity.fsd.*`.

## Architecture (Day 3)

```
                       ┌─────────────────────┐
        clients ─────► │  api-gateway :9000  │  single front door
                       └──────────┬──────────┘
                                  │ lb://SERVICE-ID (resolved via Eureka)
              ┌───────────────────┼───────────────────┐
              ▼                                        ▼
   ┌────────────────────┐                  ┌────────────────────────┐
   │ employee-service   │                  │ department-service     │
   │ :9090 (JWT secured)│                  │ :8082                  │
   │ employee_db        │                  │ department_db          │
   └─────────┬──────────┘                  └───────────┬────────────┘
             │  register / heartbeat                   │
             └──────────────┬──────────────────────────┘
                            ▼
                 ┌────────────────────────┐
                 │ eureka-server :8761    │  service registry
                 └────────────────────────┘
```

## Services

| Service | Folder | Port | Description |
|---------|--------|------|-------------|
| Eureka server | [`eurekaserver/`](eurekaserver) | 8761 | Service registry (`@EnableEurekaServer`) |
| department-service | [`departmentservice/`](departmentservice) | 8082 | CRUD for academic departments, Eureka client, `department_db` |
| employee-service | [`employeeservice/`](employeeservice) | 9090 | CRUD for faculty/staff + Spring Security/JWT, Eureka client, `employee_db` |
| api-gateway | [`apigateway/`](apigateway) | 9000 | Spring Cloud Gateway (reactive), routes by path to `lb://SERVICE-ID` |

## Versions

- Spring Boot **4.1.0**, Spring Cloud **2025.1.2** (pinned via BOM in `dependencyManagement`)
- Java 21+, MySQL 8.x (`root` / `12345678`)

## Prerequisites

- JDK 21+
- MySQL running on `localhost:3306` (`root` / `12345678`). Databases are created automatically.
- Maven (system `mvn` — `employeeservice`'s wrapper is incomplete, so it uses system `mvn`)

## Run everything

```bash
./run-all.sh      # eureka -> department -> employee -> gateway (in order)
# wait ~30s for Eureka to propagate
./verify.sh       # full end-to-end contract through the gateway (:9000)
./stop-all.sh     # stop all four services
```

Then open the **Eureka dashboard** at http://localhost:8761 — you should see
`APIGATEWAY`, `DEPARTMENTSERVICE`, and `EMPLOYEESERVICE` registered and UP.

### Run a service individually

```bash
cd eurekaserver      && ./mvnw spring-boot:run    # :8761
cd departmentservice && ./mvnw spring-boot:run    # :8082
cd employeeservice   && mvn  spring-boot:run      # :9090  (use system mvn)
cd apigateway        && ./mvnw spring-boot:run    # :9000
```

## Everything through one door (`:9000`)

```bash
GW=http://localhost:9000

# login (routed to employee-service) -> JWT
TOKEN=$(curl -s -X POST $GW/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"username":"admin","password":"admin123"}' \
  | sed -E 's/.*"token":"([^"]+)".*/\1/')

curl -i $GW/api/employees                              # 401 (no token)
curl    $GW/api/employees -H "Authorization: Bearer $TOKEN"   # 200
curl    $GW/api/departments                            # 200 (new service)
```

The gateway routes, but each service still owns its own security — `GET /api/employees`
without a token returns **401** even through the gateway.

## Accounts (employee-service)

| Username | Password | Role |
|----------|----------|------|
| `admin` | `admin123` | ADMIN (read + write) |
| `user` | `user123` | USER (read only) |

## Build order / dependency

`eureka-server` → `department-service` → `employee-service` → `api-gateway`
(registry first, gateway last). Give Eureka ~30s to populate before hitting the gateway,
or the first gateway call may return **503** while it discovers the service.
