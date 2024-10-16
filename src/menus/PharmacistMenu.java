package menus;

import db.TextDB;
import user_classes.Pharmacist;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class PharmacistMenu {
    private TextDB textDB;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public PharmacistMenu(TextDB textDB) {
        this.textDB = textDB;
    }

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
                    viewAppointmentOutcomeRecords(pharmacist);
                    break;
                case 2:
                    updatePrescriptionStatus(scanner, pharmacist);
                    break;
                case 3:
                    viewMedicationInventory();
                    break;
                case 4:
                    submitReplenishmentRequest(scanner);
                    break;
                case 5:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void viewAppointmentOutcomeRecords(Pharmacist pharmacist) {
        // Implementation for viewing appointment outcome records
    }

    private void updatePrescriptionStatus(Scanner scanner, Pharmacist pharmacist) {
        // Implementation for updating prescription status
    }

    private void viewMedicationInventory() {
        // Implementation for viewing medication inventory
    }

    private void submitReplenishmentRequest(Scanner scanner) {
        // Implementation for submitting replenishment request
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