package com.example.appoint.observer;

 

 
import org.springframework.stereotype.Component;

@Component
public class AdminObserver implements AppointmentObserver {
    @Override
    public void update(String message) {
        System.out.println("ADMIN NOTIFICATION: " + message);
        // You can extend this: send email, log to DB, etc.
    }
}