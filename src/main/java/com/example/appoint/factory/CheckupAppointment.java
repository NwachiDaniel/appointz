package com.example.appoint.factory;

public class CheckupAppointment implements AppointmentType {
    @Override
    public String getType() {
        return "Treatment Checkup";
    }

    @Override
    public int getId() {
        return 1;
    }

    @Override
    public String getName() {
        return "Treatment Checkup";
    }

    
}
