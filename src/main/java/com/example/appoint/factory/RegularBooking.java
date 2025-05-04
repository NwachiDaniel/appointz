package com.example.appoint.factory;

 
public class RegularBooking implements AppointmentType {
    @Override
    public String getType() {
        return "REGULAR_BOOKING";
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
