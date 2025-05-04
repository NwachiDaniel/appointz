package com.example.appoint.factory;
//import java.util.Arrays;
//import java.util.List;

// === Factory Utility ===
/*
public class AppointmentTypeFactory {
    public static AppointmentType getAppointmentTypeById(int id) {
        return switch (id) {
            case 1 -> new CheckupAppointment();
            case 2 -> new BookingAppointment();
            default -> throw new IllegalArgumentException("Invalid Appointment Type ID");
        };
    }
    public static AppointmentType getAppointmentTypeByName(String typeName) {
        return switch (typeName.toLowerCase()) {
            case "regular booking" -> new BookingAppointment();
            case "treatment checkup" -> new CheckupAppointment();
            default -> throw new IllegalArgumentException("Unknown appointment type: " + typeName);
        };
    }

    public static List<AppointmentType> getAllTypes() {
        return Arrays.asList(new CheckupAppointment(), new BookingAppointment());
    }
}*/
 
 

import java.util.HashMap;
import java.util.Map;

public class AppointmentTypeFactory {
    private static final Map<String, AppointmentType> types = new HashMap<>();

    static {
        AppointmentType checkup = new TreatmentCheckup();
        AppointmentType regular = new RegularBooking();

        types.put(checkup.getType(), checkup);
        types.put(regular.getType(), regular);
    }

    public static AppointmentType getAppointmentTypeByName(String type) {
        return types.get(type.toUpperCase());
    }

    public static Map<String, AppointmentType> getAllTypes() {
        return types;
    }

    public static AppointmentType getById(int id) {
        return types.values().stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
