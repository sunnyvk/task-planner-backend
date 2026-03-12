package com.blogs.service;




import com.blogs.entities.Task;
import com.blogs.entities.User;
import com.blogs.repository.TaskRepository;
import com.blogs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class NotificationService {
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    // Send email notification
    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom("noreply@taskplanner.com");
            
            mailSender.send(message);
            System.out.println("✅ Email sent successfully to: " + to);
            
        } catch (Exception e) {
            System.err.println("❌ Failed to send email to " + to + ": " + e.getMessage());
        }
    }
    
    // Scheduled job - runs every day at 9:00 AM
    @Scheduled(cron = "${notification.check.cron}")
    @Transactional
    public void sendTaskReminders() {
        System.out.println("🔔 Running scheduled task reminder check at: " + LocalDateTime.now());
        
        // Get all users
        List<User> users = userRepository.findAll();
        
        for (User user : users) {
            checkAndSendReminders(user);
        }
        
        System.out.println("✅ Task reminder check completed");
    }
    
    // Check tasks for a specific user and send reminders
    private void checkAndSendReminders(User user) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow = now.plusHours(24);
        
        // Find tasks due in next 24 hours
        List<Task> dueSoonTasks = taskRepository.findTasksDueSoon(
            user.getId(), 
            now, 
            tomorrow
        );
        
        if (dueSoonTasks.isEmpty()) {
            return; // No tasks due soon
        }
        
        // Build email content
        String emailBody = buildReminderEmail(user.getName(), dueSoonTasks);
        
        // Send email
        sendEmail(
            user.getEmail(),
            "⏰ Task Reminder: " + dueSoonTasks.size() + " task(s) due soon!",
            emailBody
        );
    }
    
    // Build formatted email body
    private String buildReminderEmail(String userName, List<Task> tasks) {
        StringBuilder email = new StringBuilder();
        
        email.append("Hi ").append(userName).append(",\n\n");
        email.append("This is a friendly reminder that you have ")
             .append(tasks.size())
             .append(" task(s) due within the next 24 hours:\n\n");
        
        int taskNumber = 1;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' hh:mm a");
        
        for (Task task : tasks) {
            email.append(taskNumber++).append(". ")
                 .append(task.getTitle())
                 .append("\n");
            
            email.append("   Priority: ").append(task.getPriority()).append("\n");
            email.append("   Status: ").append(task.getStatus()).append("\n");
            
            if (task.getDueDate() != null) {
                email.append("   Due: ").append(task.getDueDate().format(formatter)).append("\n");
            }
            
            if (task.getDescription() != null && !task.getDescription().isEmpty()) {
                email.append("   Description: ").append(task.getDescription()).append("\n");
            }
            
            email.append("\n");
        }
        
        email.append("---\n\n");
        email.append("Don't forget to mark tasks as completed once you finish them!\n\n");
        email.append("Best regards,\n");
        email.append("Task Planner Team\n\n");
        email.append("---\n");
        email.append("This is an automated reminder. Please do not reply to this email.");
        
        return email.toString();
    }
    
    // Manual method to send reminder for a specific user (for testing)
    public String sendManualReminder(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        checkAndSendReminders(user);
        return "Reminder check completed for user: " + user.getEmail();
    }
}
