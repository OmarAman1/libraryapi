# Library API

A secure and optimized REST API built with Spring Boot for managing authors, books, and loans in a library system.

---

## Features

- CRUD operations for authors and books
- Loan management
- Spring Security with Basic Authentication
- Strict CORS configuration
- DTO validation using Jakarta Validation
- Redis caching for improved performance
- Pagination with Pageable
- Rate limiting using Bucket4j
- Integration and concurrency testing
- JaCoCo test coverage

---

## Technologies

- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- H2 Database
- Redis
- Bucket4j
- Maven
- JUnit 5

---

## Authentication

All API endpoints require Basic Authentication.

Username:

```text
admin
```

Password:

```text
admin123
```

---

## Start the Project

### 1. Start Redis

```bash
docker start redis-library
```

If the container does not exist:

```bash
docker run --name redis-library -p 6379:6379 -d redis
```

### 2. Start the application

```bash
mvn spring-boot:run
```

---

## Run Tests

```bash
mvn test
```

---

## API Endpoints

### Authors

| Method | Endpoint |
|---|---|
| POST | `/api/v1/authors` |
| GET | `/api/v1/authors/{id}` |
| GET | `/api/v1/authors/{id}/books` |

### Books

| Method | Endpoint |
|---|---|
| POST | `/api/v1/books` |
| GET | `/api/v1/books/{id}` |
| GET | `/api/v1/books` |

Pagination example:

```text
/api/v1/books?page=0&size=5
```

### Loans

| Method | Endpoint |
|---|---|
| POST | `/api/v1/loans` |
| GET | `/api/v1/loans` |

Pagination example:

```text
/api/v1/loans?page=0&size=5
```

---

## Security

The API includes:

- Spring Security authentication
- Strict CORS policy
- DTO validation
- Spring Vault support preparation

Example validation annotations:

```java
@NotBlank
@Size(min = 2, max = 100)
```

---

## Redis Caching

Redis caching is implemented for:

```text
GET /api/v1/books/{id}
```

The endpoint uses:

```java
@Cacheable(value = "books", key = "#id")
```

---

## Rate Limiting

Bucket4j is used to limit requests per IP address.

Current configuration:

```text
10 requests per minute per IP
```

---

## Performance Test

A performance benchmark was performed before and after Redis caching.

### Results

| Test | Response Time |
|---|---|
| Before cache | 690.0847 ms |
| After cache | 9.1756 ms |

Performance improvement:

```text
98.67%
```

---

## Test Coverage

The project includes:

- Integration tests
- Validation tests
- Concurrency tests
- JaCoCo coverage reports

---

## Spring Vault

The project is prepared for secure secret management using Spring Vault.

Vault support is currently disabled locally:

```properties
spring.cloud.vault.enabled=false
```

---

## Author

Backend API assignment project.

Omar Aman 