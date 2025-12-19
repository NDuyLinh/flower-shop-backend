package com.jett.flowershop.application.service;

import com.jett.flowershop.application.dto.ImageUploadResult;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Service for image upload and processing
 * Images are saved to: project-root/uploads/products/{year}/{month}/{productSlug}/
 */
@Service
public class ImageService {

    private final String projectRoot;
    private static final String BASE_UPLOAD_DIR = "uploads/products";
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final int MIN_WIDTH = 800;
    private static final List<String> ALLOWED_TYPES = Arrays.asList("image/jpeg", "image/png");

    // Image sizes
    private static final int THUMB_WIDTH = 300;
    private static final int MEDIUM_WIDTH = 600;
    private static final int LARGE_WIDTH = 1200;

    // WebP quality settings
    private static final float THUMB_QUALITY = 0.75f;
    private static final float MEDIUM_QUALITY = 0.80f;
    private static final float LARGE_QUALITY = 0.83f;

    public ImageService() {
        // Get project root directory (where pom.xml is located)
        this.projectRoot = System.getProperty("user.dir");
        System.out.println("ImageService initialized - Project root: " + this.projectRoot);
    }

    /**
     * Upload and process product image
     * Saves to: project-root/uploads/products/{year}/{month}/{productSlug}/
     */
    public ImageUploadResult uploadProductImage(MultipartFile file, String productSlug) throws IOException {
        // Validate file
        validateFile(file);

        // Read image
        BufferedImage originalImage = ImageIO.read(file.getInputStream());
        if (originalImage == null) {
            throw new IllegalArgumentException("Invalid image file");
        }

        // Validate dimensions
        if (originalImage.getWidth() < MIN_WIDTH) {
            throw new IllegalArgumentException("Image width must be at least " + MIN_WIDTH + "px");
        }

        // Create directory structure: project-root/uploads/products/{year}/{month}/{productSlug}/
        LocalDateTime now = LocalDateTime.now();
        String year = String.valueOf(now.getYear());
        String month = String.format("%02d", now.getMonthValue());
        
        // Relative path for URL
        String relativePath = BASE_UPLOAD_DIR + "/" + year + "/" + month + "/" + productSlug;
        
        // Absolute path for file system
        String absoluteBasePath = projectRoot + File.separator + relativePath.replace("/", File.separator);

        System.out.println("=== Image Upload ===");
        System.out.println("Product slug: " + productSlug);
        System.out.println("Saving to: " + absoluteBasePath);

        // Save original image to: uploads/products/{year}/{month}/{productSlug}/original/
        String originalDirPath = absoluteBasePath + File.separator + "original";
        createDirectoryIfNotExists(originalDirPath);
        
        String originalExtension = getFileExtension(file.getOriginalFilename());
        String originalFileName = "hoa." + originalExtension;
        File originalFile = new File(originalDirPath, originalFileName);
        
        System.out.println("Original image: " + originalFile.getAbsolutePath());
        file.transferTo(originalFile);

        // Generate resized images (all start with quality 0.80, reduce if needed)
        String thumbFileName = generateResizedImage(originalImage, absoluteBasePath, "thumb", THUMB_WIDTH);
        String mediumFileName = generateResizedImage(originalImage, absoluteBasePath, "medium", MEDIUM_WIDTH);
        String largeFileName = generateResizedImage(originalImage, absoluteBasePath, "large", LARGE_WIDTH);

        System.out.println("=== Upload Complete ===\n");

        // Return relative URLs for web access
        return new ImageUploadResult(
                "/" + relativePath + "/original/" + originalFileName,
                "/" + relativePath + "/thumb/" + thumbFileName,
                "/" + relativePath + "/medium/" + mediumFileName,
                "/" + relativePath + "/large/" + largeFileName
        );
    }

    /**
     * Validate uploaded file
     */
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is required");
        }

        // Check file type
        String contentType = file.getContentType();
        if (!ALLOWED_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("Only JPG and PNG files are allowed");
        }

        // Check file size
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size must not exceed 10MB");
        }
    }

    /**
     * Generate resized image with WebP conversion
     * Always starts with quality 0.80, reduces by 0.05 if file size exceeds target
     */
    private String generateResizedImage(BufferedImage originalImage, String absoluteBasePath, 
                                       String sizeName, int targetWidth) throws IOException {
        System.out.println("Generating " + sizeName + " image (" + targetWidth + "px)...");
        
        // FIX 1: Prevent upscaling - don't make images larger than original
        int originalWidth = originalImage.getWidth();
        int resizeWidth = Math.min(originalWidth, targetWidth);
        
        if (originalWidth < targetWidth) {
            System.out.println("  ℹ Original width (" + originalWidth + "px) < target (" + targetWidth + "px), skipping upscale");
        }
        
        // Resize image with high quality
        BufferedImage resizedImage = Scalr.resize(
                originalImage,
                Scalr.Method.QUALITY,
                Scalr.Mode.FIT_TO_WIDTH,
                resizeWidth,
                Scalr.OP_ANTIALIAS
        );

        // FIX 2: Only apply sharpen for large images (not thumb/medium)
        if (targetWidth >= LARGE_WIDTH) {
            resizedImage = applySharpen(resizedImage);
            System.out.println("  ✓ Sharpen applied");
        }

        // Convert RGB if necessary
        BufferedImage rgbImage = new BufferedImage(
                resizedImage.getWidth(),
                resizedImage.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );
        Graphics2D g = rgbImage.createGraphics();
        g.drawImage(resizedImage, 0, 0, null);
        g.dispose();

        // Create size directory: uploads/products/{year}/{month}/{productSlug}/{size}/
        String sizeDirPath = absoluteBasePath + File.separator + sizeName;
        createDirectoryIfNotExists(sizeDirPath);
        
        // Step 1: Save as optimized JPEG first
        String jpgFileName = "hoa_temp.jpg";
        String jpgFilePath = sizeDirPath + File.separator + jpgFileName;
        File jpgFile = saveAsJPEGWithSizeLimit(rgbImage, jpgFilePath, sizeName);
        
        long jpgSize = jpgFile.length();
        System.out.println("  → JPG saved: " + formatFileSize(jpgSize));
        
        // Step 2: Convert JPEG to WebP (better compression)
        String webpFileName = "hoa.webp";
        String webpFilePath = sizeDirPath + File.separator + webpFileName;
        File webpFile = convertJPGtoWebP(jpgFilePath, webpFilePath);
        
        if (webpFile.exists()) {
            long webpSize = webpFile.length();
            long saved = jpgSize - webpSize;
            int percent = (int)((saved * 100.0) / jpgSize);
            System.out.println("  → WebP saved: " + formatFileSize(webpSize) + " (saved " + formatFileSize(saved) + " = " + percent + "%)");
            
            // Delete temp JPG file
            jpgFile.delete();
            
            System.out.println(sizeName + " complete: " + webpFilePath + " (" + formatFileSize(webpSize) + ")");
            return webpFileName;
        } else {
            // Fallback: keep JPG if WebP conversion failed
            System.out.println("  ⚠ WebP conversion failed, keeping JPG");
            File finalJpgFile = new File(sizeDirPath, "hoa.jpg");
            jpgFile.renameTo(finalJpgFile);
            System.out.println(sizeName + " saved: " + finalJpgFile.getAbsolutePath() + " (" + formatFileSize(jpgSize) + ")");
            return "hoa.jpg";
        }
    }
    
    /**
     * Convert JPEG to WebP using cwebp command line tool
     */
    private File convertJPGtoWebP(String jpgPath, String webpPath) {
        try {
            // Check if cwebp is available
            ProcessBuilder checkBuilder = new ProcessBuilder("which", "cwebp");
            Process checkProcess = checkBuilder.start();
            checkProcess.waitFor();
            
            if (checkProcess.exitValue() != 0) {
                System.out.println("  ⚠ cwebp not found, skipping WebP conversion");
                return new File(""); // Return empty file to trigger fallback
            }
            
            // Convert JPG to WebP with quality 80
            ProcessBuilder builder = new ProcessBuilder(
                "cwebp",
                "-q", "80",
                jpgPath,
                "-o", webpPath
            );
            builder.redirectErrorStream(true);
            
            Process process = builder.start();
            int exitCode = process.waitFor();
            
            if (exitCode == 0) {
                return new File(webpPath);
            } else {
                System.out.println("  ⚠ cwebp conversion failed with exit code: " + exitCode);
                return new File("");
            }
        } catch (Exception e) {
            System.out.println("  ⚠ WebP conversion error: " + e.getMessage());
            return new File("");
        }
    }

    /**
     * Apply light sharpen filter
     */
    private BufferedImage applySharpen(BufferedImage image) {
        float[] sharpenMatrix = {
                0.0f, -0.15f, 0.0f,
                -0.15f, 1.6f, -0.15f,
                0.0f, -0.15f, 0.0f
        };

        java.awt.image.Kernel kernel = new java.awt.image.Kernel(3, 3, sharpenMatrix);
        java.awt.image.ConvolveOp op = new java.awt.image.ConvolveOp(
                kernel,
                java.awt.image.ConvolveOp.EDGE_NO_OP,
                null
        );

        return op.filter(image, null);
    }

    /**
     * Save as JPEG with file size limits - re-encode if too large
     * Logic: resize → encode JPEG q=0.80 → if > target → q -= 0.05 → encode again → repeat until target or q_min
     */
    private File saveAsJPEGWithSizeLimit(BufferedImage image, String outputPath, String sizeName) throws IOException {
        // Define target file size limits based on image size
        long maxFileSize;
        switch (sizeName) {
            case "thumb":
                maxFileSize = 50 * 1024; // 50 KB
                break;
            case "medium":
                maxFileSize = 120 * 1024; // 120 KB
                break;
            case "large":
                maxFileSize = 250 * 1024; // 250 KB
                break;
            default:
                maxFileSize = 300 * 1024; // 300 KB fallback
        }
        
        File outputFile = new File(outputPath);
        float quality = 0.80f; // Always start with 0.80
        float qMin = 0.30f;    // Minimum quality
        int attempts = 0;
        int maxAttempts = 15;  // Allow more attempts with smaller steps
        
        // Encode JPEG, reduce quality if file is too large
        while (attempts < maxAttempts) {
            saveAsJPEG(image, outputFile, quality);
            
            if (outputFile.length() <= maxFileSize) {
                if (attempts > 0) {
                    System.out.println("  ✓ Re-encoded at quality " + Math.round(quality * 100) + "% to fit " + formatFileSize(maxFileSize));
                }
                break;
            }
            
            // File too large, reduce quality and try again
            attempts++;
            if (attempts < maxAttempts) {
                quality -= 0.05f; // Reduce by 5%
                if (quality < qMin) quality = qMin; // Don't go below minimum
                System.out.println("  ⚠ File size " + formatFileSize(outputFile.length()) + " > " + formatFileSize(maxFileSize) + ", re-encoding at " + Math.round(quality * 100) + "%...");
            }
        }
        
        return outputFile;
    }
    
    /**
     * Save image as JPEG with specified quality
     */
    private void saveAsJPEG(BufferedImage image, File outputFile, float quality) throws IOException {
        // Convert to RGB if needed (JPEG doesn't support alpha channel)
        BufferedImage rgbImage = image;
        if (image.getType() != BufferedImage.TYPE_INT_RGB) {
            rgbImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g = rgbImage.createGraphics();
            g.drawImage(image, 0, 0, null);
            g.dispose();
        }
        
        // Get JPEG writer
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
        ImageWriter writer = writers.next();
        ImageWriteParam writeParam = writer.getDefaultWriteParam();
        
        writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        writeParam.setCompressionQuality(quality);
        
        try (FileOutputStream fos = new FileOutputStream(outputFile);
             ImageOutputStream ios = ImageIO.createImageOutputStream(fos)) {
            writer.setOutput(ios);
            writer.write(null, new IIOImage(rgbImage, null, null), writeParam);
            writer.dispose();
        }
    }

    /**
     * Create directory if not exists
     */
    private void createDirectoryIfNotExists(String path) throws IOException {
        Path directory = Paths.get(path);
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
            System.out.println("Created directory: " + path);
        }
    }

    /**
     * Get file extension from filename
     */
    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "jpg";
        }
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }

    /**
     * Format file size for display
     */
    private String formatFileSize(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.1f KB", bytes / 1024.0);
        } else {
            return String.format("%.2f MB", bytes / (1024.0 * 1024.0));
        }
    }
    
    /**
     * Delete product images
     */
    public void deleteProductImages(String productSlug, String year, String month) {
        try {
            String relativePath = BASE_UPLOAD_DIR + "/" + year + "/" + month + "/" + productSlug;
            String absolutePath = projectRoot + File.separator + relativePath.replace("/", File.separator);
            
            Path directory = Paths.get(absolutePath);
            if (Files.exists(directory)) {
                Files.walk(directory)
                        .sorted(java.util.Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
                System.out.println("Deleted images for: " + productSlug);
            }
        } catch (IOException e) {
            // Log error but don't throw - deletion is not critical
            System.err.println("Error deleting images: " + e.getMessage());
        }
    }
}
