package main;

import java.io.IOException;
import java.util.Scanner;
import user_classes.*;
import java.util.List;
import db.TextDB;
import menus.*;

public class HospitalManagementSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String userFile = "users.txt"; // Text file containing user information

        // Instantiate TextDB to manage users
        TextDB textDB = new TextDB();

        // Load users from the file
        try {
            textDB.loadFromFile(userFile);
        } catch (IOException e) {
            System.out.println("Error reading user file: " + e.getMessage());
            // Optionally, exit the program if users cannot be loaded
            System.exit(1);
        }

        boolean systemRunning = true;

        // CLI loop
        while (systemRunning) {
            System.out.println("\n=== Welcome to the Hospital Management System ===");
            System.out.println("1. Administrator Login");
            System.out.println("2. Doctor Login");
            System.out.println("3. Pharmacist Login");
            System.out.println("4. Patient Login");
            System.out.println("5. Save Changes");
            System.out.println("6. Exit");
            System.out.print("Please select your role (1-6): ");

            String input = scanner.nextLine();
            int choice;

            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number between 1 and 6.");
                continue;
            }

            switch (choice) {
                case 1:
                    // Administrator Login
                    handleLogin(scanner, textDB, "Administrator");
                    break;
                case 2:
                    // Doctor Login
                    handleLogin(scanner, textDB, "Doctor");
                    break;
                case 3:
                    // Pharmacist Login
                    handleLogin(scanner, textDB, "Pharmacist");
                    break;
                case 4:
                    // Patient Login
                    handleLogin(scanner, textDB, "Patient");
                    break;
                case 5:
                    // Save Changes
                    try {
                        textDB.saveToFile(userFile);
                        System.out.println("Changes saved successfully!");
                    } catch (IOException e) {
                        System.out.println("Error saving user file: " + e.getMessage());
                    }
                    break;
                case 6:
                    // Exit
                    System.out.println("Exiting system...");
                    systemRunning = false;
                    break;
                default:
                    System.out.println("Invalid choice! Please select a number between 1 and 6.");
                    break;
            }
        }

        scanner.close();
    }

    private static void handleLogin(Scanner scanner, TextDB textDB, String role) {
        System.out.print("Enter Hospital ID: ");
        String inputHospitalID = scanner.nextLine();
        System.out.print("Enter Password: ");
        String inputPass = scanner.nextLine();

        User user = getUserByRoleAndID(textDB.getUsers(), role, inputHospitalID);

        if (user != null && user.getPassword().equals(inputPass)) {
            System.out.println(role + " logged in successfully!");
            navigateToMenu(scanner, user, textDB);
        } else {
            System.out.println("Invalid Hospital ID or Password!");
        }
    }

    private static User getUserByRoleAndID(List<User> userList, String role, String hospitalID) {
        for (User user : userList) {
            if (user.getClass().getSimpleName().equalsIgnoreCase(role) &&
                user.getHospitalID().equalsIgnoreCase(hospitalID)) {
                return user; // Return the matching user
            }
        }
        return null; // No matching user found
    }

    private static void navigateToMenu(Scanner scanner, User user, TextDB textDB) {
        // Implement role-specific menus
        if (user instanceof Administrator) {
            AdministratorMenu adminMenu = new AdministratorMenu(textDB);
            adminMenu.showMenu(scanner, (Administrator) user);
        } else if (user instanceof Doctor) {
            // DoctorMenu doctorMenu = new DoctorMenu(textDB);
            // doctorMenu.showMenu(scanner, (Doctor) user);
        } else if (user instanceof Pharmacist) {
            // PharmacistMenu pharmacistMenu = new PharmacistMenu(textDB);
            // pharmacistMenu.showMenu(scanner, (Pharmacist) user);
        } else if (user instanceof Patient) {
            // PatientMenu patientMenu = new PatientMenu(textDB);
            // patientMenu.showMenu(scanner, (Patient) user);
        } else {
            System.out.println("Unknown role. Access denied.");
        }
    }
}
