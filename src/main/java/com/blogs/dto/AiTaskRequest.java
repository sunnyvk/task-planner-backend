package com.blogs.dto;


import java.time.LocalDateTime;

import com.blogs.entities.Task.Priority;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiTaskRequest {
    private String title;
    private String description;
    private Priority priority;
    private LocalDateTime dueDate;
}