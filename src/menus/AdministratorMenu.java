package menus;

import java.io.IOException;
import user_classes.Administrator;
import user_classes.Doctor;
import user_classes.Pharmacist;
import user_classes.Patient;
import user_classes.User;
import db.TextDB;
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

    public void showMenu(Scanner scanner, Administrator admin) {
        boolean exit = false;

        while (!exit) {
            System.out.println("\nAdministrator Menu:");
            System.out.println("1. View Profile");
            System.out.println("2. Manage Users");
            System.out.println("3. View System Logs");
            System.out.println("4. Log out");
            System.out.print("Enter your choice: ");
            
            int choice = getIntInput(scanner);

            switch (choice) {
                case 1:
                    viewProfile(admin);
                    break;
                case 2:
                    manageUsers(scanner);
                    break;
                case 3:
                    viewSystemLogs();
                    break;
                case 4:
                    exit = true;
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // View admin profile
    private void viewProfile(Administrator admin) {
        System.out.println("\nAdministrator Profile:");
        System.out.println("ID: " + admin.getHospitalID());
        System.out.println("Name: " + admin.getName());
        System.out.println("Email: " + admin.getContactInformation().getEmailAddress());
        System.out.println("Phone: " + admin.getContactInformation().getPhoneNumber());
    }

    // Manage users
    private void manageUsers(Scanner scanner) {
        boolean back = false;

        while (!back) {
            System.out.println("\nManage Users:");
            System.out.println("1. Add User");
            System.out.println("2. Remove User");
            System.out.println("3. View All Users");
            System.out.println("4. Back to Menu");
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
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Add a new user
    private void addUser(Scanner scanner) {
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
        String gender = getNonEmptyString(scanner, "Enter Gender: ");
        String email = getNonEmptyString(scanner, "Enter Email: ");
        String phone = getNonEmptyString(scanner, "Enter Phone Number: ");
        String password = getNonEmptyString(scanner, "Enter Password: ");

        User newUser = null;

        switch (role) {
            case "administrator":
                newUser = new Administrator(hospitalID, password, name, dateOfBirth, gender, 
                                            new ContactInformation(email, phone));
                break;
            case "doctor":
                newUser = new Doctor(hospitalID, password, name, dateOfBirth, gender, 
                                     new ContactInformation(email, phone), new Schedule(null, null));
                break;
            case "pharmacist":
                newUser = new Pharmacist(hospitalID, password, name, dateOfBirth, gender, 
                                         new ContactInformation(email, phone));
                break;
            case "patient":
                newUser = new Patient(hospitalID, password, name, dateOfBirth, gender, 
                                      new ContactInformation(email, phone));
                break;
            default:
                System.out.println("Invalid role. User not added.");
                return;
        }

        textDB.addUser(newUser);
        System.out.println("User added successfully!");

        // Optionally, save immediately after adding
        try {
            textDB.saveToFile("users.txt");
            System.out.println("Changes saved to file.");
        } catch (IOException e) {
            System.out.println("Error saving to file: " + e.getMessage());
        }
    }

    // Remove an existing user
    private void removeUser(Scanner scanner) {
        System.out.println("\nRemove User:");
        String hospitalID = getNonEmptyString(scanner, "Enter Hospital ID of user to remove: ");

        User userToRemove = textDB.getUserByHospitalID(hospitalID);

        if (userToRemove != null) {
            textDB.removeUser(userToRemove);
            System.out.println("User removed successfully!");

            // Save changes after removal
            try {
                textDB.saveToFile("users.txt");
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
        }
    }

    // View system logs (placeholder)
    private void viewSystemLogs() {
        System.out.println("\nSystem Logs (Functionality to be implemented)");
        // You can add functionality to display system logs
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
}
