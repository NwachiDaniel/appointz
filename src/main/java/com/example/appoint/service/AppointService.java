package com.example.appoint.service;
 

import com.example.appoint.observer.AppointmentObserver;
import com.example.appoint.observer.AppointmentSubject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class AppointService implements AppointmentSubject {

    private final List<AppointmentObserver> observers = new ArrayList<>();

    @Autowired
    public AppointService(List<AppointmentObserver> observerBeans) {
        this.observers.addAll(observerBeans); // Auto-wire all beans that implement AppointmentObserver
    }

    @Override
    public void attach(AppointmentObserver observer) {
        this.observers.add(observer);
    }
    
    @Override
    public void detach(AppointmentObserver observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObservers(String message) {
        observers.forEach(observer -> observer.update(message));
    }

    // Your actual appointment logic
    public void bookAppointment(String customerName) {
        // Booking logic here
        notifyObservers("Appointment booked for: " + customerName);
    }

    public void cancelAppointment(String customerName) {
        // Cancel logic here
        notifyObservers("Appointment canceled for: " + customerName);
    }
}
