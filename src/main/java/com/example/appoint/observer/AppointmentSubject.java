package com.example.appoint.observer;





public interface AppointmentSubject {
    void attach(AppointmentObserver observer);
    void detach(AppointmentObserver observer);
    void notifyObservers(String message);
}
