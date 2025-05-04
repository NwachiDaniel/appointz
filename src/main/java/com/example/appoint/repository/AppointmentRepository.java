package com.example.appoint.repository;

 

import com.example.appoint.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.time.LocalDate;
import java.util.List;
import com.example.appoint.model.User; // Ensure this is the correct package for the User class

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    
    Optional<Appointment> findByIdAndUserId(Long appointmentId, Long userId);
    List<Appointment> findByUser(User user);
    List<Appointment> findByUserAndDate(User user, LocalDate date);

          
}

 