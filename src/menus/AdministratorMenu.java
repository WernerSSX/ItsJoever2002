package menus;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import user_classes.*;
import db.TextDB;

import java.util.Base64;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import items.*;

public class AdministratorMenu {

    private TextDB textDB;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public AdministratorMenu(TextDB textDB) {
        this.textDB = textDB;
    }

    public void showMenu(Scanner scanner, Administrator admin) throws IOException {
        boolean exit = false;

        while (!exit) {
            System.out.println("\nAdministrator Menu:");
            System.out.println("1. View and Manage Hospital Staff");
            System.out.println("2. View Appointments Details");
            System.out.println("3. View and Manage Medication Inventory");
            System.out.println("4. Approve Replenishment Requests");
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");
            
            int choice = getIntInput(scanner);

            switch (choice) {
                case 1:
                    manageHospitalStaff(scanner);
                    break;
                case 2:
                    viewAppointmentsDetails(scanner);
                    break;
                case 3:
                    manageMedicationInventory(scanner);
                    break;
                case 4:
                    approveReplenishmentRequests(scanner);
                    break;
                case 5:
                    exit = true;
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // ----------------------- 1. View and Manage Hospital Staff ----------------------- //

    private void manageHospitalStaff(Scanner scanner) throws IOException {
        boolean back = false;

        while (!back) {
            System.out.println("\nManage Hospital Staff:");
            System.out.println("1. Add User");
            System.out.println("2. Remove User");
            System.out.println("3. View All Users");
            System.out.println("4. Reset User Password");
            System.out.println("5. Back");
            System.out.print("Enter your choice: ");
            
            int choice = getIntInput(scanner);

            switch (choice) {
                case 1:
                    addUser(scanner);
                    break;
                case 2:
                    removeUser(scanner);
                    break;
                case 3:
                    viewAllUsers();
                    break;
                case 4:
                    resetUserPassword(scanner);
                    break;
                case 5:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    private static String hashPassword(String hospitalID, String password) {
        String combined = hospitalID + password;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(combined.getBytes());
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing algorithm not found", e);
        }
    }

    // Add a new user
    private void addUser(Scanner scanner) throws IOException {
        System.out.println("\nAdd New User:");

        String role = getNonEmptyString(scanner, "Enter role (Administrator/Doctor/Pharmacist/Patient): ").toLowerCase();

        // Validate role
        if (!role.equals("administrator") && !role.equals("doctor") && 
            !role.equals("pharmacist") && !role.equals("patient")) {
            System.out.println("Invalid role. User not added.");
            return;
        }

        String hospitalID = getNonEmptyString(scanner, "Enter Hospital ID: ");

        // Check if Hospital ID already exists
        if (textDB.getUserByHospitalID(hospitalID) != null) {
            System.out.println("Hospital ID already exists. User not added.");
            return;
        }

        String name = getNonEmptyString(scanner, "Enter Name: ");
        LocalDate dateOfBirth = getValidDate(scanner, "Enter Date of Birth (yyyy-MM-dd): ");
        String gender = getNonEmptyString(scanner, "Enter Gender (Male/Female): ");
        String email = getNonEmptyString(scanner, "Enter Email: ");
        String phone = getNonEmptyString(scanner, "Enter Phone Number: ");
        String default_password = "password"; // Default password
        String password = hashPassword(hospitalID, default_password);

        User newUser = null;

        switch (role) {
            case "administrator":
                newUser = new Administrator(hospitalID, password, name, dateOfBirth, gender);
                break;
            case "doctor":
                newUser = new Doctor(hospitalID, password, name, dateOfBirth, gender, new Schedule());
                break;
            case "pharmacist":
                newUser = new Pharmacist(hospitalID, password, name, dateOfBirth, gender);
                break;
            case "patient":
                newUser = new Patient(hospitalID, password, name, dateOfBirth, gender);
                break;
            default:
                System.out.println("Invalid role. User not added.");
                return;
        }

        textDB.addUser(newUser);
        System.out.println("User added successfully!");

        // Save immediately after adding
        try {
            TextDB.saveToFile("users.txt");
            System.out.println("Changes saved to file.");
        } catch (IOException e) {
            System.out.println("Error saving to file: " + e.getMessage());
        }
    }

    // Remove an existing user
    private void removeUser(Scanner scanner) throws IOException {
        System.out.println("\nRemove User:");
        String hospitalID = getNonEmptyString(scanner, "Enter Hospital ID of user to remove: ");

        User userToRemove = textDB.getUserByHospitalID(hospitalID);

        if (userToRemove != null) {
            textDB.removeUser(userToRemove);
            System.out.println("User removed successfully!");

            // Save changes after removal
            try {
                TextDB.saveToFile("users.txt");
                System.out.println("Changes saved to file.");
            } catch (IOException e) {
                System.out.println("Error saving to file: " + e.getMessage());
            }
        } else {
            System.out.println("User not found.");
        }
    }

    // View all users
    private void viewAllUsers() {
        System.out.println("\nList of All Users:");
        for (User user : textDB.getUsers()) {
            System.out.println(user);
            System.out.println("======================================");
        }
    }

    // Reset user password
    private void resetUserPassword(Scanner scanner) throws IOException {
        System.out.print("Enter Hospital ID of the user: ");
        String hospitalID = scanner.nextLine().trim();
        User user = textDB.getUserByHospitalID(hospitalID);
        if (user != null) {
            String newPassword = "password"; // Default password
            newPassword = hashPassword(hospitalID, newPassword);
            user.setPassword(newPassword);
            System.out.println("Password reset successfully for user with Hospital ID: " + hospitalID);
            textDB.updateUserPassword(user);
        } else {
            System.out.println("User with Hospital ID " + hospitalID + " not found.");
        }
    }

    // ----------------------- 2. View Appointments Details ----------------------- //

    private void viewAppointmentsDetails(Scanner scanner) {
        boolean back = false;

        while (!back) {
            System.out.println("\nView Appointments Details:");
            System.out.println("1. View All Appointments");
            System.out.println("2. View Appointment by ID");
            System.out.println("3. Back");
            System.out.print("Enter your choice: ");
            
            int choice = getIntInput(scanner);

            switch (choice) {
                case 1:
                    viewAllAppointments();
                    break;
                case 2:
                    viewAppointmentById(scanner);
                    break;
                case 3:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // View all appointments
    private void viewAllAppointments() {
        List<Appointment> appointments = textDB.getAppointments();
        if (appointments.isEmpty()) {
            System.out.println("\nNo appointments found.");
            return;
        }

        System.out.println("\nAll Appointments:");
        for (Appointment appointment : appointments) {
            appointment.print();
            System.out.println("-------------------------");
        }
    }

    // View appointment by ID
    private void viewAppointmentById(Scanner scanner) {
        System.out.print("Enter Appointment ID to view details: ");
        String input = scanner.nextLine().trim();
        int appointmentId;

        try {
            appointmentId = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid Appointment ID format.");
            return;
        }

        Appointment appointment = textDB.getAppointmentById(appointmentId);
        if (appointment != null) {
            System.out.println("\nAppointment Details:");
            appointment.print();
        } else {
            System.out.println("Appointment with ID " + appointmentId + " not found.");
        }
    }

    // ----------------------- 3. View and Manage Medication Inventory ----------------------- //

    private void manageMedicationInventory(Scanner scanner) throws IOException {
        boolean back = false;

        while (!back) {
            System.out.println("\nManage Medication Inventory:");
            System.out.println("1. View Inventory");
            System.out.println("2. Add Medication");
            System.out.println("3. Update Medication");
            System.out.println("4. Remove Medication");
            System.out.println("5. Back");
            System.out.print("Enter your choice: ");
            
            int choice = getIntInput(scanner);

            switch (choice) {
                case 1:
                    viewMedicationInventory();
                    break;
                case 2:
                    addMedication(scanner);
                    break;
                case 3:
                    updateMedication(scanner);
                    break;
                case 4:
                    removeMedication(scanner);
                    break;
                case 5:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // View medication inventory
    private void viewMedicationInventory() {
        List<Medication> medications = textDB.getMedications();
        if (medications.isEmpty()) {
            System.out.println("\nMedication inventory is empty.");
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

    // Add a new medication
    private void addMedication(Scanner scanner) throws IOException {
        System.out.println("\nAdd New Medication:");
        String name = getNonEmptyString(scanner, "Enter Medication Name: ");

        // Check if medication already exists
        for (Medication med : textDB.getMedications()) {
            if (med.getName().equalsIgnoreCase(name)) {
                System.out.println("Medication already exists in inventory.");
                return;
            }
        }

        int quantity = getPositiveInt(scanner, "Enter Quantity: ");
        String supplier = getNonEmptyString(scanner, "Enter Supplier Name: ");

        Medication newMedication = new Medication(name, quantity, supplier);
        textDB.getMedications().add(newMedication);
        textDB.saveMedicationInventory("inventory.txt");
        System.out.println("Medication added successfully.");
    }

    // Update existing medication
    private void updateMedication(Scanner scanner) throws IOException {
        System.out.print("Enter Medication Name to update: ");
        String name = scanner.nextLine().trim();

        Medication medication = null;
        for (Medication med : textDB.getMedications()) {
            if (med.getName().equalsIgnoreCase(name)) {
                medication = med;
                break;
            }
        }

        if (medication == null) {
            System.out.println("Medication not found in inventory.");
            return;
        }

        System.out.println("Current Quantity: " + medication.getQuantity());
        int newQuantity = getPositiveInt(scanner, "Enter new Quantity: ");
        medication.setQuantity(newQuantity);

        System.out.println("Current Supplier: " + (medication.getSupplier() != null ? medication.getSupplier() : "N/A"));
        String newSupplier = getNonEmptyString(scanner, "Enter new Supplier Name: ");
        medication.setSupplier(newSupplier);

        textDB.updateMedication(medication);
        System.out.println("Medication updated successfully.");
    }

    // Remove a medication
    private void removeMedication(Scanner scanner) throws IOException {
        System.out.print("Enter Medication Name to remove: ");
        String name = scanner.nextLine().trim();

        Medication medication = null;
        for (Medication med : textDB.getMedications()) {
            if (med.getName().equalsIgnoreCase(name)) {
                medication = med;
                break;
            }
        }

        if (medication == null) {
            System.out.println("Medication not found in inventory.");
            return;
        }

        textDB.getMedications().remove(medication);
        textDB.saveMedicationInventory("inventory.txt");
        System.out.println("Medication removed successfully.");
    }

    // ----------------------- 4. Approve Replenishment Requests ----------------------- //

    private void approveReplenishmentRequests(Scanner scanner) throws IOException {
        boolean back = false;

        while (!back) {
            System.out.println("\nApprove Replenishment Requests:");
            System.out.println("1. View All Requests");
            System.out.println("2. Approve a Request");
            System.out.println("3. Reject a Request");
            System.out.println("4. Back");
            System.out.print("Enter your choice: ");
            
            int choice = getIntInput(scanner);

            switch (choice) {
                case 1:
                    viewAllReplenishmentRequests();
                    break;
                case 2:
                    processReplenishmentRequest(scanner, true);
                    break;
                case 3:
                    processReplenishmentRequest(scanner, false);
                    break;
                case 4:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // View all replenishment requests
    private void viewAllReplenishmentRequests() {
        List<ReplenishmentRequest> requests = textDB.replenishmentRequests;
        if (requests.isEmpty()) {
            System.out.println("\nNo replenishment requests found.");
            return;
        }

        System.out.println("\nReplenishment Requests:");
        for (int i = 0; i < requests.size(); i++) {
            ReplenishmentRequest req = requests.get(i);
            System.out.println((i + 1) + ". Medication: " + req.getMedicationName() +
                               ", Quantity: " + req.getQuantity() +
                               ", Requested By: " + req.getRequestedBy() +
                               ", Request Date: " + req.getRequestDate().format(DATE_FORMATTER));
        }
    }

    // Approve or Reject a replenishment request
    private void processReplenishmentRequest(Scanner scanner, boolean isApprove) throws IOException {
        List<ReplenishmentRequest> requests = textDB.replenishmentRequests;
        if (requests.isEmpty()) {
            System.out.println("\nNo replenishment requests to process.");
            return;
        }

        viewAllReplenishmentRequests();

        System.out.print("Enter the number of the request to " + (isApprove ? "approve" : "reject") + " (or 0 to cancel): ");
        int choice = getIntInput(scanner) - 1;

        if (choice == -1 || choice >= requests.size()) {
            System.out.println("Operation cancelled or invalid selection.");
            return;
        }

        ReplenishmentRequest selectedRequest = requests.get(choice);
        String medicationName = selectedRequest.getMedicationName();
        int quantity = selectedRequest.getQuantity();

        if (isApprove) {
            // Find the medication in inventory
            Medication medication = null;
            for (Medication med : textDB.getMedications()) {
                if (med.getName().equalsIgnoreCase(medicationName)) {
                    medication = med;
                    break;
                }
            }

            if (medication != null) {
                medication.setQuantity(medication.getQuantity() + quantity);
                textDB.updateMedication(medication);
                System.out.println("Replenishment request approved. Inventory updated.");
            } else {
                System.out.println("Medication not found in inventory. Cannot approve request.");
                return;
            }
        } else {
            System.out.println("Replenishment request rejected.");
        }

        // Remove the processed request
        requests.remove(choice);
        textDB.saveReplenishmentRequests("replenishment_requests.txt");
    }

    

    // ----------------------- 5. Logout ----------------------- //

    // Logout is handled in the main showMenu method

    // ----------------------- Utility Methods ----------------------- //

    // View admin profile
    private void viewProfile(Administrator admin) {
        System.out.println("\nAdministrator Profile:");
        System.out.println("ID: " + admin.getHospitalID());
        System.out.println("Name: " + admin.getName());
    }

    // Utility method to get integer input with validation
    private int getIntInput(Scanner scanner) {
        int input;
        while (true) {
            String line = scanner.nextLine();
            try {
                input = Integer.parseInt(line);
                break;
            } catch (NumberFormatException e) {
                System.out.print("Invalid input! Please enter a number: ");
            }
        }
        return input;
    }

    private int getPositiveInt(Scanner scanner, String string) {
        int input;
        System.out.println(string);
        while (true) {
            String line = scanner.nextLine();
            try {
                input = Integer.parseInt(line);
                break;
            } catch (NumberFormatException e) {
                System.out.print("Invalid input! Please enter a number: ");
            }
        }
        return input;
    }

    // Utility method to get non-empty string input
    private String getNonEmptyString(Scanner scanner, String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                break;
            }
            System.out.println("Input cannot be empty. Please try again.");
        }
        return input;
    }

    // Utility method to get valid date input
    private LocalDate getValidDate(Scanner scanner, String prompt) {
        LocalDate date;
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                date = LocalDate.parse(input, DATE_FORMATTER);
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please enter date in yyyy-MM-dd format.");
            }
        }
        return date;
    }

    // ----------------------- Additional Methods ----------------------- //

    // You can add methods like viewSystemLogs if needed
}
