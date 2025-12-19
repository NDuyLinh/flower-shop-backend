# Image Processing Service Documentation

## Tổng quan
Service xử lý upload và resize ảnh sản phẩm với **2-step optimization**:
1. JPEG optimization với quality control để đạt target size
2. Convert JPEG → WebP để giảm thêm 24-26% file size

## Dependencies

### 1. imgscalr-lib (v4.2)
**Mục đích:** High-quality image resizing

```xml
<dependency>
    <groupId>org.imgscalr</groupId>
    <artifactId>imgscalr-lib</artifactId>
    <version>4.2</version>
</dependency>
```

**Chức năng:**
- Resize ảnh với thuật toán QUALITY (multi-step scaling)
- Mode FIT_TO_WIDTH để giữ tỷ lệ
- OP_ANTIALIAS để smooth edges
- Pure Java, không cần native library

**Sử dụng trong code:**
```java
BufferedImage resizedImage = Scalr.resize(
    originalImage,
    Scalr.Method.QUALITY,      // High quality resize
    Scalr.Mode.FIT_TO_WIDTH,   // Fit to width, maintain aspect ratio
    resizeWidth,
    Scalr.OP_ANTIALIAS         // Smooth edges
);
```

### 2. cwebp Command Line Tool
**Mục đích:** Convert JPEG → WebP với high-quality compression

**Installation (macOS):**
```bash
brew install webp
```

**Installation (Linux):**
```bash
sudo apt-get install webp
# hoặc
sudo yum install libwebp-tools
```

**Chức năng:**
- Convert images to WebP format
- Quality control (0-100)
- Better compression than pure Java libraries
- No architecture compatibility issues

**Sử dụng trong code:**
```java
ProcessBuilder builder = new ProcessBuilder(
    "cwebp",
    "-q", "80",           // Quality 80%
    inputJpgPath,
    "-o", outputWebpPath
);
Process process = builder.start();
process.waitFor();        // Wait for conversion
```

**Fallback:** Nếu cwebp không available, giữ file JPEG

### 3. Java Standard Library

#### javax.imageio.*
```java
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
```
**Chức năng:**
- ImageIO: Core image I/O operations
- ImageWriter: Write images với compression settings
- ImageWriteParam: Configure quality, compression mode
- ImageOutputStream: Output stream cho ImageWriter

#### java.awt.*
```java
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
```
**Chức năng:**
- BufferedImage: In-memory image representation
- Graphics2D: Draw và convert images
- ConvolveOp: Apply convolution filters (sharpen)
- Kernel: Define filter matrix

#### java.io.*
```java
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
```
**Chức năng:**
- File operations
- FileOutputStream: Write images to disk

#### java.nio.file.*
```java
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
```
**Chức năng:**
- Modern file system operations
- Directory creation và deletion

## Chức năng chính

### 1. Upload và xử lý ảnh
**Method:** `uploadProductImage(MultipartFile file, String productSlug)`

**Input:**
- `file`: MultipartFile từ HTTP request
- `productSlug`: Tên slug của sản phẩm (dùng trong path)

**Output:**
```java
ImageUploadResult {
    String originalUrl;  // /uploads/products/2025/12/test-flower/original/hoa.jpg
    String thumbUrl;     // /uploads/products/2025/12/test-flower/thumb/hoa.jpg
    String mediumUrl;    // /uploads/products/2025/12/test-flower/medium/hoa.jpg
    String largeUrl;     // /uploads/products/2025/12/test-flower/large/hoa.jpg
}
```

**Validation:**
- File type: JPG, PNG only
- Max size: 10MB
- Min width: 800px

### 2. Resize với 3 sizes

| Size | Width | Target File Size | Use Case |
|------|-------|-----------------|----------|
| Thumb | 300px | ≤ 50KB | Thumbnails, list views |
| Medium | 600px | ≤ 120KB | Product cards, galleries |
| Large | 1200px | ≤ 250KB | Product details, zoom |

### 3. Optimization Features

#### FIX 1: Prevent Upscaling
```java
int resizeWidth = Math.min(originalWidth, targetWidth);
```
- Không phóng to ảnh nhỏ hơn target width
- Giữ nguyên resolution nếu ảnh gốc nhỏ

#### FIX 2: Conditional Sharpen
```java
if (targetWidth >= LARGE_WIDTH) {
    resizedImage = applySharpen(resizedImage);
}
```
- Chỉ apply sharpen filter cho ảnh large (≥1200px)
- Thumb và medium không cần sharpen

**Sharpen Matrix (3x3 Convolution Kernel):**
```
 0.0  -0.25   0.0
-0.25  2.0   -0.25
 0.0  -0.25   0.0
```

#### FIX 3: File Size Limit với Re-encoding
```java
float quality = 0.80f;  // Start quality
while (fileSize > maxFileSize && attempts < 15) {
    encode(image, quality);
    if (fileSize <= maxFileSize) break;
    quality -= 0.05f;  // Reduce by 5%
}
```

**Logic:**
1. Resize ảnh về target width
2. Encode JPEG với quality 0.80 (80%)
3. Nếu file size > target → giảm quality 0.05 (5%)
4. Re-encode và check lại
5. Lặp Two-Step Optimization Strategy

**Step 1: JPEG Optimization**
```java
private File saveAsJPEGWithSizeLimit(BufferedImage image, String outputPath, String sizeName) {
    float quality = 0.80f;  // Start with 80%
    while (fileSize > maxFileSize && attempts < 15) {
        saveAsJPEG(image, file, quality);
        if (fileSize <= maxFileSize) break;
        quality -= 0.05f;  // Reduce by 5% each attempt
    }
    return file;
}
```

**Target sizes:**
- Thumb: ≤ 50KB
- Medium: ≤ 120KB
- Large: ≤ 250KB

**Step 2: JPEG → WebP Conversion**
```java
private File convertJPGtoWebP(String jpgPath, String webpPath) {
    ProcessBuilder builder = new ProcessBuilder(
        "cwebp",
        "-q", "80",
        jpgPath,
        "-o", webpPath
    );
    Process process = builder.start();
    process.waitFor();
    
    // Delete temp JPG, keep WebP (24-26% smaller)
    jpgFile.delete(); # Original file (uploaded)
                    ├── thumb/
                    │   └── hoa.webp   # 300px, ~20KB (WebP)
                    ├── medium/
                    │   └── hoa.webp   # 600px, ~67KB (WebP)
                    └── large/
                        └── hoa.webp   # 1200px, ~241KB (WebP)
```

**Note:** Temp JPG files (hoa_temp.jpg) được tạo trong quá trình xử lý nhưng sẽ tự động xóa sau khi convert sang WebP thành công.edium: 88KB JPG → 67KB WebP (**-24%**)
- Large: 241KB JPG → 241KB WebP (same quality)

**Tại sao 2 steps?**
1. JPEG optimization: Đảm bảo đạt target size trước
2. WebP conversion: Giảm thêm ~25% mà không mất quality
3. Fallback safe: Nếu WebP fail, vẫn có JPEG tối ưu writer.write(null, new IIOImage(rgbImage, null, null), writeParam);
}
```

## Directory Structure

```
project-root/
└── uploads/
    └── products/
        └── {year}/          # 2025
            └── {month}/     # 12
                └── {productSlug}/    # test-flower
                    ├── original/
                    │   └── hoa.jpg   # Original file
                    ├── thumb/
                    │   └── hoa.jpg   # 300px, ≤50KB
                    ├── medium/
                    │   └── hoa.jpg   # 600px, ≤120KB
                    └── large/
                        └── hoa.jpg   # 1200px, ≤250KB
```

## Configuration

### Spring Boot application.properties
```properties
# File upload limits
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

### Static Resource Serving
```java
@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}
```

## API Usage

### cURL Example
```bash
curl -X POST "http://localhost:8080/api/flowers" \
  -H "Content-Type: multipart/form-data" \
  -F "file=@/path/to/image.jpg" \
  -F "name=Rose Flower" \
  -F "price=150000" \
  -F "productSlug=rose-flower"
```

### Response
```json
{
  "id": 1,
  "name": "Rose Flower",
  "price": 150000,
  "imageUrls": {webp",
    "mediumUrl": "/uploads/products/2025/12/rose-flower/medium/hoa.webp",
    "largeUrl": "/uploads/products/2025/12/rose-flower/large/hoa.webp",
    "mediumUrl": "/uploads/products/2025/12/rose-flower/medium/hoa.jpg",
    "largeUrl": "/uploads/products/2025/12/rose-flower/large/hoa.jpg"
  }
}
```

## Performance

### Processing Time (Real World)
- File upload: < 100ms
- Resize 3 sizes: 200-500ms (imgscalr)
- JPEG optimization: 0-2000ms (quality loop)
- WebP conversion: 300-800ms (cwebp x3)
- **Total: ~1-4 seconds**

### File Size Results (JPEG → WebP)
**Original: 400KB JPG (1200x1800px)**

| Size | Target | JPEG Optimized | WebP Final | Reduction |
|------|--------|----------------|------------|-----------|
| Thumb | ≤50KB | 27KB | **20KB** | -26% |
| Medium | ≤120KB | 88KB | **67KB** | -24% |
| Large | ≤250KB | 241KB | **241KB** | ~0% |

**Bandwidth saved:** ~21KB/image × 1000 images/day = **~20MB/day**

## Error Handling

### Validation Errors
```java
throw new IllegalArgumentException("Only JPG and PNG files are allowed");
throw new IllegalArgumentException("File size must not exceed 10MB");
throw new IllegalArgumentException("Image width must be at least 800px");
```

### Fallback Strategy
```java
try {
    saveAsWebP(image, path, quality);  // Try WebP
} catch (Exception e) {
    saveAsJPEG(image, file, quality);  // Fallback to JPEG
}
```

## Logging


Generating thumb image (300px)...
  → JPG saved: 27.0 KB
  → WebP saved: 20.0 KB (saved 7.0 KB = 26%)
thumb complete: .../thumb/hoa.webp (20.0 KB)

Generating medium image (600px)...
  → JPG saved: 88.0 KB
  → WebP saved: 67.0 KB (saved 21.0 KB = 24%)
medium complete: .../medium/hoa.webp (67.0 KB)

Generat~~WebP Support~~ ✅ DONE
- Đã implement với cwebp tool
- JPEG → WebP giảm 24-26% file size
- Fallback sang JPEG nếu cwebp không available
thumb saved: .../thumb/hoa.jpg (48.3 KB)
=== Upload Complete ===
```

## Future Improvements

### 1. WebP Support (Production Ready)
**Option A:** Sử dụng native library với ARM64 support
```xml
<dependency>
    <groupId>com.github.gotson</groupId>
    <artifactId>webp-imageio</artifactId>
    <version>0.2.2</version> <!-- Có ARM64 support -->
</dependency>
```

**Option B:** External service (imagemagick, sharp, etc.)

### 2. Async Processing
```java
@Async
public CompletableFuture<ImageUploadResult> uploadProductImageAsync(...)
```

### 3. Cloud Storage
- Upload to S3/CloudStorage thay vì local disk
- CDN cho static files

### 4. Progressive JPEG
```java
writeParam.setProgressiveMode(ImageWriteParam.MODE_DEFAULT);
```

### 5. Image Optimization Service
- Integrate với TinyPNG, Cloudinary, etc.
- Better compression algorithms

### 6. Lazy Image Generation
- Chỉ generate on-demand
- Cache resized images

## Testing

### Unit Test Example
```java
@Test
void shouldResizeImageToThumb() throws IOException {
    // Given
    MultipartFile file = createMockImageFile(1000, 1000);
    
    // When
    ImageUploadResult result = imageService.uploadProductImage(file, "test");
    
    // Then
    File thumbFile = new File(projectRoot + result.getThumbUrl());
    assertTrue(thumbFile.exists());
    assertTrue(thumbFile.length() <= 50 * 1024); // ≤ 50KB
}
```cwebp not found
**Symptom:** Log shows "cwebp not found, skipping WebP conversion"
**Cause:** cwebp tool chưa được cài đặt
**Solution:**
```bash
# macOS
brew install webp

# Linux (Ubuntu/Debian)
sudo apt-get install webp
4: Architecture mismatch (RESOLVED)
**Previous Issue:** Native WebP libraries có x86_64/arm64 compatibility problems
**Solution:** Sử dụng cwebp CLI tool thay vì native library
- CLI tool tự động detect architecture
- Không cần Java native bindings
- Works trên tất cả platforms (macOS ARM64, Linux x86_64, etc.

### Issue 2: WebP conversion failed
**Symptom:** Log shows "WebP conversion failed, keeping JPG"
**Cause:** cwebp process error hoặc permission issues
**Solution:** 
- Check cwebp installation: `which cwebp`
- Test manually: `cwebp -q 80 input.jpg -o output.webp`
- Check file permissions
- Fallback: Service tự động giữ file JPEG

### Issue 3* Log shows "WebP not available, saved as JPEG instead"
**Cause:** TwelveMonkeys chỉ hỗ trợ READ WebP, không WRITE
**Solution:** Đang dùng JPEG fallback, hoặc upgrade dependency

### Issue 2: File sizes too large
**cwebp | System | JPEG → WebP conversion | CLI Tool |
| Java ImageIO | Built-in | Image I/O (JPEG) | JDK |
| Java AWT | Built-in | Image processing | JDK |

**Total External Dependencies:** 1 JAR + 1 CLI tool
**Installation Required:** `brew install webp` (macOS) or `apt-get install webp` (Linux)
**Architecture Support:** Universal (cwebp tự detect
## Dependencies Summary

| Library | Version | Purpose | Type |
|---------|---------|---------|------|
| imgscalr-lib | 4.2 | High-quality resize | Pure Java |
| imageio-core | 3.11.0 | ImageIO extensions | Pure Java |
| imageio-webp | 3.11.0 | WebP READ support | Pure Java |
| imageio-metadata | 3.11.0 | Image metadata | Pure Java |
| common-image | 3.11.0 | Common utilities | Pure Java |
| Java ImageIO | Built-in | Image I/O | JDK |
| Java AWT | Built-in | Image processing | JDK |

**Total External Dependencies:** 5 JARs (~500KB)
**Native Dependencies:** 0 (Pure Java solution)

---

**Author:** ImageService Implementation
**Last Updated:** December 19, 2025
**Version:** 1.0.0
