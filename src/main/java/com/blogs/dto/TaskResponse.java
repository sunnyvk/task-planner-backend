package com.blogs.dto;

import java.time.LocalDateTime;

import com.blogs.entities.Task;
import com.blogs.entities.Task.Priority;
import com.blogs.entities.Task.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private Priority priority;
    private Status status;
    private LocalDateTime dueDate;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructor to convert Task entity to TaskResponse DTO
    public TaskResponse(Task task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.priority = task.getPriority();
        this.status = task.getStatus();
        this.dueDate = task.getDueDate();
        this.userId = task.getUserId();
        this.createdAt = task.getCreatedAt();
        this.updatedAt = task.getUpdatedAt();
    }
}
