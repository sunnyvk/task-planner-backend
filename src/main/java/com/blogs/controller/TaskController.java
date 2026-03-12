package com.blogs.controller;

import com.blogs.dto.TaskRequest;
import com.blogs.dto.TaskResponse;
import com.blogs.entities.Task.Priority;
import com.blogs.entities.Task.Status;
import com.blogs.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "http://localhost:3000")
public class TaskController {
    
    @Autowired
    private TaskService taskService;
    
    // CREATE TASK
    @PostMapping("/{userId}")
    public ResponseEntity<TaskResponse> createTask(
            @PathVariable Long userId,
            @Valid @RequestBody TaskRequest request) {
        TaskResponse response = taskService.createTask(userId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    // GET ALL TASKS FOR USER
    @GetMapping("/{userId}")
    public ResponseEntity<List<TaskResponse>> getAllTasks(@PathVariable Long userId) {
        List<TaskResponse> tasks = taskService.getAllTasks(userId);
        return ResponseEntity.ok(tasks);
    }
    
    // GET TASK BY ID
    @GetMapping("/{userId}/{taskId}")
    public ResponseEntity<TaskResponse> getTaskById(
            @PathVariable Long userId,
            @PathVariable Long taskId) {
        TaskResponse response = taskService.getTaskById(userId, taskId);
        return ResponseEntity.ok(response);
    }
    
    // UPDATE TASK
    @PutMapping("/{userId}/{taskId}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long userId,
            @PathVariable Long taskId,
            @Valid @RequestBody TaskRequest request) {
        TaskResponse response = taskService.updateTask(userId, taskId, request);
        return ResponseEntity.ok(response);
    }
    
    // DELETE TASK
    @DeleteMapping("/{userId}/{taskId}")
    public ResponseEntity<Map<String, String>> deleteTask(
            @PathVariable Long userId,
            @PathVariable Long taskId) {
        String message = taskService.deleteTask(userId, taskId);
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        return ResponseEntity.ok(response);
    }
    
    // FILTER BY STATUS
    @GetMapping("/{userId}/status/{status}")
    public ResponseEntity<List<TaskResponse>> getTasksByStatus(
            @PathVariable Long userId,
            @PathVariable Status status) {
        List<TaskResponse> tasks = taskService.getTasksByStatus(userId, status);
        return ResponseEntity.ok(tasks);
    }
    
    // FILTER BY PRIORITY
    @GetMapping("/{userId}/priority/{priority}")
    public ResponseEntity<List<TaskResponse>> getTasksByPriority(
            @PathVariable Long userId,
            @PathVariable Priority priority) {
        List<TaskResponse> tasks = taskService.getTasksByPriority(userId, priority);
        return ResponseEntity.ok(tasks);
    }
}
