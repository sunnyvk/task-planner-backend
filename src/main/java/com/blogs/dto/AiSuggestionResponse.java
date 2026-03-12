package com.blogs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiSuggestionResponse {
    private String recommendations;
    private int totalTasks;
    private String generatedAt;
}
