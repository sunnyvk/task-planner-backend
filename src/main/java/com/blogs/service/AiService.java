package com.blogs.service;


import com.blogs.customexception.UserNotFoundException;
import com.blogs.dto.AiSuggestionResponse;
import com.blogs.dto.AiTaskRequest;
import com.blogs.entities.Task;
import com.blogs.repository.TaskRepository;
import com.blogs.repository.UserRepository;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AiService {
    
    @Autowired
    private OpenAiService openAiService;
    
    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Value("${openai.model}")
    private String model;
    
    // Generate AI suggestions based on user's tasks
//    public AiSuggestionResponse generateTaskSuggestions(Long userId) {
        // Verify user exists
//        if (!userRepository.existsById(userId)) {
//            throw new UserNotFoundException("User not found with id: " + userId);
//        }
//        
//        // Get all pending and in-progress tasks for the user
//        List<Task> tasks = taskRepository.findByUserId(userId)
//                .stream()
//                .filter(task -> task.getStatus() != Task.Status.COMPLETED)
//                .collect(Collectors.toList());
//        
//        if (tasks.isEmpty()) {
//            return new AiSuggestionResponse(
//                "Great job! You have no pending tasks. Consider adding new goals to stay productive!",
//                0,
//                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
//            );
//        }
        
        // Build prompt for OpenAI
//        String prompt = buildPrompt(tasks);
        
        // Create chat completion request
//        List<ChatMessage> messages = new ArrayList<>();
//        messages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(), 
//            "You are an intelligent task management assistant. Analyze tasks and provide actionable scheduling recommendations."));
//        messages.add(new ChatMessage(ChatMessageRole.USER.value(), prompt));
//        
//        ChatCompletionRequest chatRequest = ChatCompletionRequest.builder()
//                .model(model)
//                .messages(messages)
//                .temperature(0.7)
//                .maxTokens(500)
//                .build();
        
//        try {
//            // Call OpenAI API
//            ChatCompletionResult result = openAiService.createChatCompletion(chatRequest);
//            String aiResponse = result.getChoices().get(0).getMessage().getContent();
//            
//            return new AiSuggestionResponse(
//                aiResponse,
//                tasks.size(),
//                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
//            );
//            
//        } catch (Exception e) {
//            // Fallback response if API fails
//            return new AiSuggestionResponse(
//                "Unable to generate AI suggestions at this time. Please try again later. Error: " + e.getMessage(),
//                tasks.size(),
//                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
//            );
//        }
//    }
//    
        
        public AiSuggestionResponse generateTaskSuggestions(Long userId) {
            // Verify user exists
            if (!userRepository.existsById(userId)) {
                throw new UserNotFoundException("User not found with id: " + userId);
            }
            
            // Get all pending and in-progress tasks for the user
            List<Task> tasks = taskRepository.findByUserId(userId)
                    .stream()
                    .filter(task -> task.getStatus() != Task.Status.COMPLETED)
                    .collect(Collectors.toList());
            
            if (tasks.isEmpty()) {
                return new AiSuggestionResponse(
                    "Great job! You have no pending tasks. Consider adding new goals to stay productive!",
                    0,
                    LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                );
            }
            
            // MOCK AI RESPONSE - No OpenAI API call needed!
            String mockAiResponse = generateMockAiSuggestions(tasks);
            
            return new AiSuggestionResponse(
                mockAiResponse,
                tasks.size(),
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            );
        }
     // Generate realistic AI-like suggestions without calling OpenAI
        private String generateMockAiSuggestions(List<Task> tasks) {
            StringBuilder suggestions = new StringBuilder();
            suggestions.append("🎯 **Smart Task Schedule & Recommendations**\n\n");
            
            // Sort tasks by priority and due date
            List<Task> highPriorityTasks = tasks.stream()
                .filter(t -> t.getPriority() == Task.Priority.HIGH)
                .sorted((a, b) -> {
                    if (a.getDueDate() == null) return 1;
                    if (b.getDueDate() == null) return -1;
                    return a.getDueDate().compareTo(b.getDueDate());
                })
                .collect(Collectors.toList());
            
            List<Task> mediumPriorityTasks = tasks.stream()
                .filter(t -> t.getPriority() == Task.Priority.MEDIUM)
                .collect(Collectors.toList());
            
            List<Task> lowPriorityTasks = tasks.stream()
                .filter(t -> t.getPriority() == Task.Priority.LOW)
                .collect(Collectors.toList());
            
            // High priority recommendations
            if (!highPriorityTasks.isEmpty()) {
                suggestions.append("🔴 **HIGH PRIORITY - Tackle These First:**\n\n");
                int count = 1;
                for (Task task : highPriorityTasks) {
                    suggestions.append(count++).append(". **").append(task.getTitle()).append("**\n");
                    if (task.getDueDate() != null) {
                        LocalDateTime dueDate = task.getDueDate();
                        long hoursUntilDue = java.time.Duration.between(LocalDateTime.now(), dueDate).toHours();
                        
                        if (hoursUntilDue < 24) {
                            suggestions.append("   ⚠️ URGENT! Due in ").append(hoursUntilDue).append(" hours\n");
                            suggestions.append("   💡 Suggestion: Complete this TODAY before anything else\n");
                        } else {
                            suggestions.append("   📅 Due: ").append(dueDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm"))).append("\n");
                            suggestions.append("   💡 Suggestion: Schedule 2-3 hour focused block for this\n");
                        }
                    }
                    if (task.getDescription() != null && !task.getDescription().isEmpty()) {
                        suggestions.append("   📝 Note: ").append(task.getDescription()).append("\n");
                    }
                    suggestions.append("\n");
                }
            }
            
            // Medium priority recommendations
            if (!mediumPriorityTasks.isEmpty()) {
                suggestions.append("🟡 **MEDIUM PRIORITY - Schedule These Soon:**\n\n");
                for (Task task : mediumPriorityTasks) {
                    suggestions.append("• ").append(task.getTitle());
                    if (task.getDueDate() != null) {
                        suggestions.append(" (Due: ").append(task.getDueDate().format(DateTimeFormatter.ofPattern("MMM dd"))).append(")");
                    }
                    suggestions.append("\n");
                }
                suggestions.append("💡 Tip: Allocate afternoon slots for these after completing high-priority tasks\n\n");
            }
            
            // Low priority recommendations
            if (!lowPriorityTasks.isEmpty()) {
                suggestions.append("🟢 **LOW PRIORITY - Fill Time:**\n\n");
                for (Task task : lowPriorityTasks) {
                    suggestions.append("• ").append(task.getTitle()).append("\n");
                }
                suggestions.append("💡 Tip: Perfect for when you have extra time or need a mental break\n\n");
            }
            
            // General productivity tips
            suggestions.append("---\n\n");
            suggestions.append("📊 **Overall Strategy:**\n");
            suggestions.append("1. Start your day with the most urgent HIGH priority task\n");
            suggestions.append("2. Take 5-min breaks between tasks to maintain focus\n");
            suggestions.append("3. Group similar tasks together for efficiency\n");
            suggestions.append("4. Review and update task status as you progress\n\n");
            
            suggestions.append("✨ **Pro Tip:** Break down large tasks into smaller 30-minute chunks for better momentum!");
            
            return suggestions.toString();
        }
     
    // Generate suggestions from custom task list (for frontend flexibility)
    public AiSuggestionResponse generateCustomSuggestions(Long userId, List<AiTaskRequest> taskRequests) {
        // Verify user exists
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with id: " + userId);
        }
        
        if (taskRequests == null || taskRequests.isEmpty()) {
            return new AiSuggestionResponse(
                "No tasks provided for analysis.",
                0,
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            );
        }
        
        // Build prompt from custom tasks
        String prompt = buildPromptFromRequests(taskRequests);
        
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(), 
            "You are an intelligent task management assistant. Analyze tasks and provide actionable scheduling recommendations."));
        messages.add(new ChatMessage(ChatMessageRole.USER.value(), prompt));
        
        ChatCompletionRequest chatRequest = ChatCompletionRequest.builder()
                .model(model)
                .messages(messages)
                .temperature(0.7)
                .maxTokens(500)
                .build();
        
        try {
            ChatCompletionResult result = openAiService.createChatCompletion(chatRequest);
            String aiResponse = result.getChoices().get(0).getMessage().getContent();
            
            return new AiSuggestionResponse(
                aiResponse,
                taskRequests.size(),
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            );
            
        } catch (Exception e) {
            return new AiSuggestionResponse(
                "Unable to generate AI suggestions. Error: " + e.getMessage(),
                taskRequests.size(),
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            );
        }
    }
    
    // Build prompt from database tasks
    private String buildPrompt(List<Task> tasks) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("I have the following tasks to complete:\n\n");
        
        int taskNumber = 1;
        for (Task task : tasks) {
            prompt.append(taskNumber++).append(". ");
            prompt.append("Title: ").append(task.getTitle()).append("\n");
            
            if (task.getDescription() != null && !task.getDescription().isEmpty()) {
                prompt.append("   Description: ").append(task.getDescription()).append("\n");
            }
            
            prompt.append("   Priority: ").append(task.getPriority()).append("\n");
            prompt.append("   Status: ").append(task.getStatus()).append("\n");
            
            if (task.getDueDate() != null) {
                prompt.append("   Due Date: ")
                      .append(task.getDueDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                      .append("\n");
            }
            
            prompt.append("\n");
        }
        
        prompt.append("Based on these tasks, their priorities, and due dates, ");
        prompt.append("please provide a smart schedule and recommendations on:\n");
        prompt.append("1. Which tasks to tackle first\n");
        prompt.append("2. Optimal time slots for each task\n");
        prompt.append("3. Any tasks that need urgent attention\n");
        prompt.append("4. Suggestions for breaking down complex tasks\n");
        prompt.append("\nProvide concise, actionable advice in a friendly tone.");
        
        return prompt.toString();
    }
    
    // Build prompt from custom task requests
    private String buildPromptFromRequests(List<AiTaskRequest> taskRequests) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("I have the following tasks to complete:\n\n");
        
        int taskNumber = 1;
        for (AiTaskRequest task : taskRequests) {
            prompt.append(taskNumber++).append(". ");
            prompt.append("Title: ").append(task.getTitle()).append("\n");
            
            if (task.getDescription() != null && !task.getDescription().isEmpty()) {
                prompt.append("   Description: ").append(task.getDescription()).append("\n");
            }
            
            if (task.getPriority() != null) {
                prompt.append("   Priority: ").append(task.getPriority()).append("\n");
            }
            
            if (task.getDueDate() != null) {
                prompt.append("   Due Date: ")
                      .append(task.getDueDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                      .append("\n");
            }
            
            prompt.append("\n");
        }
        
        prompt.append("Based on these tasks, their priorities, and due dates, ");
        prompt.append("please provide a smart schedule and recommendations on:\n");
        prompt.append("1. Which tasks to tackle first\n");
        prompt.append("2. Optimal time slots for each task\n");
        prompt.append("3. Any tasks that need urgent attention\n");
        prompt.append("4. Suggestions for breaking down complex tasks\n");
        prompt.append("\nProvide concise, actionable advice in a friendly tone.");
        
        return prompt.toString();
    }
}
