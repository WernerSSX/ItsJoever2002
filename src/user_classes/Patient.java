package user_classes;
import items.ContactInformation;
import items.MedicalRecord;

import java.time.LocalDate;

public class Patient extends User {
    private MedicalRecord medicalRecord;
    public Patient(String hospitalID, String password, String name, LocalDate dateOfBirth, String gender, ContactInformation contactInformation) {
        super(hospitalID, password, name, dateOfBirth, gender, contactInformation);
        this.role = "Patient";
        this.medicalRecord = new MedicalRecord(password, name, dateOfBirth, gender, contactInformation);
    }

    @Override
    public void login() {
        // Implementation for patient login
    }

    @Override
    public String toString() {
        return "Patient [ID=" + getHospitalID() + ", Name=" + getName() + ", Email=" + 
        getContactInformation().getEmailAddress() + ", Phone=" + 
        getContactInformation().getPhoneNumber() + "]";
    }

    @Override
    public void changePassword() {
        // Implementation for changing patient password
    }

    @Override
    public void logout() {
        // Implementation for patient logout
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

    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }
}