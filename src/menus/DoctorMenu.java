package menus;

import db.TextDB;
import user_classes.Doctor;
import items.MedicalRecord;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class DoctorMenu {
    private TextDB textDB;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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
        // Implementation for viewing personal schedule
    }

    private void setAvailability(Scanner scanner, Doctor doctor) {
        // Implementation for setting availability
    }

    private void acceptOrDeclineAppointmentRequests(Scanner scanner, Doctor doctor) {
        // Implementation for accepting or declining appointment requests
    }

    private void viewUpcomingAppointments(Doctor doctor) {
        // Implementation for viewing upcoming appointments
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