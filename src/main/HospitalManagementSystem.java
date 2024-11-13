package main;

import db.TextDB;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import menus.*;
import user_classes.*;

/**
 * @brief Entry point for the Hospital Management System application.
 */
public class HospitalManagementSystem {
	private static final String DEFAULT_PASSWORD = "password";
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        TextDB textDB = TextDB.getInstance();

        boolean systemRunning = true;

        // Main system loop
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

            // Handle user choice
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
                        TextDB.saveToFile("users.txt");
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
    private static boolean isDefaultPassword(String hospitalID, String passwordHash) {
        return passwordHash.equals(hashPassword(hospitalID, DEFAULT_PASSWORD));
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
    private static void updatePassword(Scanner scanner, User user) throws IOException {
        String newPassword;
        while (true) {
            System.out.print("Enter new password: ");
            newPassword = scanner.nextLine();
            System.out.print("Confirm new password: ");
            String confirmPassword = scanner.nextLine();
            if (newPassword.equals(confirmPassword)) {
                break;
            } else {
                System.out.println("Passwords do not match. Try again.");
            }
        }

        // Update user's password hash
        String newHashedPassword = hashPassword(user.getHospitalID(), newPassword);
        user.setPassword(newHashedPassword);
        System.out.println("Password updated successfully!");
    }
    /**
     * @brief Handles the login process for a specific role.
     * 
     * @param scanner   Scanner instance for input.
     * @param textDB    Database instance.
     * @param role      Role of the user attempting to log in.
     */
    private static void handleLogin(Scanner scanner, TextDB textDB, String role) throws IOException {
        System.out.print("Enter Hospital ID: ");
        String inputHospitalID = scanner.nextLine();
        System.out.print("Enter Password: ");
        String inputPass = scanner.nextLine();

        User user = getUserByRoleAndID(textDB.getUsers(), role, inputHospitalID);
        
        if (user != null) {
            String hashedInputPassword = hashPassword(inputHospitalID, inputPass);

            if (user.getPassword().equals(hashedInputPassword)) {
                System.out.println(role + " logged in successfully!");

                // Check if the password is the default
                if (isDefaultPassword(inputHospitalID, hashedInputPassword)) {
                    System.out.println("Your password is the default. Please change your password.");
                    updatePassword(scanner, user);
                }

                navigateToMenu(scanner, user, textDB);
            } else {
                System.out.println("Invalid Hospital ID or Password!");
            }
        } else {
            System.out.println("Invalid Hospital ID or Password!");
        }
        /*
        if (user != null && user.getPassword().equals(inputPass)) {
            System.out.println(role + " logged in successfully!");
            navigateToMenu(scanner, user, textDB);
        } else {
            System.out.println("Invalid Hospital ID or Password!");
        }
        */
    }

    /**
     * @brief Retrieves a user by role and hospital ID.
     * 
     * @param userList    List of all users in the system.
     * @param role        Role of the user to find.
     * @param hospitalID  Hospital ID of the user to find.
     * @return            User object if a match is found, null otherwise.
     */
    private static User getUserByRoleAndID(List<User> userList, String role, String hospitalID) {
        for (User user : userList) {
            if (user.getClass().getSimpleName().equalsIgnoreCase(role) &&
                user.getHospitalID().equalsIgnoreCase(hospitalID)) {
                return user; // Return the matching user
            }
        }
        return null; // No matching user found
    }

    /**
     * @brief Navigates to the appropriate menu based on the user's role.
     * 
     * @param scanner Scanner instance for input.
     * @param user    User who has logged in.
     * @param textDB  Database instance.
     */
    private static void navigateToMenu(Scanner scanner, User user, TextDB textDB) {
        if (user instanceof Administrator) {
            AdministratorMenu adminMenu = new AdministratorMenu(textDB);
            try {
                adminMenu.showMenu(scanner, (Administrator) user);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (user instanceof Doctor) {
            DoctorMenu doctorMenu = new DoctorMenu(textDB);
            try {
                doctorMenu.showMenu(scanner, (Doctor) user);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (user instanceof Pharmacist) {
            PharmacistMenu pharmacistMenu = new PharmacistMenu(textDB);
            try {
                pharmacistMenu.showMenu(scanner, (Pharmacist) user);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (user instanceof Patient) {
            PatientMenu patientMenu = new PatientMenu(textDB);
            try {
                patientMenu.showMenu(scanner, (Patient) user);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Unknown role. Access denied.");
        }
    }
}
