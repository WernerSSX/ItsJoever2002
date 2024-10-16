package menus;

import db.TextDB;
import user_classes.Doctor;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import items.*;


public class DoctorMenu {
    private TextDB textDB;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public DoctorMenu(TextDB textDB) {
        this.textDB = textDB;
    }

    public void showMenu(Scanner scanner, Doctor doctor) throws IOException {
        boolean back = false;

        while (!back) {
            System.out.println("\nDoctor Menu:");
            System.out.println("1. View Patient Medical Records");
            System.out.println("2. Update Patient Medical Records");
            System.out.println("3. View Personal Schedule");
            System.out.println("4. Set Availability");
            System.out.println("5. Accept or Decline Appointment Requests");
            System.out.println("6. View Upcoming Appointments");
            System.out.println("7. Record Appointment Outcome");
            System.out.println("8. Back");
            System.out.print("Enter your choice: ");

            int choice = getIntInput(scanner);

            switch (choice) {
                case 1:
                    viewPatientMedicalRecords(scanner, doctor);
                    break;
                case 2:
                    updatePatientMedicalRecords(scanner, doctor);
                    break;
                case 3:
                    viewPersonalSchedule(doctor);
                    break;
                case 4:
                    setAvailability(scanner, doctor);
                    break;
                case 5:
                    acceptOrDeclineAppointmentRequests(scanner, doctor);
                    break;
                case 6:
                    viewUpcomingAppointments(doctor);
                    break;
                case 7:
                    recordAppointmentOutcome(scanner, doctor);
                    break;
                case 8:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void viewPatientMedicalRecords(Scanner scanner, Doctor doctor) {
        // Implementation for viewing patient medical records
    }

    private void updatePatientMedicalRecords(Scanner scanner, Doctor doctor) {
        // Implementation for updating patient medical records
    }

    private void viewPersonalSchedule(Doctor doctor) {
        Schedule schedule = doctor.getSchedule();
        System.out.println("Personal Schedule:");
        for (LocalDate date : schedule.getAvailability().keySet()) {
            System.out.println("Date: " + date.format(DATE_FORMATTER));
            List<TimeSlot> slots = schedule.getAvailableTimeSlots(date);
            for (TimeSlot slot : slots) {
                System.out.println("  " + slot.getStartTime().toLocalTime().format(TIME_FORMATTER) +
                                   " - " + slot.getEndTime().toLocalTime().format(TIME_FORMATTER));
            }
        }
    }

    private void setAvailability(Scanner scanner, Doctor doctor) throws IOException {
        System.out.print("Enter date to set availability (yyyy-MM-dd): ");
        String dateStr = scanner.nextLine();
        LocalDate date;
        try {
            date = LocalDate.parse(dateStr, DATE_FORMATTER);
        } catch (Exception e) {
            System.out.println("Invalid date format.");
            return;
        }

        List<TimeSlot> availableSlots = new ArrayList<>();
        boolean addingSlots = true;

        while (addingSlots) {
            System.out.print("Enter start time for available slot (HH:mm) or 'done' to finish: ");
            String startStr = scanner.nextLine();
            if (startStr.equalsIgnoreCase("done")) {
                break;
            }

            System.out.print("Enter end time for available slot (HH:mm): ");
            String endStr = scanner.nextLine();

            try {
                LocalTime startTime = LocalTime.parse(startStr, TIME_FORMATTER);
                LocalTime endTime = LocalTime.parse(endStr, TIME_FORMATTER);
                TimeSlot slot = new TimeSlot(
                        LocalDateTime.of(date, startTime),
                        LocalDateTime.of(date, endTime),
                        true
                );
                availableSlots.add(slot);
            } catch (Exception e) {
                System.out.println("Invalid time format. Please try again.");
            }
        }

        if (!availableSlots.isEmpty()) {
            // Update the Doctor's schedule in TextDB to ensure persistence
            doctor.setAvailability(date, availableSlots);
            textDB.updateDoctorSchedule(doctor.getHospitalID(), doctor.getSchedule());
            System.out.println("Availability updated successfully.");
        } else {
            System.out.println("No availability slots added.");
        }
    }


    private void acceptOrDeclineAppointmentRequests(Scanner scanner, Doctor doctor) {
        // Implementation for accepting or declining appointment requests
    }

    private void viewUpcomingAppointments(Doctor doctor) {
        List<Appointment> upcoming = textDB.getUpcomingAppointmentsByDoctorId(doctor.getHospitalID());
        System.out.println("Upcoming Appointments:");
        for (Appointment appt : upcoming) {
            System.out.println(appt);
        }
    }

    private void recordAppointmentOutcome(Scanner scanner, Doctor doctor) {
        // Implementation for recording appointment outcome
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
