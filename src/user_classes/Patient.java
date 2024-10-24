package user_classes;
import items.ContactInformation;
import items.MedicalRecord;

import java.time.LocalDate;
import java.io.IOException;

import db.TextDB;

public class Patient extends User {
    private MedicalRecord medicalRecord;
    public Patient(String hospitalID, String password, String name, LocalDate dateOfBirth, String gender) {
        super(hospitalID, password, name, dateOfBirth, gender);
        this.role = "Patient";
        
        // Load existing medical record from the TextDB if it exists
        TextDB textDB = TextDB.getInstance();
        MedicalRecord existingRecord = textDB.getMedicalRecordByPatientId(hospitalID);

        if (existingRecord != null) {
            this.medicalRecord = existingRecord;
        } else {
            // If no record exists, create a new medical record without contact information
            this.medicalRecord = new MedicalRecord(hospitalID, name, dateOfBirth, gender, new ContactInformation("", ""));
            try {
                // Save the new record to the database
                textDB.addMedicalRecord(this.medicalRecord);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void login() {
        // Implementation for patient login
    }

    @Override
    public String toString() {
        return "Patient: ID=" + getHospitalID() + ", Name=" + getName();
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