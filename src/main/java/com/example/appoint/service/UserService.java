package com.example.appoint.service;
 
import com.example.appoint.model.User;
import com.example.appoint.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // Authenticate user by email and password
    public User authenticate(String email, String password) {
        //Optional<User> user = userRepository.findByEmail(email);
        //return user.filter(u -> u.getPassword().equals(password)).orElse(null);
        return userRepository.findByEmailAndPassword(email, password).orElse(null);
    }
     
    // Get user by email
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    // Get user by password
    public Optional<User> getUserByPassword(String password) {
        return userRepository.findByPassword(password);
    }
    //Get user by Username
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    public Optional<User> getUserByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }
    // Update user details (basic example)
    public User updateUser(User user) {
        return userRepository.save(user);
    }
    // Save user to the database
    public void save(User user) {
        userRepository.save(user);
    }
}
