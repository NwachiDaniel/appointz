package com.example.appoint.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "appointment_date")
    private LocalDate date;

    private LocalTime time;

    // Store the appointment type as a string from the factory
    private String type;
     
    private String reason;
    
       
    // Use Enum for status
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    // Transient field for form dropdown handling
    @Transient
    private int statusId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}

  