    package menus;

    import user_classes.Administrator;
    import java.util.Scanner;

    public class AdministratorMenu {

        public void showMenu(Scanner scanner, Administrator admin) {
            boolean exit = false;

            while (!exit) {
                System.out.println("\nAdministrator Menu:");
                System.out.println("1. View Profile");
                System.out.println("2. Manage Users");
                System.out.println("3. View System Logs");
                System.out.println("4. Log out");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

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

        // Manage users (placeholder functionality)
        private void manageUsers(Scanner scanner) {
            System.out.println("\nManage Users (Functionality to be implemented)");
            // You can add options for adding/removing users, viewing user details, etc.
            System.out.println("1. Add User (Not implemented)");
            System.out.println("2. Remove User (Not implemented)");
            System.out.println("3. Back to Menu");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    System.out.println("Add User selected (Not implemented).");
                    break;
                case 2:
                    System.out.println("Remove User selected (Not implemented).");
                    break;
                case 3:
                    System.out.println("Returning to Administrator Menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        // View system logs (placeholder)
        private void viewSystemLogs() {
            System.out.println("\nSystem Logs (Functionality to be implemented)");
            // You can add functionality to display system logs
        }
    }
