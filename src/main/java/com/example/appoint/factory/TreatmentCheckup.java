package com.example.appoint.factory;

public class TreatmentCheckup implements AppointmentType {
    @Override
    public String getType() {
        return "TREATMENT_CHECKUP";
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