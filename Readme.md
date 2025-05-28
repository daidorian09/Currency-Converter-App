# 💱 FX Currency Exchange Service

## Overview

This is a robust, production-grade **Currency Exchange API** built using **Spring Boot**. The system supports:

- Real-time currency rate retrieval via an external **Fixer.io** API
- Caching with **Redis + Redisson**
- Periodic exchange rate synchronization via **scheduled jobs**
- **Bulk currency conversion** from uploaded CSV files
- **Domain-Driven Design** (DDD) principles with clean modular structure
- Rich validation, layered exception handling, and OpenAPI documentation

## 🚀 Features

- 🔁 **Currency Conversion API** – Converts any supported amount between two currencies.
- 💹 **Exchange Rate API** – Fetches current exchange rate from in-memory cache or fallback to DB.
- 🗃️ **CSV Upload API** – Processes uploaded CSV files containing conversion requests.
- 📥 **Job Scheduler** – Periodically fetches and updates exchange rates using Fixer.
- 🧠 **Smart Caching** – Uses Redis-based Redisson cache with TTL and idle eviction.
- 📜 **Specification-based Filtering** – For paginated conversion history.
- 📈 **99%+ Code Coverage** – Thorough unit tests for service, mapper, controller, and exception layers.

---

## 🧪 Test Coverage

✅ **90+ test cases** written  
✅ **99% code coverage**  
✅ Covers:
- Service logic
- Controller endpoints (success + failure)
- Mapper transformations
- Exception handling
- Scheduled jobs
- Concurrency scenarios (Redisson locking)
- CSV parsing with malformed input rows

Sample stack used:
```shell
JUnit 5 + Mockito + Spring Boot Test + MockMvc + @DataJpaTest + Embedded H2
```

---

## 🔧 Technologies Used

| Layer                | Technology                       |
|---------------------|----------------------------------|
| Framework           | Spring Boot 3.x                  |
| Language            | Java 17 / 21                     |
| API Documentation   | Springdoc OpenAPI (Swagger)      |
| External API        | Fixer.io                         |
| Cache               | Redis (Redisson-based)           |
| DB                  | H2 (in-memory)                   |
| Scheduling          | Spring TaskScheduler + Cron      |
| Testing             | JUnit 5, Mockito, AssertJ        |

---

## 📁 Project Structure

```
src/
├── api/             // Controllers + DTOs
├── application/     // Service interfaces & implementations
├── domain/          // Domain models, enums, validation
├── infrastructure/  // Adapters: Repository, Redisson, Scheduler, External clients
├── config/          // App-wide configuration
├── exception/       // Custom exception hierarchy
└── util/            // Utility classes and validators
```

---

## 🔄 Running the Application

### 1. Start Redis with Docker Compose

```bash
docker-compose up -d redis
```

### 2. Build & Run

```bash
./mvnw clean package -DskipTests
./mvnw spring-boot:run
```

### 3. Swagger UI

Visit: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## 📂 Sample Endpoints

| Endpoint                   | Method | Description                    |
|---------------------------|--------|--------------------------------|
| `/api/currency/rate`      | GET    | Get exchange rate              |
| `/api/currency/convert`   | POST   | Convert currency               |
| `/api/conversion/history` | GET    | View conversion history        |
| `/api/conversion/upload`  | POST   | Bulk conversion via CSV upload|

---

## ⚙️ Configuration Tips

### Fixer Only Supports EUR as Base:
Make sure to define your currency pairs accordingly. Example:

```properties
exchange.job.currency-pairs[0]=EUR,USD
exchange.job.currency-pairs[1]=EUR,TRY
exchange.job.currency-pairs[2]=EUR,GBP
exchange.job.currency-pairs[3]=EUR,JPY
exchange.job.currency-pairs[4]=EUR,CAD
```

---

## 📬 Contact

Feel free to open issues or suggestions via GitHub.