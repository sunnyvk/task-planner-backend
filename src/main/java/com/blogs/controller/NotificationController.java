package com.blogs.controller;



import com.blogs.dto.EmailRequest;
import com.blogs.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    
    @Autowired
    private NotificationService notificationService;
    
    // Send test email
    @PostMapping("/send-test")
    public ResponseEntity<Map<String, String>> sendTestEmail(@RequestBody EmailRequest request) {
        notificationService.sendEmail(request.getTo(), request.getSubject(), request.getBody());
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Test email sent to: " + request.getTo());
        return ResponseEntity.ok(response);
    }
    
    // Trigger manual reminder check for a user
    @PostMapping("/reminder/{userId}")
    public ResponseEntity<Map<String, String>> sendManualReminder(@PathVariable Long userId) {
        String message = notificationService.sendManualReminder(userId);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        return ResponseEntity.ok(response);
    }
}

