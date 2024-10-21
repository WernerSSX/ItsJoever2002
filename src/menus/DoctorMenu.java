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
import java.util.stream.Collectors;

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
            System.out.println("8. Log out");
            System.out.print("Enter your choice: ");

            int choice = getIntInput(scanner);

            switch (choice) {
                case 1:
                    viewPatientMedicalRecords(scanner, doctor);
                    break;
                case 2:
                    //updatePatientMedicalRecords(scanner, doctor);
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
        System.out.print("Enter Patient ID to view medical records: ");
        String patientId = scanner.nextLine().trim();

        MedicalRecord record = textDB.getMedicalRecordByPatientId(patientId);
        if (record == null) {
            System.out.println("Medical record for Patient ID " + patientId + " not found.");
            return;
        }

        System.out.println();
        record.display();
    }



    private void acceptOrDeclineAppointmentRequests(Scanner scanner, Doctor doctor) throws IOException {
    List<Appointment> requestedAppointments = textDB.getRequestedAppointmentsByDoctor(doctor.getHospitalID());

    if (requestedAppointments.isEmpty()) {
        System.out.println("You have no appointment requests to review.");
        return;
    }

    System.out.println("\nAppointment Requests:");
        for (int i = 0; i < requestedAppointments.size(); i++) {
            Appointment appt = requestedAppointments.get(i);
            System.out.println((i + 1) + ". Appointment ID: " + appt.getId() +
                            ", Patient ID: " + appt.getPatientId() +
                            ", Date: " + appt.getDate().format(DATE_FORMATTER) +
                            ", Time: " + appt.getTimeSlot());
        }

        System.out.print("Enter the number of the appointment you want to review (or 0 to cancel): ");
        int choice = getIntInput(scanner) - 1;

        if (choice == -1) {
            System.out.println("Operation cancelled.");
            return;
        }

        if (choice < 0 || choice >= requestedAppointments.size()) {
            System.out.println("Invalid selection. Please try again.");
            return;
        }

        Appointment selectedAppointment = requestedAppointments.get(choice);
        selectedAppointment.print();

        System.out.print("Do you want to accept this appointment? (y/n): ");
        String decision = scanner.nextLine().trim().toLowerCase();

        switch (decision) {
            case "y":
                textDB.updateAppointmentStatus(selectedAppointment.getId(), "Scheduled");
                System.out.println("Appointment ID " + selectedAppointment.getId() + " has been accepted and scheduled.");
                break;
            case "n":
                textDB.updateAppointmentStatus(selectedAppointment.getId(), "Declined");
                System.out.println("Appointment ID " + selectedAppointment.getId() + " has been declined.");
                break;
            default:
                System.out.println("Invalid input. Please enter 'accept' or 'decline'.");
        }
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



    private void viewUpcomingAppointments(Doctor doctor) {
        // Fetch all appointments
        List<Appointment> allAppointments = textDB.getAppointments();

        // Current date and time for comparison
        LocalDateTime now = LocalDateTime.now();

        // Filter appointments based on:
        // 1. Assigned to the current doctor
        // 2. Status is "Scheduled"
        // 3. Start time is after the current time
        List<Appointment> upcomingAppointments = allAppointments.stream()
                .filter(appt -> appt.getDoctorId().equals(doctor.getHospitalID()))
                .filter(appt -> appt.getStatus().equalsIgnoreCase("Scheduled"))
                .filter(appt -> appt.getTimeSlot().getStartTime().isAfter(now))
                .sorted((a1, a2) -> a1.getTimeSlot().getStartTime().compareTo(a2.getTimeSlot().getStartTime()))
                .collect(Collectors.toList());

        if (upcomingAppointments.isEmpty()) {
            System.out.println("You have no upcoming appointments.");
            return;
        }

        System.out.println("\nUpcoming Appointments:");
        for (int i = 0; i < upcomingAppointments.size(); i++) {
            Appointment appt = upcomingAppointments.get(i);
            String date = appt.getTimeSlot().getStartTime().format(DATE_FORMATTER);
            String startTime = appt.getTimeSlot().getStartTime().format(TIME_FORMATTER);
            String endTime = appt.getTimeSlot().getEndTime().format(TIME_FORMATTER);
            System.out.println((i + 1) + ". Appointment ID: " + appt.getId() +
                    ", Patient ID: " + appt.getPatientId() +
                    ", Date: " + date +
                    ", Time: " + startTime + " - " + endTime);
        }
    }



    /**
     * Allows the doctor to record the outcome of a completed appointment.
     *
     * Appointment Outcome Record includes:
     * - Date of Appointment
     * - Type of service provided (e.g., consultation, X-ray, blood test etc.)
     * - Any prescribed medications:
     *   - Medication name
     *   - Status (default is pending)
     * - Consultation notes
     *
     * @param scanner Scanner object for user input
     * @param doctor  The doctor recording the outcome
     * @throws IOException If an I/O error occurs during data saving
     */
    public void recordAppointmentOutcome(Scanner scanner, Doctor doctor) throws IOException {
        LocalDateTime now = LocalDateTime.now();
        // Step 1: Fetch eligible appointments
        List<Appointment> eligibleAppointments = textDB.getAppointments().stream()
            .filter(appt -> appt.getDoctorId().equals(doctor.getHospitalID()))
            .filter(appt -> appt.getStatus().equalsIgnoreCase("Scheduled"))
            .filter(appt -> appt.getTimeSlot().getStartTime().isAfter(now))
            .sorted((a1, a2) -> a1.getTimeSlot().getStartTime().compareTo(a2.getTimeSlot().getStartTime()))
            .collect(Collectors.toList());

        if (eligibleAppointments.isEmpty()) {
            System.out.println("You have no completed appointments to record outcomes for.");
            return;
        }

        // Step 2: Display the eligible appointments
        System.out.println("\nCompleted Appointments:");
        for (int i = 0; i < eligibleAppointments.size(); i++) {
            Appointment appt = eligibleAppointments.get(i);
            System.out.println((i + 1) + ". Appointment ID: " + appt.getId() +
                    ", Patient ID: " + appt.getPatientId() +
                    ", Date: " + appt.getDate().format(DATE_FORMATTER) +
                    ", Time: " + appt.getTimeSlot().getStartTime().toLocalTime().format(TIME_FORMATTER) +
                    " - " + appt.getTimeSlot().getEndTime().toLocalTime().format(TIME_FORMATTER));
        }

        // Step 3: Select an appointment
        System.out.print("Enter the number of the appointment to record outcome (or 0 to cancel): ");
        int choice = getIntInput(scanner) - 1;

        if (choice == -1 || choice >= eligibleAppointments.size()) {
            System.out.println("Operation cancelled or invalid selection.");
            return;
        }

        Appointment selectedAppt = eligibleAppointments.get(choice);

        // Step 4: Input outcome data
        System.out.println("\nRecording outcome for Appointment ID: " + selectedAppt.getId());

        // Type of service
        System.out.print("Enter the type of service provided (e.g., consultation, X-ray, blood test): ");
        String serviceType = scanner.nextLine().trim();
        while (serviceType.isEmpty()) {
            System.out.print("Service type cannot be empty. Please enter again: ");
            serviceType = scanner.nextLine().trim();
        }

        // Prescribed medications
        List<Prescription> prescriptions = new ArrayList<>();
        System.out.println("Enter prescribed medications (enter 'done' when finished):");
        while (true) {
            System.out.print("Medication Name (or 'done'): ");
            String medName = scanner.nextLine().trim();
            if (medName.equalsIgnoreCase("done")) {
                break;
            }
            if (medName.isEmpty()) {
                System.out.println("Medication name cannot be empty.");
                continue;
            }
            System.out.print("Status for " + medName + " (default is 'pending'): ");
            String status = scanner.nextLine().trim();
            if (status.isEmpty()) {
                status = "pending";
            }
            prescriptions.add(new Prescription(medName, status));
        }

        // Consultation notes
        System.out.print("Enter consultation notes: ");
        String consultationNotes = scanner.nextLine().trim();
        while (consultationNotes.isEmpty()) {
            System.out.print("Consultation notes cannot be empty. Please enter again: ");
            consultationNotes = scanner.nextLine().trim();
        }

        // Step 5: Create Treatment object
        Treatment treatment = new Treatment();
        treatment.setServiceType(serviceType);
        treatment.setDateOfAppointment(selectedAppt.getTimeSlot().getStartTime().toLocalDate());
        treatment.setTreatmentComments(consultationNotes);
        for (Prescription p : prescriptions) {
            treatment.addPrescription(p);
        }

        // Step 6: Update MedicalRecord
        MedicalRecord record = textDB.getMedicalRecordByPatientId(selectedAppt.getPatientId());
        if (record == null) {
            System.out.println("Medical record for Patient ID " + selectedAppt.getPatientId() + " not found.");
            return;
        }

        record.addTreatment(treatment);
        textDB.updateMedicalRecord(record);

        // Step 7: Update Appointment status to "Completed" and set outcomeRecord
        selectedAppt.setStatus("Completed");
        selectedAppt.setOutcomeRecord(treatment.serialize());
        textDB.updateAppointment(selectedAppt);

        System.out.println("Appointment outcome recorded successfully.");
    }

    /**
     * Utility method to safely get integer input from the user.
     *
     * @param scanner Scanner object for user input
     * @return The integer entered by the user
     */
    private int getIntInput(Scanner scanner) {
        while (true) {
            if (scanner.hasNextInt()) {
                int num = scanner.nextInt();
                scanner.nextLine(); // consume the newline character
                return num;
            } else {
                System.out.print("Invalid input. Please enter a valid number: ");
                scanner.nextLine(); // consume the invalid input
            }
        }
    }


}
