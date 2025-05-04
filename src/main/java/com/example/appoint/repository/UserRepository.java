package com.example.appoint.repository;

 

import com.example.appoint.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByPassword(String password);
    Optional<User> findByEmailAndPassword(String email, String password);
}

