package com.blogs.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blogs.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
	 // Find user by email
    Optional<User> findByEmail(String email);
    
    // Check if email exists
    boolean existsByEmail(String email);
}
