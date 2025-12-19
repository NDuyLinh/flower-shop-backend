# Flower Shop Backend - API & Database Overview

## 📊 Database Tables

### 1. **flowers**
Bảng lưu trữ thông tin sản phẩm hoa

| Column | Type | Description |
|--------|------|-------------|
| id | Long | Primary Key |
| name | String | Tên hoa |
| price | BigDecimal | Giá |
| description | String | Mô tả |
| imageUrl | String | URL hình ảnh |
| stockQuantity | Integer | Số lượng tồn kho |
| isFeatured | Boolean | Sản phẩm nổi bật |
| isHot | Boolean | Sản phẩm hot |
| viewCount | Integer | Lượt xem |
| soldCount | Integer | Số lượng đã bán |
| status | String | ACTIVE/INACTIVE |
| categoryId | Long | Foreign Key → categories |
| occasion | String | Dịp (Birthday, Wedding, Valentine...) |
| color | String | Màu sắc |
| createdAt | LocalDateTime | Ngày tạo |
| updatedAt | LocalDateTime | Ngày cập nhật |

### 2. **categories**
Bảng danh mục sản phẩm

| Column | Type | Description |
|--------|------|-------------|
| id | Long | Primary Key |
| name | String | Tên danh mục |
| description | String | Mô tả |
| imageUrl | String | URL hình ảnh |
| displayOrder | Integer | Thứ tự hiển thị |
| isFeatured | Boolean | Danh mục nổi bật |
| status | String | ACTIVE/INACTIVE |
| createdAt | LocalDateTime | Ngày tạo |
| updatedAt | LocalDateTime | Ngày cập nhật |

### 3. **banners**
Bảng banner trang chủ

| Column | Type | Description |
|--------|------|-------------|
| id | Long | Primary Key |
| title | String | Tiêu đề |
| subtitle | String | Tiêu đề phụ |
| description | String | Mô tả |
| imageUrl | String | URL hình ảnh |
| linkUrl | String | Link đích |
| bannerType | String | HOT_FLOWER/OCCASION/PROMOTION |
| displayOrder | Integer | Thứ tự hiển thị |
| isActive | Boolean | Đang hoạt động |
| startDate | LocalDateTime | Ngày bắt đầu |
| endDate | LocalDateTime | Ngày kết thúc |

### 4. **reviews**
Bảng đánh giá khách hàng

| Column | Type | Description |
|--------|------|-------------|
| id | Long | Primary Key |
| customerName | String | Tên khách hàng |
| customerEmail | String | Email khách hàng |
| customerAvatar | String | URL avatar |
| rating | Integer | Đánh giá (1-5) |
| comment | String | Nội dung đánh giá |
| flowerId | Long | Foreign Key → flowers (optional) |
| isFeatured | Boolean | Đánh giá nổi bật |
| isApproved | Boolean | Đã duyệt |
| status | String | PENDING/APPROVED/REJECTED |
| createdAt | LocalDateTime | Ngày tạo |

### 5. **shop_info**
Bảng thông tin cửa hàng (singleton)

| Column | Type | Description |
|--------|------|-------------|
| id | Long | Primary Key (always 1) |
| shopName | String | Tên cửa hàng |
| logoUrl | String | URL logo |
| aboutUs | String | Giới thiệu |
| description | String | Mô tả chi tiết |
| phone | String | Số điện thoại |
| email | String | Email |
| address | String | Địa chỉ |
| workingHours | String | Giờ làm việc |
| facebookUrl | String | Link Facebook |
| instagramUrl | String | Link Instagram |
| updatedAt | LocalDateTime | Ngày cập nhật |

---

## 🚀 API Endpoints

### **Base URL**: `/api`

### 1. Flower APIs

#### 1.1. Create Flower
```
POST /api/flowers
Content-Type: application/json

Request Body:
{
  "requestTrace": "string",
  "requestDateTime": "2025-12-18T00:00:00",
  "requestParameter": {
    "name": "Hoa Hồng Đỏ",
    "price": 150000,
    "description": "Hoa hồng đỏ tươi",
    "occasion": "Valentine",
    "color": "Red"
  }
}

Response: 201 Created
```

#### 1.2. Get All Flowers
```
GET /api/flowers

Response: 200 OK
```

#### 1.3. Get Flower by ID
```
GET /api/flowers/{id}

Response: 200 OK / 404 Not Found
```

#### 1.4. Update Flower
```
PUT /api/flowers/{id}

Request Body: (same structure as Create)

Response: 200 OK
```

#### 1.5. Delete Flower
```
DELETE /api/flowers/{id}

Response: 200 OK
```

#### 1.6. Search Flowers by Name
```
GET /api/flowers/search?name={keyword}

Response: 200 OK
```

#### 1.7. Filter by Occasion
```
GET /api/flowers/filter/occasion?occasion={occasion}

Response: 200 OK
```

#### 1.8. Filter by Color
```
GET /api/flowers/filter/color?color={color}

Response: 200 OK
```

#### 1.9. Filter by Price Range
```
GET /api/flowers/filter/price?minPrice={min}&maxPrice={max}

Response: 200 OK
```

#### 1.10. Universal Search
```
GET /api/flowers/search/universal?keyword={keyword}

Response: 200 OK
```

---

### 2. Category APIs

#### 2.1. Create Category
```
POST /api/categories

Request Body:
{
  "requestTrace": "string",
  "requestDateTime": "2025-12-18T00:00:00",
  "requestParameter": {
    "name": "Hoa Sinh Nhật",
    "description": "Các loại hoa dành cho sinh nhật"
  }
}

Response: 201 Created
```

#### 2.2. Get All Categories
```
GET /api/categories

Response: 200 OK
```

#### 2.3. Get Category by ID
```
GET /api/categories/{id}

Response: 200 OK / 404 Not Found
```

#### 2.4. Update Category
```
PUT /api/categories/{id}

Request Body: (same structure as Create)

Response: 200 OK
```

#### 2.5. Delete Category
```
DELETE /api/categories/{id}

Response: 200 OK
```

---

### 3. Banner APIs

#### 3.1. Create Banner
```
POST /api/banners

Request Body:
{
  "requestTrace": "string",
  "requestDateTime": "2025-12-18T00:00:00",
  "requestParameter": {
    "title": "Sale 50%",
    "subtitle": "Hoa Valentine",
    "description": "Giảm giá đặc biệt",
    "imageUrl": "https://...",
    "linkUrl": "/flowers/valentine",
    "bannerType": "PROMOTION",
    "displayOrder": 1,
    "isActive": true,
    "startDate": "2025-02-01T00:00:00",
    "endDate": "2025-02-14T23:59:59"
  }
}

Response: 201 Created
```

#### 3.2. Get All Banners
```
GET /api/banners

Response: 200 OK
```

#### 3.3. Get Active Banners
```
GET /api/banners/active

Response: 200 OK
```

#### 3.4. Get Banner by ID
```
GET /api/banners/{id}

Response: 200 OK
```

#### 3.5. Update Banner
```
PUT /api/banners/{id}

Request Body: (same structure as Create)

Response: 200 OK
```

#### 3.6. Delete Banner
```
DELETE /api/banners/{id}

Response: 200 OK
```

---

### 4. Review APIs

#### 4.1. Create Review
```
POST /api/reviews

Request Body:
{
  "requestTrace": "string",
  "requestDateTime": "2025-12-18T00:00:00",
  "requestParameter": {
    "customerName": "Nguyễn Văn A",
    "customerEmail": "nva@example.com",
    "customerAvatar": "https://...",
    "rating": 5,
    "comment": "Hoa đẹp, giao hàng nhanh",
    "flowerId": 1
  }
}

Response: 201 Created
```

#### 4.2. Get All Reviews (Admin)
```
GET /api/reviews

Response: 200 OK
```

#### 4.3. Get Featured Reviews
```
GET /api/reviews/featured

Response: 200 OK
```

#### 4.4. Get Approved Reviews
```
GET /api/reviews/approved

Response: 200 OK
```

#### 4.5. Get Reviews by Flower
```
GET /api/reviews/flower/{flowerId}

Response: 200 OK
```

#### 4.6. Get Review by ID
```
GET /api/reviews/{id}

Response: 200 OK
```

#### 4.7. Approve Review
```
PUT /api/reviews/{id}/approve

Response: 200 OK
```

#### 4.8. Reject Review
```
PUT /api/reviews/{id}/reject

Response: 200 OK
```

#### 4.9. Set Featured Status
```
PUT /api/reviews/{id}/featured?featured={true/false}

Response: 200 OK
```

#### 4.10. Delete Review
```
DELETE /api/reviews/{id}

Response: 200 OK
```

---

### 5. Shop Info APIs

#### 5.1. Get Shop Information
```
GET /api/shop-info

Response: 200 OK
{
  "responseDateTime": "2025-12-18T00:00:00",
  "responseStatus": {
    "code": "00",
    "message": "Shop information retrieved successfully"
  },
  "responseData": {
    "id": 1,
    "shopName": "Flower Shop",
    "logoUrl": "",
    "aboutUs": "Welcome to our flower shop",
    "description": "We provide the best flowers",
    "phone": "",
    "email": "",
    "address": "",
    "workingHours": "Mon-Sat: 8:00-20:00",
    "facebookUrl": "",
    "instagramUrl": "",
    "updatedAt": "2025-12-18T00:00:00"
  }
}
```

#### 5.2. Update Shop Information
```
PUT /api/shop-info

Request Body:
{
  "requestTrace": "string",
  "requestDateTime": "2025-12-18T00:00:00",
  "requestParameter": {
    "shopName": "My Flower Shop",
    "logoUrl": "https://...",
    "aboutUs": "Chúng tôi chuyên cung cấp hoa tươi",
    "description": "Với hơn 10 năm kinh nghiệm...",
    "phone": "0123456789",
    "email": "info@flowershop.com",
    "address": "123 ABC Street, Ho Chi Minh City",
    "workingHours": "Mon-Sat: 8:00-20:00, Sun: 9:00-18:00",
    "facebookUrl": "https://facebook.com/flowershop",
    "instagramUrl": "https://instagram.com/flowershop"
  }
}

Response: 200 OK
```

---

### 6. Homepage API (Aggregated Data)

#### 6.1. Get Homepage Data
```
GET /api/homepage

Response: 200 OK
{
  "responseDateTime": "2025-12-18T00:00:00",
  "responseStatus": {
    "code": "00",
    "message": "Homepage data retrieved successfully"
  },
  "responseData": {
    "activeBanners": [
      {
        "id": 1,
        "title": "Sale 50%",
        "subtitle": "Hoa Valentine",
        ...
      }
    ],
    "featuredCategories": [
      {
        "id": 1,
        "name": "Hoa Sinh Nhật",
        "description": "...",
        ...
      }
    ],
    "hotFlowers": [
      {
        "id": 1,
        "name": "Hoa Hồng Đỏ",
        "price": 150000,
        ...
      }
    ],
    "bestSellingFlowers": [
      {
        "id": 2,
        "name": "Hoa Ly",
        "price": 200000,
        ...
      }
    ],
    "featuredReviews": [
      {
        "id": 1,
        "customerName": "Nguyễn Văn A",
        "rating": 5,
        "comment": "...",
        ...
      }
    ],
    "shopInfo": {
      "id": 1,
      "shopName": "Flower Shop",
      "phone": "0123456789",
      ...
    }
  }
}
```

---

## 📝 Response Format

Tất cả API đều sử dụng format chuẩn:

### Success Response
```json
{
  "responseDateTime": "2025-12-18T00:00:00",
  "responseStatus": {
    "code": "00",
    "message": "Success message"
  },
  "responseData": { ... }
}
```

### Error Response
```json
{
  "responseDateTime": "2025-12-18T00:00:00",
  "responseStatus": {
    "code": "01",
    "message": "Error message"
  },
  "responseData": null
}
```

---

## 🔐 Security Configuration

- CSRF: Disabled (for API development)
- All endpoints: Public access (permitAll)
- Swagger UI: Accessible at `/swagger-ui.html`
- H2 Console: Accessible at `/h2-console` (if enabled)

---

## 📚 Swagger Documentation

Access API documentation at: **http://localhost:8080/swagger-ui.html**

---

## 🏗️ Architecture

Project follows **Clean Architecture (Hexagonal Architecture)**:

```
domain/          # Business entities & repository interfaces (no framework)
├── entity/      # Pure Java POJOs
└── repository/  # Repository interfaces

application/     # Use cases & business logic
└── service/     # Service layer

infrastructure/  # Framework implementations
└── repository/  # Repository implementations (in-memory)

presentation/    # Controllers & DTOs
├── controller/  # REST controllers
├── request/     # Request DTOs
└── response/    # Response DTOs

common/          # Configuration & utilities
└── config/      # Spring configurations
```

---

## 🎯 Features Implemented

✅ Complete CRUD operations for Flowers, Categories, Banners, Reviews, Shop Info  
✅ Advanced search & filter (name, occasion, color, price range)  
✅ Homepage aggregated API  
✅ Review approval workflow (PENDING → APPROVED/REJECTED)  
✅ Featured items (flowers, categories, reviews)  
✅ Hot flowers & best-selling tracking  
✅ Banner management with date range  
✅ Shop information management (singleton)  
✅ Clean Architecture compliance  
✅ Swagger/OpenAPI documentation  
✅ BaseRequest/BaseResponse wrapper pattern  

---

**Version**: 1.0.0  
**Last Updated**: December 18, 2025
