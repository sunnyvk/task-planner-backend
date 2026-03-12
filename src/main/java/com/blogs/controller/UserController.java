package com.blogs.controller;

import com.blogs.dto.SigninRequest;
import com.blogs.dto.SignupRequest;
import com.blogs.dto.UserResponse;
import com.blogs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    // SIGNUP
    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@Valid @RequestBody SignupRequest request) {
        UserResponse response = userService.signup(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    // SIGNIN
    @PostMapping("/signin")
    public ResponseEntity<UserResponse> signin(@Valid @RequestBody SigninRequest request) {
        UserResponse response = userService.signin(request);
        return ResponseEntity.ok(response);
    }
    
    // FORGOT PASSWORD - Send OTP
    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestParam String email) {
        String message = userService.forgotPassword(email);
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        return ResponseEntity.ok(response);
    }
    
    // RESET PASSWORD - Verify OTP and reset
    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(
            @RequestParam String email,
            @RequestParam String otp,
            @RequestParam String newPassword) {
        String message = userService.resetPassword(email, otp, newPassword);
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        return ResponseEntity.ok(response);
    }
    
    // GET USER BY ID
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId) {
        UserResponse response = userService.getUserById(userId);
        return ResponseEntity.ok(response);
    }
}
