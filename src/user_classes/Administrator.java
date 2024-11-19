package user_classes;

import java.time.LocalDate;

/**
 * Administrator
 * Represents an administrator user with specific responsibilities within the hospital system.
 *
 * The Administrator class extends the User class and provides additional functionalities
 * such as managing hospital staff, viewing appointment details, managing medication inventory,
 * and approving replenishment requests.
 */
public class Administrator extends User {
    
    /**
     * Constructs an Administrator with specified details.
     * @param hospitalID The unique hospital ID of the administrator.
     * @param password The login password for the administrator.
     * @param name The name of the administrator.
     * @param dateOfBirth The birth date of the administrator.
     * @param gender The gender of the administrator.
     *
     * Initializes an Administrator instance, setting the role to "Administrator".
     */
    public Administrator(String hospitalID, String password, String name, LocalDate dateOfBirth, String gender) {
        super(hospitalID, password, name, dateOfBirth, gender);
        this.role = "Administrator";
    }

    /**
     * Provides a string representation of the Administrator.
     * @return A string containing the administrator's ID and name.
     */
    @Override
    public String toString() {
        return "Administrator [ID=" + getHospitalID() + ", Name=" + getName();
    }

    /**
     * Handles the login process for the administrator.
     *
     * This method contains the specific implementation for administrator login.
     */
    @Override
    public void login() {
        // Implementation for administrator login
    }

    /**
     * Allows the administrator to change their password.
     *
     * This method contains the specific implementation for password modification.
     */
    @Override
    public void changePassword() {
        // Implementation for changing administrator password
    }

    /**
     * Handles the logout process for the administrator.
     *
     * This method contains the specific implementation for administrator logout.
     */
    @Override
    public void logout() {
        // Implementation for administrator logout
    }

    /**
     * Manages hospital staff.
     *
     * This method contains the specific implementation for managing hospital staff.
     */
    public void manageHospitalStaff() {
        // Implementation for managing hospital staff
    }

    /**
     * Views detailed appointment information.
     *
     * This method contains the specific implementation for viewing appointment details.
     */
    public void viewAppointmentsDetails() {
        // Implementation for viewing appointment details
    }

    /**
     * Views and manages the medication inventory.
     *
     * This method contains the specific implementation for managing the medication inventory.
     */
    public void viewAndManageMedicationInventory() {
        // Implementation for viewing and managing medication inventory
    }

    /**
     * Approves replenishment requests for medications.
     *
     * This method contains the specific implementation for approving medication replenishment requests.
     */
    public void approveReplenishmentRequests() {
        // Implementation for approving replenishment requests
    }
}
