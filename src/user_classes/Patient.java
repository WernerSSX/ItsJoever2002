package user_classes;
import items.ContactInformation;
import java.time.LocalDate;

public class Patient extends User {
    public Patient(String hospitalID, String password, String name, LocalDate dateOfBirth, String gender, ContactInformation contactInformation) {
        super(hospitalID, password, name, dateOfBirth, gender, contactInformation);
        this.role = "Patient";
    }

    @Override
    public void login() {
        // Implementation for patient login
    }

    @Override
    public void changePassword() {
        // Implementation for changing patient password
    }

    @Override
    public void logout() {
        // Implementation for patient logout
    }

    public void viewMedicalRecords() {
        // Implementation for viewing medical records
    }

    public void requestAppointment() {
        // Implementation for requesting appointment
    }

    public void viewPrescriptions() {
        // Implementation for viewing prescriptions
    }

    public void updateContactInformation() {
        // Implementation for updating contact information
    }
}