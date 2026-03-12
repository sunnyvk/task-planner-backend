package com.blogs.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blogs.entities.Task;
import com.blogs.entities.Task.Priority;
import com.blogs.entities.Task.Status;

public interface TaskRepository extends JpaRepository<Task, Long> {
	// Find all tasks for a specific user
    List<Task> findByUserId(Long userId);
    
    // Find tasks by user and status
    List<Task> findByUserIdAndStatus(Long userId, Status status);
    
    // Find tasks by user and priority
    List<Task> findByUserIdAndPriority(Long userId, Priority priority);
    
    // Find tasks due within 24 hours (for email notifications)
    @Query("SELECT t FROM Task t WHERE t.userId = :userId AND t.dueDate BETWEEN :now AND :tomorrow AND t.status != 'COMPLETED'")
    List<Task> findTasksDueSoon(@Param("userId") Long userId, 
                                 @Param("now") LocalDateTime now, 
                                 @Param("tomorrow") LocalDateTime tomorrow);
    
    // Count tasks by status for analytics
    long countByUserIdAndStatus(Long userId, Status status);
    
    // Count total tasks for user
    long countByUserId(Long userId);
}
