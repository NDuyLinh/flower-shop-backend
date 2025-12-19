# Image Upload API Documentation

## Endpoint: Create Flower with Image Upload

### Request

**URL:** `POST /api/flowers`

**Content-Type:** `multipart/form-data`

### Parameters

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| file | File | Yes | Image file (JPG/PNG, max 10MB) |
| productSlug | String | Yes | Product identifier (e.g., "flower-rose-red") |
| name | String | Yes | Flower name |
| price | BigDecimal | Yes | Price |
| description | String | No | Description |
| occasion | String | No | Occasion (Birthday, Wedding, etc.) |
| color | String | No | Color |
| stockQuantity | Integer | No | Stock quantity (default: 0) |
| categoryId | Long | No | Category ID |
| isFeatured | Boolean | No | Is featured product (default: false) |
| isHot | Boolean | No | Is hot product (default: false) |

### Validation Rules

#### File Validation
- **Allowed formats:** JPG, PNG only
- **Maximum file size:** 10MB
- **Minimum width:** 800px

#### Image Processing
The uploaded image will be processed automatically:

1. **Save Original:** Stored in `uploads/products/{year}/{month}/{productSlug}/original/`
2. **Generate 3 resized versions:**
   - **Thumb:** 300px width → saved as WebP (quality 0.75)
   - **Medium:** 600px width → saved as WebP (quality 0.80)
   - **Large:** 1200px width → saved as WebP (quality 0.83)

3. **Image Enhancement:**
   - Lanczos/Bicubic scaling for high quality
   - Light sharpen filter applied
   - WebP conversion for optimal file size

#### Directory Structure
```
uploads/products/
└── {year}/
    └── {month}/
        └── {productSlug}/
            ├── original/
            │   └── original.jpg
            ├── thumb/
            │   └── hoa.webp
            ├── medium/
            │   └── hoa.webp
            └── large/
                └── hoa.webp
```

### Example Request (cURL)

```bash
curl -X POST "http://localhost:8080/api/flowers" \
  -H "Content-Type: multipart/form-data" \
  -F "file=@/path/to/image.jpg" \
  -F "productSlug=flower-rose-red" \
  -F "name=Hoa Hồng Đỏ" \
  -F "price=150000" \
  -F "description=Hoa hồng đỏ tươi, tặng người yêu" \
  -F "occasion=Valentine" \
  -F "color=Red" \
  -F "stockQuantity=100" \
  -F "isFeatured=true" \
  -F "isHot=true"
```

### Example Request (Postman)

1. Set method to `POST`
2. URL: `http://localhost:8080/api/flowers`
3. Go to `Body` tab
4. Select `form-data`
5. Add fields:
   - `file` (type: File) - select image file
   - `productSlug` (type: Text) - "flower-rose-red"
   - `name` (type: Text) - "Hoa Hồng Đỏ"
   - `price` (type: Text) - "150000"
   - `description` (type: Text) - "Description here"
   - `occasion` (type: Text) - "Valentine"
   - `color` (type: Text) - "Red"
   - `stockQuantity` (type: Text) - "100"
   - `isFeatured` (type: Text) - "true"
   - `isHot` (type: Text) - "true"

### Success Response

**Status:** `201 Created`

```json
{
  "responseDateTime": "2025-12-19T08:00:00",
  "responseStatus": {
    "code": "00",
    "message": "Flower created successfully with images"
  },
  "responseData": {
    "id": 1,
    "name": "Hoa Hồng Đỏ",
    "price": 150000,
    "description": "Hoa hồng đỏ tươi, tặng người yêu",
    "occasion": "Valentine",
    "color": "Red"
  }
}
```

**Note:** The flower entity will have `imageUrl` set to the large version path:
`/uploads/products/2025/12/flower-rose-red/large/hoa.webp`

### Error Responses

#### Invalid File Type
**Status:** `400 Bad Request`
```json
{
  "responseDateTime": "2025-12-19T08:00:00",
  "responseStatus": {
    "code": "02",
    "message": "Validation error: Only JPG and PNG files are allowed"
  },
  "responseData": null
}
```

#### File Too Large
**Status:** `400 Bad Request`
```json
{
  "responseDateTime": "2025-12-19T08:00:00",
  "responseStatus": {
    "code": "02",
    "message": "Validation error: File size must not exceed 10MB"
  },
  "responseData": null
}
```

#### Image Width Too Small
**Status:** `400 Bad Request`
```json
{
  "responseDateTime": "2025-12-19T08:00:00",
  "responseStatus": {
    "code": "02",
    "message": "Validation error: Image width must be at least 800px"
  },
  "responseData": null
}
```

#### Server Error
**Status:** `500 Internal Server Error`
```json
{
  "responseDateTime": "2025-12-19T08:00:00",
  "responseStatus": {
    "code": "01",
    "message": "Error processing image: [error details]"
  },
  "responseData": null
}
```

---

## Alternative: JSON Endpoint (Without Image)

For creating flowers without image upload, use the JSON endpoint:

**URL:** `POST /api/flowers/json`

**Content-Type:** `application/json`

This endpoint uses the original BaseRequest wrapper format as documented in the main API overview.

---

## Access Uploaded Images

Uploaded images can be accessed via static URL:

```
http://localhost:8080/uploads/products/{year}/{month}/{productSlug}/{size}/hoa.webp
```

Examples:
- Thumb: `http://localhost:8080/uploads/products/2025/12/flower-rose-red/thumb/hoa.webp`
- Medium: `http://localhost:8080/uploads/products/2025/12/flower-rose-red/medium/hoa.webp`
- Large: `http://localhost:8080/uploads/products/2025/12/flower-rose-red/large/hoa.webp`
- Original: `http://localhost:8080/uploads/products/2025/12/flower-rose-red/original/original.jpg`

---

## Configuration

The following settings are configured in `application.properties`:

```properties
# File upload configuration
spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
spring.servlet.multipart.file-size-threshold=2KB
```

---

## Dependencies

The image processing uses the following libraries:
- **imgscalr-lib 4.2:** High-quality image resizing
- **imageio-webp 3.10.1:** WebP format support

---

## Notes

1. **Product Slug:** Must be unique for each product to avoid overwriting images
2. **Date-based folders:** Images are organized by year/month for better management
3. **WebP Fallback:** If WebP conversion fails, the system automatically falls back to PNG format
4. **Automatic Cleanup:** Use `ImageService.deleteProductImages()` to remove all images for a product
5. **Image Quality:** The quality settings are optimized for web delivery:
   - Thumb (75%): Small preview images
   - Medium (80%): Product listing pages
   - Large (83%): Product detail pages

---

**Last Updated:** December 19, 2025
