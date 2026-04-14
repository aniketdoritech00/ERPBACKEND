package com.doritech.PdfService.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.doritech.PdfService.Entity.CCTVBranchDocument;
import com.doritech.PdfService.Entity.FASBranchDocument;

@Service
public class ImageStorageService {

    private static final Logger logger = LoggerFactory.getLogger(ImageStorageService.class);

    @Value("${app.image.base-path}")
    private String basePath;

    private static final Map<String, String> MIME_TYPES = new HashMap<>();

    static {
        MIME_TYPES.put("jpg", "image/jpeg");
        MIME_TYPES.put("jpeg", "image/jpeg");
        MIME_TYPES.put("png", "image/png");
        MIME_TYPES.put("gif", "image/gif");
        MIME_TYPES.put("webp", "image/webp");
    }

    public String saveImage(MultipartFile file, String docType, Long branchId) {
        try {
            Path dirPath = Paths.get(basePath, "cctvImages", String.valueOf(branchId));
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }
            String originalFilename = file.getOriginalFilename();
            String extension = (originalFilename != null && originalFilename.contains("."))
                    ? originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase()
                    : ".jpg";
            String filename = docType + "_" + System.currentTimeMillis() + extension;
            Path filePath = dirPath.resolve(filename);
            Files.write(filePath, file.getBytes());
            return "cctvImages/" + branchId + "/" + filename;
        } catch (IOException e) {
            logger.error("Failed to save image - DocType: {}, BranchId: {}, Error: {}",
                    docType, branchId, e.getMessage(), e);
            throw new RuntimeException("Image save failed: " + e.getMessage(), e);
        }
    }

    public String saveFASImage(MultipartFile file, String docType, Long branchId) {
        try {
            Path dirPath = Paths.get(basePath, "fasImages", String.valueOf(branchId));
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }
            String originalFilename = file.getOriginalFilename();
            String extension = (originalFilename != null && originalFilename.contains("."))
                    ? originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase()
                    : ".jpg";
            String filename = docType + "_" + System.currentTimeMillis() + extension;
            Path filePath = dirPath.resolve(filename);
            Files.write(filePath, file.getBytes());
            return "fasImages/" + branchId + "/" + filename;
        } catch (IOException e) {
            logger.error("Failed to save FAS image - DocType: {}, BranchId: {}, Error: {}",
                    docType, branchId, e.getMessage(), e);
            throw new RuntimeException("FAS Image save failed: " + e.getMessage(), e);
        }
    }

    public String readImageAsBase64(String relativePath) {
        if (relativePath == null || relativePath.trim().isEmpty()) {
            logger.warn("readImageAsBase64 called with null/empty path");
            return null;
        }
        try {
            Path filePath = Paths.get(basePath, relativePath);
            if (!Files.exists(filePath)) {
                logger.warn("Image not found at path: {}", filePath);
                return null;
            }
            if (!Files.isReadable(filePath)) {
                logger.warn("Image not readable at path: {}", filePath);
                return null;
            }
            long fileSizeBytes = Files.size(filePath);
            long maxSizeBytes = 10 * 1024 * 1024;
            if (fileSizeBytes > maxSizeBytes) {
                logger.warn("Image too large ({} bytes) at path: {}", fileSizeBytes, filePath);
                return null;
            }
            byte[] imageBytes = Files.readAllBytes(filePath);
            if (imageBytes.length == 0) {
                logger.warn("Image file is empty at path: {}", filePath);
                return null;
            }
            String extension = relativePath.contains(".")
                    ? relativePath.substring(relativePath.lastIndexOf(".") + 1).toLowerCase()
                    : "jpeg";
            String mimeType = MIME_TYPES.getOrDefault(extension, "image/jpeg");
            return "data:" + mimeType + ";base64," + Base64.getEncoder().encodeToString(imageBytes);
        } catch (OutOfMemoryError e) {
            logger.error("OutOfMemory while reading image at path: {}", relativePath);
            return null;
        } catch (SecurityException e) {
            logger.error("Security exception reading image at path: {}, Error: {}", relativePath, e.getMessage());
            return null;
        } catch (IOException e) {
            logger.error("IO error reading image at path: {}, Error: {}", relativePath, e.getMessage());
            return null;
        } catch (Exception e) {
            logger.error("Unexpected error reading image at path: {}, Error: {}", relativePath, e.getMessage());
            return null;
        }
    }

    public Map<Long, String> readImagesAsBase64Parallel(List<CCTVBranchDocument> documents) {
        if (documents == null || documents.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<Long, String> result = new ConcurrentHashMap<>();
        List<CompletableFuture<Void>> futures = documents.stream()
                .filter(doc -> doc != null && doc.getDocumentPath() != null)
                .map(doc -> CompletableFuture.runAsync(() -> {
                    try {
                        String base64 = readImageAsBase64(doc.getDocumentPath());
                        if (base64 != null) {
                            result.put(doc.getDocumentId(), base64);
                        }
                    } catch (Exception e) {
                        logger.error("Parallel read failed for documentId: {}, Error: {}",
                                doc.getDocumentId(), e.getMessage());
                    }
                }))
                .collect(Collectors.toList());
        try {
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                    .get(30, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            logger.error("Timeout while reading images in parallel");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Interrupted while reading images in parallel");
        } catch (ExecutionException e) {
            logger.error("Execution error in parallel image read: {}", e.getMessage());
        }
        return result;
    }

    public Map<Long, String> readFASImagesAsBase64Parallel(List<FASBranchDocument> documents) {
        if (documents == null || documents.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<Long, String> result = new ConcurrentHashMap<>();
        List<CompletableFuture<Void>> futures = documents.stream()
                .filter(doc -> doc != null && doc.getDocumentPath() != null)
                .map(doc -> CompletableFuture.runAsync(() -> {
                    try {
                        String base64 = readImageAsBase64(doc.getDocumentPath());
                        if (base64 != null) {
                            result.put(doc.getDocumentId(), base64);
                        }
                    } catch (Exception e) {
                        logger.error("Parallel read failed for FAS documentId: {}, Error: {}",
                                doc.getDocumentId(), e.getMessage());
                    }
                }))
                .collect(Collectors.toList());
        try {
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                    .get(30, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            logger.error("Timeout while reading FAS images in parallel");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Interrupted while reading FAS images in parallel");
        } catch (ExecutionException e) {
            logger.error("Execution error in FAS parallel image read: {}", e.getMessage());
        }
        return result;
    }

    public void deleteImage(String relativePath) {
        try {
            Path filePath = Paths.get(basePath, relativePath);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
        } catch (IOException e) {
            logger.error("Failed to delete image: {}", e.getMessage(), e);
        }
    }

    public String getFullPath(String relativePath) {
        return Paths.get(basePath, relativePath).toString();
    }
}