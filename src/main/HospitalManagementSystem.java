package main;

import db.TextDB;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import menus.*;
import user_classes.*;

public class HospitalManagementSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TextDB textDB = TextDB.getInstance();

        boolean systemRunning = true;

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

    private static void handleLogin(Scanner scanner, TextDB textDB, String role) {
        System.out.print("Enter Hospital ID: ");
        String inputHospitalID = scanner.nextLine();
        // POSSIBLE UPDATE: Include Error Handling and minimal requirements for Hospital ID
        System.out.print("Enter Password: ");
        String inputPass = scanner.nextLine();
        // POSSIBLE UPDATE: Include Error Handling and minimal requirements for Password
        // POSSIBLE UPDATE: Include "Forget Password" Feature

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
