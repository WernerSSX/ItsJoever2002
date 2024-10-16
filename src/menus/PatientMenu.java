package menus;
import services.*;

import db.TextDB;
import user_classes.Doctor;
import user_classes.Patient;
import items.Appointment;
import items.MedicalRecord;
import items.Schedule;
import items.TimeSlot;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class PatientMenu {
    private TextDB textDB;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public PatientMenu(TextDB textDB) {
        this.textDB = textDB;
    }

    public void showMenu(Scanner scanner, Patient patient) throws IOException {
        while (true) {
            System.out.println("\nPatient Menu:");
            System.out.println("1. View Medical Record");
            System.out.println("2. Update Contact Information");
            System.out.println("3. View Available Appointment Slots");
            System.out.println("4. Schedule Appointment");
            System.out.println("5. Reschedule Appointment");
            System.out.println("6. Cancel Appointment");
            System.out.println("7. View Appointment Status");
            System.out.println("8. View Appointment Outcome Records");
            System.out.println("9. Change Password");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            int choice = getIntInput(scanner);

            switch (choice) {
                case 1:
                    viewMedicalRecord(patient);
                    break;
                case 2:
                    updateContactInformation(scanner, patient);
                    break;
                case 3:
                    viewAvailableAppointmentSlotsWithDoctor(scanner);
                    break;
                case 4:
                    scheduleAppointment(scanner, patient);
                    break;
                case 5:
                    //rescheduleAppointment(scanner, patient);
                    break;
                case 6:
                    cancelAppointment(scanner, patient);
                    break;
                case 7:
                    viewAppointmentStatus(patient);
                    break;
                case 8:
                    
                    break;
                case 9:
                    changePassword(scanner, patient);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void viewMedicalRecord(Patient patient) {
        MedicalRecord record = patient.getMedicalRecord();
        System.out.println("\nMedical Record:");
        System.out.println("Patient ID: " + patient.getHospitalID());
        System.out.println("Name: " + patient.getName());
        System.out.println("Date of Birth: " + patient.getDateOfBirth());
        System.out.println("Gender: " + patient.getGender());
        System.out.println("Contact Information: " + patient.getContactInformation());
        System.out.println("Blood Type: " + record.getBloodType());
        System.out.println("Past Diagnoses: " + record.getPastDiagnoses());
        System.out.println("Past Treatments: " + record.getPastTreatments());
    }

    private void updateContactInformation(Scanner scanner, Patient patient) {
        System.out.print("Enter new email address: ");
        String email = scanner.nextLine();
        System.out.print("Enter new phone number: ");
        String phone = scanner.nextLine();
        patient.getContactInformation().setEmailAddress(email);
        patient.getContactInformation().setPhoneNumber(phone);
        System.out.println("Contact information updated.");
    }

    private void viewAppointmentStatus(Patient patient) {
        List<Appointment> appointments = textDB.getAppointments().stream()
            .filter(appt -> appt.getPatientId().equals(patient.getHospitalID()))
            .collect(Collectors.toList());
        
        if (appointments.isEmpty()) {
            System.out.println("You have no upcoming appointments.");
        } else {
            System.out.println("\nYour Appointments:");
            for (Appointment appointment : appointments) {
                System.out.println("Appointment ID: " + appointment.getId() +
                                ", Doctor: " + appointment.getDoctorId() +
                                ", Date & Time: " + appointment.getTimeSlot() +
                                ", Status: " + appointment.getStatus());
            }
        }
    }

    private void viewAvailableAppointmentSlotsWithDoctor(Scanner scanner) {
        // Step 1: Display list of available doctors
        List<Doctor> doctors = textDB.getAllDoctors();
        if (doctors.isEmpty()) {
            System.out.println("No doctors are currently available.");
            return;
        }

        System.out.println("\nAvailable Doctors:");
        for (int i = 0; i < doctors.size(); i++) {
            System.out.println((i + 1) + ". Dr. " + doctors.get(i).getName() + " (ID: " + doctors.get(i).getHospitalID() + ")");
        }

        System.out.print("Enter the number corresponding to the doctor you want to view available slots for: ");
        int doctorIndex = getIntInput(scanner) - 1;

        if (doctorIndex < 0 || doctorIndex >= doctors.size()) {
            System.out.println("Invalid selection. Please try again.");
            return;
        }

        Doctor selectedDoctor = doctors.get(doctorIndex);
        System.out.println("You have selected Dr. " + selectedDoctor.getName());

        // Step 2: Call the updated viewAvailableAppointmentSlots method
        viewAvailableAppointmentSlots(scanner, selectedDoctor);
    }

    private void viewAvailableAppointmentSlots(Scanner scanner, Doctor doctor) {
        System.out.print("Enter the date (yyyy-MM-dd) for which you want to see available slots: ");
        String dateInput = scanner.nextLine();
        LocalDate date;
        
        try {
            date = LocalDate.parse(dateInput, DATE_FORMATTER);
        } catch (Exception e) {
            System.out.println("Invalid date format. Please try again.");
            return;
        }
        
        List<TimeSlot> availableSlots = textDB.getAvailableAppointmentSlots(date, doctor);
        
        if (availableSlots.isEmpty()) {
            System.out.println("No available slots for Dr. " + doctor.getName() + " on " + date);
        } else {
            System.out.println("\nAvailable Appointment Slots with Dr. " + doctor.getName() + " on " + date + ":");
            for (TimeSlot slot : availableSlots) {
                System.out.println(slot);
            }
        }
    }

    private void scheduleAppointment(Scanner scanner, Patient patient) {
        // Step 1: Ask for the date of the appointment
        System.out.print("Enter the date (yyyy-MM-dd) for which you want to schedule an appointment: ");
        String dateInput = scanner.nextLine();
        LocalDate date;
        
        try {
            date = LocalDate.parse(dateInput, DATE_FORMATTER);
        } catch (Exception e) {
            System.out.println("Invalid date format. Please try again.");
            return;
        }
    
        // Step 2: Display list of available doctors
        List<Doctor> doctors = textDB.getAllDoctors();
        if (doctors.isEmpty()) {
            System.out.println("No doctors are currently available.");
            return;
        }
    
        System.out.println("\nAvailable Doctors:");
        for (int i = 0; i < doctors.size(); i++) {
            System.out.println((i + 1) + ". Dr. " + doctors.get(i).getName() + " (ID: " + doctors.get(i).getHospitalID() + ")");
        }
    
        System.out.print("Enter the number corresponding to the doctor you want to book an appointment with: ");
        int doctorIndex = getIntInput(scanner) - 1;
    
        if (doctorIndex < 0 || doctorIndex >= doctors.size()) {
            System.out.println("Invalid selection. Please try again.");
            return;
        }
    
        Doctor selectedDoctor = doctors.get(doctorIndex);
        System.out.println("You have selected Dr. " + selectedDoctor.getName());
    
        // Step 3: Display available time slots for the selected doctor and date
        List<TimeSlot> availableSlots = textDB.getAvailableAppointmentSlots(date, selectedDoctor);
        
        if (availableSlots.isEmpty()) {
            System.out.println("No available slots for Dr. " + selectedDoctor.getName() + " on " + date + ". Please choose a different date or doctor.");
            return;
        }
    
        System.out.println("\nAvailable Appointment Slots with Dr. " + selectedDoctor.getName() + " on " + date + ":");
        for (int i = 0; i < availableSlots.size(); i++) {
            System.out.println((i + 1) + ". " + availableSlots.get(i));
        }
    
        // Step 4: Ask the user to select a time slot by entering its index
        System.out.print("Enter the number corresponding to the time slot you want to book: ");
        int slotIndex = getIntInput(scanner) - 1;
    
        if (slotIndex < 0 || slotIndex >= availableSlots.size()) {
            System.out.println("Invalid selection. Please try again.");
            return;
        }
    
        TimeSlot selectedSlot = availableSlots.get(slotIndex);
        System.out.println("You have selected " + selectedSlot);
    
        // Step 5: Use the updated addAppointment method in TextDB
        boolean success = textDB.addAppointment(patient, selectedDoctor, date, selectedSlot);
    
        if (success) {
            System.out.println("Appointment scheduled successfully with Dr. " + selectedDoctor.getName() + " on " + date + " at " + selectedSlot + ".");
        } else {
            System.out.println("Failed to schedule appointment. Please try again.");
        }
    }

    private void cancelAppointment(Scanner scanner, Patient patient) {
        System.out.print("Enter the ID of the appointment you want to cancel: ");
        int appointmentId = getIntInput(scanner);
        boolean success = textDB.cancelAppointment(patient, appointmentId);
        if (success) {
            System.out.println("Appointment canceled successfully.");
        } else {
            System.out.println("Failed to cancel appointment. Please try again.");
        }
    }

    private void changePassword(Scanner scanner, Patient patient) {
        System.out.print("Enter current password: ");
        String currentPassword = scanner.nextLine();
        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine();
        
        // Assuming Patient class has a method to verify the current password
        if (patient.getPassword().equals(currentPassword)) {
            try {
                patient.setPassword(newPassword);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Password changed successfully.");
        } else {
            System.out.println("Failed to change password. Please try again.");
        }
    }

    private int getIntInput(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Please enter a number: ");
            scanner.next();
        }
        int out = scanner.nextInt();
        scanner.nextLine();
        return out;
    }
}
