package menus;

import HospitalNotificationSystem.NotifyAdministrator;
import db.TextDB;
import items.*;
import items.medical_records.MedicalRecord;
import items.medical_records.Treatment;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import user_classes.Pharmacist;

/**
 * PharmacistMenu
 * Displays a menu for pharmacists to do pharmacist tasks
 */
public final class PharmacistMenu {
    private TextDB textDB;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Constructs a PharmacistMenu object with the given TextDB instance.
     * 
     * @param textDB The TextDB instance for accessing the database.
     */
    public PharmacistMenu(TextDB textDB) {
        this.textDB = textDB;
    }

    /**
     * Displays the pharmacist menu and handles user input for various pharmacist tasks.
     * 
     * @param scanner The scanner object to read user input.
     * @param pharmacist The pharmacist whose menu is being shown.
     * @throws IOException If an I/O error occurs during database operations.
     */
    public void showMenu(Scanner scanner, Pharmacist pharmacist) throws IOException {
        boolean back = false;

        while (!back) {
            System.out.println("\nPharmacist Menu:");
            System.out.println("1. View Appointment Outcome Records");
            System.out.println("2. Update Prescription Status");
            System.out.println("3. View Medication Inventory");
            System.out.println("4. Submit Replenishment Request");
            System.out.println("5. Back");
            System.out.print("Enter your choice: ");

            int choice = getIntInput(scanner);

            switch (choice) {
                case 1:
                    viewPatientMedicalRecords(scanner, pharmacist);
                    break;
                case 2:
                    updatePrescriptionStatus(scanner, pharmacist);
                    break;
                case 3:
                    viewMedicationInventory();
                    break;
                case 4:
                    submitReplenishmentRequest(scanner, pharmacist);
                    break;
                case 5:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Allows the pharmacist to view a patient's medical records.
     * 
     * @param scanner The scanner object to read user input.
     * @param pharmacist The pharmacist viewing the medical records.
     */
    private void viewPatientMedicalRecords(Scanner scanner, Pharmacist pharmacist) {
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

    /**
     * Allows the pharmacist to update the status of a prescription for a patient.
     * 
     * @param scanner The scanner object to read user input.
     * @param pharmacist The pharmacist updating the prescription status.
     * @throws IOException If an I/O error occurs while updating the record.
     */
    private void updatePrescriptionStatus(Scanner scanner, Pharmacist pharmacist) throws IOException {
        System.out.print("Enter Patient ID: ");
        String patientId = scanner.nextLine().trim();

        MedicalRecord record = textDB.getMedicalRecordByPatientId(patientId);
        if (record == null) {
            System.out.println("Medical record for Patient ID " + patientId + " not found.");
            return;
        }

        List<Treatment> treatments = record.getPastTreatments();
        if (treatments.isEmpty()) {
            System.out.println("No treatments found for this patient.");
            return;
        }

        // Display treatments and their prescriptions
        System.out.println("\nPast Treatments:");
        for (int i = 0; i < treatments.size(); i++) {
            Treatment treatment = treatments.get(i);
            System.out.println((i + 1) + ". " + treatment.getServiceType() + " on " + treatment.getDateOfAppointment().format(DATE_FORMATTER));
            List<Prescription> prescriptions = treatment.getAllPrescribedMedicine();
            if (prescriptions.isEmpty()) {
                System.out.println("   No prescriptions found for this treatment.");
            } else {
                for (int j = 0; j < prescriptions.size(); j++) {
                    Prescription p = prescriptions.get(j);
                    System.out.println("   " + (j + 1) + ". " + p.getMedicationName() + " | Status: " + p.getStatus());
                }
            }
        }

        System.out.print("Enter the number of the treatment to update prescriptions (or 0 to cancel): ");
        int treatmentChoice = getIntInput(scanner) - 1;

        if (treatmentChoice == -1 || treatmentChoice >= treatments.size()) {
            System.out.println("Operation cancelled or invalid selection.");
            return;
        }

        Treatment selectedTreatment = treatments.get(treatmentChoice);
        List<Prescription> prescriptions = selectedTreatment.getAllPrescribedMedicine();

        if (prescriptions.isEmpty()) {
            System.out.println("No prescriptions to update for this treatment.");
            return;
        }

        System.out.println("\nPrescriptions:");
        for (int j = 0; j < prescriptions.size(); j++) {
            Prescription p = prescriptions.get(j);
            System.out.println((j + 1) + ". " + p.getMedicationName() + " | Status: " + p.getStatus());
        }

        System.out.print("Enter the number of the prescription to update (or 0 to cancel): ");
        int prescriptionChoice = getIntInput(scanner) - 1;

        if (prescriptionChoice == -1 || prescriptionChoice >= prescriptions.size()) {
            System.out.println("Operation cancelled or invalid selection.");
            return;
        }

        Prescription selectedPrescription = prescriptions.get(prescriptionChoice);
        System.out.println("Current Status: " + selectedPrescription.getStatus());
        System.out.print("Enter new status (e.g., Approved, Rejected, Dispensed): ");
        String newStatus = scanner.nextLine().trim();

        if (newStatus.isEmpty()) {
            System.out.println("Status cannot be empty.");
            return;
        }

        selectedPrescription.setStatus(newStatus);
        textDB.updateMedicalRecord(record);
        System.out.println("Prescription status updated successfully.");
    }

    /**
     * Displays the current medication inventory.
     */
    private void viewMedicationInventory() {
        List<Medication> medications = textDB.getMedications();
        if (medications.isEmpty()) {
            System.out.println("Medication inventory is empty.");
            return;
        }

        System.out.println("\nMedication Inventory:");
        System.out.printf("%-20s %-10s %-20s%n", "Medication Name", "Quantity", "Supplier");
        System.out.println("-------------------------------------------------------------");
        for (Medication med : medications) {
            System.out.printf("%-20s %-10d %-20s%n",
                    med.getName(),
                    med.getQuantity(),
                    med.getSupplier() != null ? med.getSupplier() : "N/A");
        }
    }

    /**
     * Allows the pharmacist to submit a replenishment request for medication.
     * 
     * @param scanner The scanner object to read user input.
     * @param pharmacist The pharmacist submitting the replenishment request.
     * @throws IOException If an I/O error occurs during the request submission.
     */
    private void submitReplenishmentRequest(Scanner scanner, Pharmacist pharmacist) throws IOException {
        System.out.print("Enter Medication Name to replenish: ");
        String medicationName = scanner.nextLine().trim();
    
        if (medicationName.isEmpty()) {
            System.out.println("Medication name cannot be empty.");
            return;
        }
    
        // Check if medication exists in inventory
        Medication medication = null;
        for (Medication med : textDB.getMedications()) {
            if (med.getName().equalsIgnoreCase(medicationName)) {
                medication = med;
                break;
            }
        }
    
        if (medication == null) {
            System.out.println("Medication " + medicationName + " not found in inventory.");
            return;
        }
    
        System.out.print("Enter quantity to replenish: ");
        int quantity;
        while (true) {
            if (scanner.hasNextInt()) {
                quantity = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                if (quantity <= 0) {
                    System.out.print("Quantity must be positive. Enter again: ");
                } else {
                    break;
                }
            } else {
                System.out.print("Invalid input. Please enter a number: ");
                scanner.next(); // Consume invalid input
            }
        }
    
        // Create a new ReplenishmentRequest
        ReplenishmentRequest request = new ReplenishmentRequest(
                medicationName,
                quantity,
                "Pharmacist: " + pharmacist.getHospitalID(),
                java.time.LocalDate.now()
        );
    
        // Add the request to TextDB
        textDB.addReplenishmentRequest(request);
        System.out.println("Replenishment request submitted successfully.");

        // Notify Administrator
        NotifyAdministrator.getInstance().notifyAdminUser(request.toString());
    }

    /**
     * Helper method to get an integer input from the user.
     * 
     * @param scanner The scanner object to read user input.
     * @return The integer input entered by the user.
     */
    private int getIntInput(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Please enter a number: ");
            scanner.next();
        }
        int out = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        return out;
    }
}
