package com.wuubangdev.portfolio.infrastructure.global.upload;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/uploads")
public class FileUploadController {

    @Value("${app.upload-dir:uploads}")
    private String uploadDir;

    @PostMapping("/images")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UploadResponse> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String originalName = StringUtils.cleanPath(file.getOriginalFilename() == null ? "image" : file.getOriginalFilename());
        String extension = "";
        int extensionIndex = originalName.lastIndexOf('.');
        if (extensionIndex >= 0) {
            extension = originalName.substring(extensionIndex).toLowerCase(Locale.ROOT);
        }

        Path targetDir = Paths.get(uploadDir, "images").toAbsolutePath().normalize();
        Files.createDirectories(targetDir);

        String fileName = UUID.randomUUID() + extension;
        Path targetPath = targetDir.resolve(fileName);
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        return ResponseEntity.ok(new UploadResponse(
                fileName,
                "/uploads/images/" + fileName,
                file.getContentType(),
                file.getSize()
        ));
    }

    public record UploadResponse(
            String fileName,
            String url,
            String contentType,
            long size
    ) {}
}
