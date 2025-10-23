package com.example.privacyanalyzer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    @Autowired
    private DataSource dataSource;

    @GetMapping
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> health = new HashMap<>();
        
        try {
            // Check database connectivity
            try (Connection connection = dataSource.getConnection()) {
                boolean isValid = connection.isValid(5);
                health.put("database", isValid ? "UP" : "DOWN");
            }
            
            health.put("status", "UP");
            health.put("timestamp", System.currentTimeMillis());
            health.put("service", "Privacy Policy Analyzer");
            health.put("version", "1.0.0");
            
            return ResponseEntity.ok(health);
        } catch (SQLException e) {
            health.put("status", "DOWN");
            health.put("database", "DOWN");
            health.put("error", e.getMessage());
            health.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(503).body(health);
        }
    }
}