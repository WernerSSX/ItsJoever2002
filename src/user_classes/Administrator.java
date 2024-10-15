package user_classes;

import items.ContactInformation;
import java.time.LocalDate;

public class Administrator extends User {

    public Administrator(String hospitalID, String password, String name, LocalDate dateOfBirth, String gender, ContactInformation contactInformation) {
        super(hospitalID, password, name, dateOfBirth, gender, contactInformation);
    }

    @Override
    public void login() {
        // Implementation for administrator login
    }

    @Override
    public void changePassword() {
        // Implementation for changing administrator password
    }

    @Override
    public void logout() {
        // Implementation for administrator logout
    }

    public void manageHospitalStaff() {
        // Implementation for managing hospital staff
    }

    public void viewAppointmentsDetails() {
        // Implementation for viewing appointment details
    }

    public void viewAndManageMedicationInventory() {
        // Implementation for viewing and managing medication inventory
    }

    public void approveReplenishmentRequests() {
        // Implementation for approving replenishment requests
    }
}
