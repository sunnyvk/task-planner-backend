package com.blogs.service;
import com.blogs.customexception.TaskNotFoundException;
import com.blogs.customexception.UserNotFoundException;
import com.blogs.dto.TaskRequest;
import com.blogs.dto.TaskResponse;
import com.blogs.entities.Task;
import com.blogs.entities.Task.Priority;
import com.blogs.entities.Task.Status;
import com.blogs.repository.TaskRepository;
import com.blogs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {
    
    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    // CREATE TASK
    public TaskResponse createTask(Long userId, TaskRequest request) {
        // Verify user exists
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with id: " + userId);
        }
        
        // Create task
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority());
        task.setStatus(request.getStatus());
        task.setDueDate(request.getDueDate());
        task.setUserId(userId);
        
        Task savedTask = taskRepository.save(task);
        return new TaskResponse(savedTask);
    }
    
    // GET ALL TASKS FOR USER
    public List<TaskResponse> getAllTasks(Long userId) {
        // Verify user exists
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with id: " + userId);
        }
        
        List<Task> tasks = taskRepository.findByUserId(userId);
        return tasks.stream()
                   .map(TaskResponse::new)
                   .collect(Collectors.toList());
    }
    
    // GET TASK BY ID
    public TaskResponse getTaskById(Long userId, Long taskId) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));
        
        // Verify task belongs to user
        if (!task.getUserId().equals(userId)) {
            throw new TaskNotFoundException("Task not found for this user");
        }
        
        return new TaskResponse(task);
    }
    
    // UPDATE TASK
    public TaskResponse updateTask(Long userId, Long taskId, TaskRequest request) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));
        
        // Verify task belongs to user
        if (!task.getUserId().equals(userId)) {
            throw new TaskNotFoundException("Task not found for this user");
        }
        
        // Update fields
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority());
        task.setStatus(request.getStatus());
        task.setDueDate(request.getDueDate());
        
        Task updatedTask = taskRepository.save(task);
        return new TaskResponse(updatedTask);
    }
    
    // DELETE TASK
    public String deleteTask(Long userId, Long taskId) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));
        
        // Verify task belongs to user
        if (!task.getUserId().equals(userId)) {
            throw new TaskNotFoundException("Task not found for this user");
        }
        
        taskRepository.delete(task);
        return "Task deleted successfully";
    }
    
    // FILTER TASKS BY STATUS
    public List<TaskResponse> getTasksByStatus(Long userId, Status status) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with id: " + userId);
        }
        
        List<Task> tasks = taskRepository.findByUserIdAndStatus(userId, status);
        return tasks.stream()
                   .map(TaskResponse::new)
                   .collect(Collectors.toList());
    }
    
    // FILTER TASKS BY PRIORITY
    public List<TaskResponse> getTasksByPriority(Long userId, Priority priority) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with id: " + userId);
        }
        
        List<Task> tasks = taskRepository.findByUserIdAndPriority(userId, priority);
        return tasks.stream()
                   .map(TaskResponse::new)
                   .collect(Collectors.toList());
    }
}
