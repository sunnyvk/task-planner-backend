package com.blogs.service;
import com.blogs.customexception.UserNotFoundException;
import com.blogs.entities.Task.Status;
import com.blogs.repository.TaskRepository;
import com.blogs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AnalyticsService {
    
    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    // GET TASK STATISTICS
    public Map<String, Object> getTaskAnalytics(Long userId) {
        // Verify user exists
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with id: " + userId);
        }
        
        Map<String, Object> analytics = new HashMap<>();
        
        // Total tasks
        long totalTasks = taskRepository.countByUserId(userId);
        
        // Tasks by status
        long completedTasks = taskRepository.countByUserIdAndStatus(userId, Status.COMPLETED);
        long pendingTasks = taskRepository.countByUserIdAndStatus(userId, Status.PENDING);
        long inProgressTasks = taskRepository.countByUserIdAndStatus(userId, Status.IN_PROGRESS);
        
        analytics.put("totalTasks", totalTasks);
        analytics.put("completedTasks", completedTasks);
        analytics.put("pendingTasks", pendingTasks);
        analytics.put("inProgressTasks", inProgressTasks);
        
        // Completion percentage
        double completionRate = totalTasks > 0 ? (completedTasks * 100.0 / totalTasks) : 0;
        analytics.put("completionRate", Math.round(completionRate * 100.0) / 100.0);
        
        return analytics;
    }
}

