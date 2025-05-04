package com.example.appoint.factory;

public class BookingAppointment implements AppointmentType {
    @Override
    public String getType() {
        return "Regular Booking";
    }

    @Override
    public int getId() {
        return 2;
    }

    @Override
    public String getName() {
        return "Regular Booking";
    }
    
}
