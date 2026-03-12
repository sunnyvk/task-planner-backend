package com.blogs.controller;


import com.blogs.dto.AiSuggestionResponse;
import com.blogs.dto.AiTaskRequest;
import com.blogs.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
public class AiController {
    
    @Autowired
    private AiService aiService;
    
    // Get AI suggestions based on user's existing tasks
    @GetMapping("/suggestions/{userId}")
    public ResponseEntity<AiSuggestionResponse> getTaskSuggestions(@PathVariable Long userId) {
        AiSuggestionResponse response = aiService.generateTaskSuggestions(userId);
        return ResponseEntity.ok(response);
    }
    
    // Get AI suggestions based on custom task list
    @PostMapping("/suggestions/{userId}")
    public ResponseEntity<AiSuggestionResponse> getCustomSuggestions(
            @PathVariable Long userId,
            @RequestBody List<AiTaskRequest> tasks) {
        AiSuggestionResponse response = aiService.generateCustomSuggestions(userId, tasks);
        return ResponseEntity.ok(response);
    }
}
