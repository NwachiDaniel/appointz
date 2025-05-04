package com.example.appoint.service;

import com.example.appoint.model.Appointment;
import com.example.appoint.model.User;
import com.example.appoint.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    // Save appointment and log info
    public void scheduleAppointment(Appointment appointment) {
        appointmentRepository.save(appointment);

        User user = appointment.getUser();
        String userName = user != null ? user.getName() : "Unknown User";

        System.out.println("Appointment Scheduled:");
        System.out.println("👤 User: " + userName);
        System.out.println("📅 Date: " + appointment.getDate());
        System.out.println("⏰ Time: " + appointment.getTime());
        System.out.println("📌 Type: " + appointment.getType());
        System.out.println("📋 Status: " + appointment.getStatus());
    }

    // Cancel appointment if the user owns it
    public boolean cancelAppointment(Long appointmentId, Long userId) {
        Optional<Appointment> optional = appointmentRepository.findByIdAndUserId(appointmentId, userId);
        if (optional.isPresent()) {
            appointmentRepository.deleteById(appointmentId);
            return true;
        }
        return false;
    }

    // Get all appointments by user
    public List<Appointment> getAppointmentsByUser(User user) {
        return appointmentRepository.findByUser(user);
    }
    public List<Appointment> getAppointmentsForToday(User user) {
        LocalDate today = LocalDate.now();
        return appointmentRepository.findByUserAndDate(user, today);
    }

}   

