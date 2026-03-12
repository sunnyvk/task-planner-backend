package com.blogs.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.blogs.entities.Task.Priority;
import com.blogs.entities.Task.Status;

import lombok.Data;

@Data
public class TaskRequest {
    
    @NotBlank(message = "Title is required")
    private String title;
    
    private String description;
    
    @NotNull(message = "Priority is required")
    private Priority priority;
    
    @NotNull(message = "Status is required")
    private Status status;
    
    private LocalDateTime dueDate;
}