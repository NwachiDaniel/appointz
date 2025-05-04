package com.example.appoint.controller;

import com.example.appoint.factory.AppointmentType;
import com.example.appoint.factory.AppointmentTypeFactory;
import com.example.appoint.model.Appointment;
import com.example.appoint.model.AppointmentStatus;
import com.example.appoint.model.User;
import com.example.appoint.observer.AdminObserver;
import com.example.appoint.observer.AppointmentNotifier;

import com.example.appoint.observer.LoggerManager;
import com.example.appoint.repository.AppointmentRepository;
import com.example.appoint.repository.UserRepository;
import com.example.appoint.service.AppointmentService;
import com.example.appoint.service.UserService;
import com.example.appoint.service.AppointService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

 
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AppointController {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentService appointmentService;
    private final UserRepository userRepository;
    private final UserService userService;
    // Observer pattern for appointment notifications
     
    private final AppointService appointService;
    private final AppointmentNotifier notifier = new AppointmentNotifier();

    // Show appointment page
    @GetMapping("/appointments")
    public String showAppointmentsPage(Model model, HttpSession session) {
        try {
            User currentUser = (User) session.getAttribute("loggedInUser");
            if (currentUser == null) {
                return "redirect:/login";
            }

            model.addAttribute("user", currentUser);
            model.addAttribute("appointment", new Appointment());
            model.addAttribute("appointments", appointmentService.getAppointmentsByUser(currentUser));

            model.addAttribute("types", List.of(
                new StatusOption(1, "Regular Booking"),
                new StatusOption(2, "Treatment Checkup")
            ));

            model.addAttribute("status", List.of(
                new StatusOption(1, "SCHEDULED"),
                new StatusOption(2, "COMPLETED"),
                new StatusOption(3, "CANCELLED")
            ));

            return "appointments";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/error";
        }
    }

    // Book appointment
    @PostMapping("/appointments/book")
    public String bookAppointment(@ModelAttribute Appointment appointment,
                                  @RequestParam String status, String username,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("loggedInUser");
        if (currentUser == null) {
            return "redirect:/login";
        }
    
        try {
            // Set user
            appointment.setUser(currentUser);
    
            // Fallback default values
            appointment.setDate(appointment.getDate() != null ? appointment.getDate() : LocalDate.now().plusDays(1));
            appointment.setTime(appointment.getTime() != null ? appointment.getTime() : LocalTime.now().plusHours(1));
    
            // Set type using Factory pattern
            String typeStr = appointment.getType();
            AppointmentType type = AppointmentTypeFactory.getAppointmentTypeByName(typeStr);
            appointment.setType(type.getType());
    
            // Set status using passed param
            appointment.setStatus(AppointmentStatus.valueOf(status.toUpperCase()));
    
            // Save and notify
            appointmentRepository.save(appointment);
            notifier.addObserver(new AdminObserver());
            notifier.notifyObservers("Appointment booked for user: " + currentUser.getUsername());
    
            redirectAttributes.addFlashAttribute("success", "Appointment successfully booked.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to book appointment: " + e.getMessage());
        }
    
        return "redirect:/appointments";
    }
    
    

    // Cancel appointment
    @PostMapping("/appointments/cancel/{id}")
    public String cancelAppointment(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        boolean canceled = appointmentService.cancelAppointment(id, user.getId());
        if (canceled) {
            //redirectAttributes.addFlashAttribute("success", "Appointment canceled successfully.");
            appointService.bookAppointment(user.getUsername());
           // LoggerManager.getInstance().log("Appointment canceled for user: " + user.getUsername());
            System.out.println( "Appointment booked for " + user.getUsername() + " has been canceled.");
        } else {
            //redirectAttributes.addFlashAttribute("error", "Failed to cancel the appointment.");
            LoggerManager.getInstance().log("Failed to cancel appointment for user: " + user.getUsername());
            //redirectAttributes.addFlashAttribute("error", "Failed to cancel the appointment.");
        }
        
        return "redirect:/appointments";
    }
    
    // Helper class for dropdown options
    public record StatusOption(int id, String name) {}

    // Home page
    @GetMapping("/")
    public String showHomePage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user != null) {
            model.addAttribute("user", user);
            model.addAttribute("todayAppointments", appointmentService.getAppointmentsForToday(user));
        }
        return "index";
    }

    // Show Register form
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // Process Registration
    @PostMapping("/register")
    public String processRegister(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Email already in use. Try a different one.");
            return "redirect:/register";
        }

        user.setRole("USER");
        userRepository.save(user);
        //redirectAttributes.addFlashAttribute("success", "Registration successful! Please log in.");
        LoggerManager.getInstance().log("User " + user.getUsername() + " registered successfully.");
        return "redirect:/login";
    }

    // Show login form
    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    // Process login
    @PostMapping("/login")
    public String loginUser(@RequestParam String email,
                            @RequestParam String password,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        User user = userService.authenticate(email, password);
        if (user != null) {
            session.setAttribute("loggedInUser", user);
            return "redirect:/appointments";
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid credentials!");
            return "redirect:/login";
        }
    }

    // Logout
    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("success", "You have been logged out.");
        return "redirect:/";
    }

    // Show profile
    @GetMapping("/profile")
    public String showProfilePage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";
        model.addAttribute("user", user);
        return "profile";
    }

    // Update profile
    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute User updatedUser,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("loggedInUser");
        if (currentUser == null) return "redirect:/login";

        currentUser.setName(updatedUser.getName());
        currentUser.setEmail(updatedUser.getEmail());
        userRepository.save(currentUser);
        session.setAttribute("loggedInUser", currentUser);

        redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
        return "redirect:/profile";
    }
}
