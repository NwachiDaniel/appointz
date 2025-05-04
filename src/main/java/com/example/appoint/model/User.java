package com.example.appoint.model;
 
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "app_user") // custom name to avoid keyword
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String email;
    public String username;
    private String password;
    private String role;
}