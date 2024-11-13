package user_classes;
import db.TextDB;
import items.ContactInformation;
import items.MedicalRecord;
import java.io.IOException;
import java.time.LocalDate;

/**
 * @class Patient
 * @brief Represents a patient in the hospital management system.
 * 
 * This class extends the User class and includes additional methods
 * and attributes specific to patients, such as managing their medical
 * record and requesting appointments.
 */
public class Patient extends User {
    private MedicalRecord medicalRecord; ///< The medical record associated with the patient

    /**
     * @brief Constructs a Patient object with the specified attributes.
     * 
     * Upon creation, this constructor checks if a medical record exists
     * for the patient in the database. If it exists, the record is loaded;
     * otherwise, a new record is created and saved.
     *
     * @param hospitalID  Unique identifier for the patient within the hospital
     * @param password    Password for authentication
     * @param name        Full name of the patient
     * @param dateOfBirth Date of birth
     * @param gender      Gender of the patient
     */
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

    /**
     * @brief Logs the patient into the system.
     */
    @Override
    public void login() {
        // Implementation for patient login
    }

    /**
     * @brief Provides a string representation of the Patient object.
     * @return String representation of the Patient
     */
    @Override
    public String toString() {
        return "Patient: ID=" + getHospitalID() + ", Name=" + getName();
    }

    /**
     * @brief Allows the patient to change their password.
     */
    @Override
    public void changePassword() {
        // Implementation for changing patient password
    }

    /**
     * @brief Logs the patient out of the system.
     */
    @Override
    public void logout() {
        // Implementation for patient logout
    }

    /**
     * @brief Requests an appointment for the patient.
     */
    public void requestAppointment() {
        // Implementation for requesting appointment
    }

    /**
     * @brief Views the patient's current prescriptions.
     */
    public void viewPrescriptions() {
        // Implementation for viewing prescriptions
    }

    /**
     * @brief Updates the contact information of the patient.
     */
    public void updateContactInformation() {
        // Implementation for updating contact information
    }

    /**
     * @brief Retrieves the patient's medical record.
     * @return The MedicalRecord object associated with the patient
     */
    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }
}
