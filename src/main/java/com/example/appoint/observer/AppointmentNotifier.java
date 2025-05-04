package com.example.appoint.observer;

 
import java.util.ArrayList;
import java.util.List;


public class AppointmentNotifier {
    private final List<AppointmentObserver> observers = new ArrayList<>();

    public void addObserver(AppointmentObserver observer) {
        observers.add(observer);
    }

    public void notifyObservers(String message) {
        for (AppointmentObserver observer : observers) {
            observer.update(message);
        }
    }
}



