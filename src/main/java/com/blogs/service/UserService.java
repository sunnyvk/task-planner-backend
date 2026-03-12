//package com.blogs.service;
//
//import com.blogs.customexception.EmailAlreadyExistsException;
//import com.blogs.customexception.InvalidCredentialsException;
//import com.blogs.customexception.UserNotFoundException;
//import com.blogs.dto.SigninRequest;
//import com.blogs.dto.SignupRequest;
//import com.blogs.dto.UserResponse;
//import com.blogs.entities.User;
//import com.blogs.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Random;
//
//@Service
//public class UserService {
//    
//    @Autowired
//    private UserRepository userRepository;
//    
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//    
//    // In-memory OTP storage (userId -> OTP)
//    // In production, use Redis or database
//    private Map<String, String> otpStorage = new HashMap<>();
//    private Map<String, Long> otpExpiry = new HashMap<>();
//    
//    // SIGNUP
//    public UserResponse signup(SignupRequest request) {
//        // Check if email already exists
//        if (userRepository.existsByEmail(request.getEmail())) {
//            throw new EmailAlreadyExistsException("Email already registered: " + request.getEmail());
//        }
//        
//        // Create new user
//        User user = new User();
//        user.setName(request.getName());
//        user.setEmail(request.getEmail());
//        user.setPassword(passwordEncoder.encode(request.getPassword())); // Hash password
//        
//        User savedUser = userRepository.save(user);
//        
//        // Return response without password
//        return new UserResponse(
//            savedUser.getId(),
//            savedUser.getName(),
//            savedUser.getEmail(),
//            savedUser.getCreatedAt()
//        );
//    }
//    
//    // SIGNIN
//    public UserResponse signin(SigninRequest request) {
//        // Find user by email
//        User user = userRepository.findByEmail(request.getEmail())
//            .orElseThrow(() -> new UserNotFoundException("User not found with email: " + request.getEmail()));
//        
//        // Verify password
//        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//            throw new InvalidCredentialsException("Invalid password");
//        }
//        
//        // Return user info
//        return new UserResponse(
//            user.getId(),
//            user.getName(),
//            user.getEmail(),
//            user.getCreatedAt()
//        );
//    }
//    
//    // FORGOT PASSWORD - Send OTP
//    public String forgotPassword(String email) {
//        // Check if user exists
//        User user = userRepository.findByEmail(email)
//            .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
//        
//        // Generate 6-digit OTP
//        String otp = String.format("%06d", new Random().nextInt(999999));
//        
//        // Store OTP with 10 minutes expiry
//        otpStorage.put(email, otp);
//        otpExpiry.put(email, System.currentTimeMillis() + 10 * 60 * 1000); // 10 minutes
//        
//        // In real app, send OTP via email here
//        System.out.println("OTP for " + email + " is: " + otp);
//        
//        return "OTP sent to email: " + email;
//    }
//    
//    // RESET PASSWORD - Verify OTP and reset
//    public String resetPassword(String email, String otp, String newPassword) {
//        // Check if user exists
//        User user = userRepository.findByEmail(email)
//            .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
//        
//        // Verify OTP exists
//        if (!otpStorage.containsKey(email)) {
//            throw new InvalidCredentialsException("OTP not found. Please request a new OTP.");
//        }
//        
//        // Check OTP expiry
//        if (System.currentTimeMillis() > otpExpiry.get(email)) {
//            otpStorage.remove(email);
//            otpExpiry.remove(email);
//            throw new InvalidCredentialsException("OTP expired. Please request a new OTP.");
//        }
//        
//        // Verify OTP
//        if (!otpStorage.get(email).equals(otp)) {
//            throw new InvalidCredentialsException("Invalid OTP");
//        }
//        
//        // Reset password
//        user.setPassword(passwordEncoder.encode(newPassword));
//        userRepository.save(user);
//        
//        // Clear OTP
//        otpStorage.remove(email);
//        otpExpiry.remove(email);
//        
//        return "Password reset successful";
//    }
//    
//    // GET USER BY ID
//    public UserResponse getUserById(Long userId) {
//        User user = userRepository.findById(userId)
//            .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
//        
//        return new UserResponse(
//            user.getId(),
//            user.getName(),
//            user.getEmail(),
//            user.getCreatedAt()
//        );
//    }
//}


package com.blogs.service;

import com.blogs.config.JwtUtil;
import com.blogs.customexception.EmailAlreadyExistsException;
import com.blogs.customexception.InvalidCredentialsException;
import com.blogs.customexception.UserNotFoundException;
import com.blogs.dto.SigninRequest;
import com.blogs.dto.SignupRequest;
import com.blogs.dto.UserResponse;
import com.blogs.entities.User;
import com.blogs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil; // ADD THIS
    
    private Map<String, String> otpStorage = new HashMap<>();
    private Map<String, Long> otpExpiry = new HashMap<>();
    
    // SIGNUP - Generate JWT token
    public UserResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already registered: " + request.getEmail());
        }
        
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        
        User savedUser = userRepository.save(user);
        
        // Generate JWT token
        String token = jwtUtil.generateToken(savedUser.getEmail(), savedUser.getId());
        
        return new UserResponse(
            savedUser.getId(),
            savedUser.getName(),
            savedUser.getEmail(),
            savedUser.getCreatedAt(),
            token // Include token in response
        );
    }
    
    // SIGNIN - Generate JWT token
    public UserResponse signin(SigninRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new UserNotFoundException("User not found with email: " + request.getEmail()));
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid password");
        }
        
        // Generate JWT token
        String token = jwtUtil.generateToken(user.getEmail(), user.getId());
        
        return new UserResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getCreatedAt(),
            token // Include token in response
        );
    }
    
    // Rest of the methods remain the same...
    public String forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        
        String otp = String.format("%06d", new Random().nextInt(999999));
        otpStorage.put(email, otp);
        otpExpiry.put(email, System.currentTimeMillis() + 10 * 60 * 1000);
        
        System.out.println("OTP for " + email + " is: " + otp);
        return "OTP sent to email: " + email;
    }
    
    public String resetPassword(String email, String otp, String newPassword) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        
        if (!otpStorage.containsKey(email)) {
            throw new InvalidCredentialsException("OTP not found. Please request a new OTP.");
        }
        
        if (System.currentTimeMillis() > otpExpiry.get(email)) {
            otpStorage.remove(email);
            otpExpiry.remove(email);
            throw new InvalidCredentialsException("OTP expired. Please request a new OTP.");
        }
        
        if (!otpStorage.get(email).equals(otp)) {
            throw new InvalidCredentialsException("Invalid OTP");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        
        otpStorage.remove(email);
        otpExpiry.remove(email);
        
        return "Password reset successful";
    }
    
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        
        return new UserResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getCreatedAt()
        );
    }
}

