package com.blogs.controller;

import com.blogs.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = "http://localhost:3000")
public class AnalyticsController {
    
    @Autowired
    private AnalyticsService analyticsService;
    
    // GET TASK ANALYTICS
    @GetMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> getTaskAnalytics(@PathVariable Long userId) {
        Map<String, Object> analytics = analyticsService.getTaskAnalytics(userId);
        return ResponseEntity.ok(analytics);
    }
}

