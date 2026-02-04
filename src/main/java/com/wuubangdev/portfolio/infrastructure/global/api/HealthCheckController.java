package com.wuubangdev.portfolio.infrastructure.global.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/health")
@Tag(name = "Health Check", description = "Kiểm tra trạng thái hoạt động của hệ thống")
public class HealthCheckController {

    @GetMapping("/ping")
    @Operation(summary = "Ping API", description = "Kiểm tra kết nối và tính sẵn sàng của server")
    public ResponseEntity<Map<String, Object>> ping() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "UP");
        response.put("message", "Pong!");
        response.put("timestamp", LocalDateTime.now());
        response.put("version", "0.0.1-SNAPSHOT");

        return ResponseEntity.ok(response);
    }
}