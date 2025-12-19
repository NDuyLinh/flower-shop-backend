# API & Architecture Conventions

Dự án này tuân theo kiến trúc **Clean Architecture** với phân tách rõ ràng các layer.

---

## 🏗️ Quy tắc Clean Architecture

### 1. Domain Layer (`domain/`)
**Trách nhiệm:** Core business logic - độc lập hoàn toàn với framework

✅ **Được phép:**
- Entity classes (POJO thuần túy)
- Repository interfaces (chỉ định nghĩa contract)
- Domain exceptions
- Value objects
- Business rules validation

❌ **KHÔNG được phép:**
- Spring annotations (`@Entity`, `@Component`, `@Service`, v.v.)
- JPA annotations (`@Table`, `@Column`, `@Id`, v.v.)
- Bất kỳ framework dependencies nào
- HTTP/REST concerns
- Database implementation details

**Ví dụ:**
```java
// ✅ ĐÚNG - Pure domain entity
public class Flower {
    private Long id;
    private String name;
    private BigDecimal price;
    // getters, setters, equals, hashCode
}

// ❌ SAI - Có framework annotations
@Entity
@Table(name = "flowers")
public class Flower {
    @Id
    private Long id;
    // ...
}
```

---

### 2. Application Layer (`application/`)
**Trách nhiệm:** Orchestrate use cases - điều phối business logic

✅ **Được phép:**
- Service classes chứa use cases
- DTOs cho internal use cases (nếu cần)
- Application-specific exceptions
- Dependency injection qua constructor

⚠️ **Chú ý:**
- Có thể dùng `@Service` để Spring quản lý bean (nhưng không bắt buộc theo quy tắc clean)
- Chỉ phụ thuộc vào domain layer
- Không biết về HTTP requests/responses
- Không biết về database implementation

❌ **KHÔNG được phép:**
- HTTP annotations (`@RestController`, `@RequestMapping`)
- JPA/Database code
- Request/Response objects từ presentation layer
- Business logic rò rỉ ra ngoài

**Ví dụ:**
```java
// ✅ ĐÚNG - Pure use case service
@Service  // Chỉ để Spring inject, không vi phạm clean architecture
public class FlowerService {
    private final FlowerRepository flowerRepository;
    
    public FlowerService(FlowerRepository flowerRepository) {
        this.flowerRepository = flowerRepository;
    }
    
    public Flower createFlower(Flower flower) {
        // Business logic
        if (flower.getCreatedAt() == null) {
            flower.setCreatedAt(LocalDateTime.now());
        }
        return flowerRepository.save(flower);
    }
}
```

---

### 3. Presentation Layer (`presentation/`)
**Trách nhiệm:** HTTP interface - xử lý REST API requests/responses

✅ **Được phép:**
- REST Controllers (`@RestController`)
- Request/Response DTOs
- HTTP status codes
- Validation annotations (`@Valid`, `@NotNull`)
- API documentation annotations (Swagger/OpenAPI)
- Mapping giữa DTOs và domain entities

❌ **KHÔNG được phép:**
- Business logic
- Direct database access
- JPA entities trong responses
- Domain logic calculations

**Ví dụ:**
```java
// ✅ ĐÚNG - Controller không chứa business logic
@RestController
@RequestMapping("/api/flowers")
public class FlowerController {
    private final FlowerService flowerService;
    
    @PostMapping
    public ResponseEntity<BaseResponse<FlowerResponse>> createFlower(
            @Valid @RequestBody BaseRequest<CreateFlowerRequest> request) {
        
        // Extract và map
        CreateFlowerRequest createRequest = request.getRequestParameter();
        Flower flower = mapToEntity(createRequest);
        
        // Delegate to service
        Flower created = flowerService.createFlower(flower);
        
        // Map và return
        return ResponseEntity.ok(mapToResponse(created));
    }
}
```

---

### 4. Infrastructure Layer (`infrastructure/`)
**Trách nhiệm:** Technical implementation - database, external services

✅ **Được phép:**
- Repository implementations
- JPA entities (nếu cần tách khỏi domain entities)
- Database configurations
- External API clients
- Spring Data JPA repositories
- ORM mappings

❌ **KHÔNG được phép:**
- Business logic
- HTTP handling
- Use case orchestration

**Ví dụ:**
```java
// ✅ ĐÚNG - Repository implementation
@Repository
public class FlowerRepositoryImpl implements FlowerRepository {
    private final Map<Long, Flower> storage = new ConcurrentHashMap<>();
    
    @Override
    public Flower save(Flower flower) {
        if (flower.getId() == null) {
            flower.setId(generateId());
        }
        storage.put(flower.getId(), flower);
        return flower;
    }
    
    @Override
    public Optional<Flower> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }
}
```

---

## 📋 Dependency Rules

**Nguyên tắc quan trọng:** Dependencies chỉ đi theo một chiều từ ngoài vào trong:

```
Presentation → Application → Domain
Infrastructure → Domain
```

- **Domain:** Không phụ thuộc vào layer nào (core)
- **Application:** Chỉ phụ thuộc vào Domain
- **Presentation:** Phụ thuộc vào Application và Domain
- **Infrastructure:** Phụ thuộc vào Domain (implement interfaces)

---

## 📦 Định dạng API Request/Response

### Request Format (BaseRequest)
Tất cả HTTP request có body **bắt buộc** phải tuân theo cấu trúc wrapper:

```json
{
  "requestTrace": "string (UUID hoặc trace ID)",
  "requestDateTime": "2025-12-15T10:30:00",
  "requestParameter": {
    // actual request payload - specific to each endpoint
  }
}
```

**Ví dụ - Create Flower Request:**
```json
{
  "requestTrace": "550e8400-e29b-41d4-a716-446655440000",
  "requestDateTime": "2025-12-15T10:30:00",
  "requestParameter": {
    "name": "Rose",
    "price": 25000,
    "description": "Beautiful red rose"
  }
}
```

---

### Response Format (BaseResponse)
Tất cả HTTP response **bắt buộc** phải tuân theo cấu trúc wrapper:

```json
{
  "responseDateTime": "2025-12-15T10:30:01",
  "responseStatus": {
    "responseCode": "00",
    "responseMessage": "Success"
  },
  "responseData": {
    // actual response payload - specific to each endpoint
  }
}
```

**Response Status Codes:**
- `00` - Success
- `01` - Validation Error
- `02` - Business Logic Error
- `03` - Not Found
- `04` - Unauthorized
- `99` - System Error

**Ví dụ - Create Flower Response (Success):**
```json
{
  "responseDateTime": "2025-12-15T10:30:01",
  "responseStatus": {
    "responseCode": "00",
    "responseMessage": "Success"
  },
  "responseData": {
    "id": 1,
    "name": "Rose",
    "price": 25000,
    "description": "Beautiful red rose"
  }
}
```

**Ví dụ - Error Response:**
```json
{
  "responseDateTime": "2025-12-15T10:30:01",
  "responseStatus": {
    "responseCode": "01",
    "responseMessage": "Validation failed: Price must be greater than 0"
  },
  "responseData": null
}
```

---

## 🔧 Common Layer (`common/`)

**Trách nhiệm:** Shared utilities và configurations

✅ **Được phép:**
- Configuration classes (`@Configuration`)
- Exception handlers (`@ControllerAdvice`)
- Security configs
- Swagger/OpenAPI configs
- Constants
- Utility classes
- Custom annotations

**Ví dụ:**
```java
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI flowerShopOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Flower Shop API")
                        .version("1.0.0"));
    }
}
```

---

## 🧪 Code Review Checklist

### Domain Layer
- [ ] Không có Spring/JPA annotations
- [ ] Entities là POJO thuần túy
- [ ] Repository chỉ là interfaces
- [ ] Không có dependencies đến layers khác

### Application Layer
- [ ] Services chỉ chứa use case logic
- [ ] Không có HTTP handling code
- [ ] Không có database implementation details
- [ ] Chỉ phụ thuộc vào domain layer

### Presentation Layer
- [ ] Controllers không chứa business logic
- [ ] Chỉ mapping và validation
- [ ] Sử dụng BaseRequest/BaseResponse wrapper
- [ ] Có Swagger annotations cho documentation

### Infrastructure Layer
- [ ] Repository implementations hoàn chỉnh
- [ ] Không chứa business logic
- [ ] Implement đúng interfaces từ domain

---

## 📝 Naming Conventions

### Packages
- `domain.entity` - Domain entities
- `domain.repository` - Repository interfaces
- `application.service` - Use case services
- `presentation.controller` - REST controllers
- `presentation.request` - Request DTOs
- `presentation.response` - Response DTOs
- `infrastructure.repository` - Repository implementations
- `common.config` - Configuration classes
- `common.exception` - Custom exceptions

### Classes
- Entity: `Flower`, `Order`, `Customer`
- Repository Interface: `FlowerRepository`, `OrderRepository`
- Repository Implementation: `FlowerRepositoryImpl`
- Service: `FlowerService`, `OrderService`
- Controller: `FlowerController`, `OrderController`
- Request DTO: `CreateFlowerRequest`, `UpdateFlowerRequest`
- Response DTO: `FlowerResponse`, `OrderResponse`

### Methods
- Controller: `createFlower()`, `getFlowerById()`, `updateFlower()`
- Service: `createFlower()`, `findFlowerById()`, `updateFlower()`
- Repository: `save()`, `findById()`, `findAll()`, `deleteById()`

---

## ⚠️ Common Violations

### ❌ ANTI-PATTERNS

**1. Business Logic trong Controller:**
```java
// ❌ SAI
@PostMapping
public ResponseEntity<?> createFlower(@RequestBody CreateFlowerRequest request) {
    if (request.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
        throw new InvalidPriceException(); // Business logic!
    }
    // ...
}
```

**2. Framework Annotations trong Domain:**
```java
// ❌ SAI
@Entity
@Table(name = "flowers")
public class Flower {
    @Id
    private Long id;
}
```

**3. Direct Database Access trong Controller:**
```java
// ❌ SAI
@RestController
public class FlowerController {
    @Autowired
    private JpaRepository<Flower, Long> jpaRepository; // Không được!
}
```

**4. Trả về Domain Entity trực tiếp:**
```java
// ❌ SAI
@GetMapping("/{id}")
public ResponseEntity<Flower> getFlower(@PathVariable Long id) {
    return ResponseEntity.ok(flowerService.findById(id));
}

// ✅ ĐÚNG - Dùng BaseResponse wrapper
@GetMapping("/{id}")
public ResponseEntity<BaseResponse<FlowerResponse>> getFlower(@PathVariable Long id) {
    Flower flower = flowerService.findById(id);
    FlowerResponse response = mapToResponse(flower);
    return ResponseEntity.ok(buildBaseResponse("00", "Success", response));
}
```

---

## 📚 Best Practices

1. **Dependency Injection:** Dùng constructor injection, không dùng field injection
2. **Immutability:** Ưu tiên final fields khi có thể
3. **Validation:** Request validation ở presentation layer, business validation ở application layer
4. **Exception Handling:** Dùng @ControllerAdvice để xử lý exceptions tập trung
5. **Logging:** Log ở service layer, không log sensitive data
6. **Testing:** Viết unit tests cho domain và application layers, integration tests cho presentation
7. **Documentation:** Sử dụng Swagger/OpenAPI annotations cho API documentation

---

## 🎯 Summary

**Clean Architecture Goals:**
- **Testability:** Dễ test từng layer độc lập
- **Maintainability:** Dễ maintain và mở rộng
- **Flexibility:** Dễ thay đổi technology stack
- **Independence:** Domain logic độc lập với framework

**Key Principles:**
- Domain không phụ thuộc vào gì cả
- Application chỉ phụ thuộc vào Domain
- Presentation và Infrastructure phụ thuộc vào Domain
- Luôn dùng BaseRequest/BaseResponse wrapper cho API
