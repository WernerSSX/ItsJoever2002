package user_classes;
import items.ContactInformation;
import java.time.LocalDate;

public class Pharmacist extends User {
    public Pharmacist(String hospitalID, String password, String name, LocalDate dateOfBirth, String gender) {
        super(hospitalID, password, name, dateOfBirth, gender);
        this.role = "Pharmacist";
    }

    @Override
    public void login() {
        // Implementation for pharmacist login
    }

    @Override
    public void changePassword() {
        // Implementation for changing pharmacist password
    }

    @Override
    public void logout() {
        // Implementation for pharmacist logout
    }

    @Override
    public String toString() {
        return "Pharmacist [ID=" + getHospitalID() + ", Name=" + getName();
    }

    public void viewAppointmentOutcomeRecords() {
        // Implementation for viewing appointment outcome records
    }

    public void updatePrescriptionStatus() {
        // Implementation for updating prescription status
    }

    public void viewMedicationInventory() {
        // Implementation for viewing medication inventory
    }

    public void submitReplenishmentRequest() {
        // Implementation for submitting replenishment request
    }
}