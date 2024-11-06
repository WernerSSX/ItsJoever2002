package user_classes;
import java.time.LocalDate;

/**
 * @class Pharmacist
 * @brief Represents a pharmacist in the hospital management system.
 * 
 * This class extends the User class and includes additional methods
 * specific to the role of a pharmacist, such as managing prescriptions,
 * viewing appointment outcomes, and handling medication inventory.
 */
public class Pharmacist extends User {

    /**
     * @brief Constructs a Pharmacist object with the specified attributes.
     *
     * @param hospitalID  Unique identifier for the pharmacist within the hospital
     * @param password    Password for authentication
     * @param name        Full name of the pharmacist
     * @param dateOfBirth Date of birth
     * @param gender      Gender of the pharmacist
     */
    public Pharmacist(String hospitalID, String password, String name, LocalDate dateOfBirth, String gender) {
        super(hospitalID, password, name, dateOfBirth, gender);
        this.role = "Pharmacist";
    }

    /**
     * @brief Logs the pharmacist into the system.
     */
    @Override
    public void login() {
        // Implementation for pharmacist login
    }

    /**
     * @brief Allows the pharmacist to change their password.
     */
    @Override
    public void changePassword() {
        // Implementation for changing pharmacist password
    }

    /**
     * @brief Logs the pharmacist out of the system.
     */
    @Override
    public void logout() {
        // Implementation for pharmacist logout
    }

    /**
     * @brief Provides a string representation of the Pharmacist object.
     * @return String representation of the Pharmacist
     */
    @Override
    public String toString() {
        return "Pharmacist [ID=" + getHospitalID() + ", Name=" + getName();
    }

    /**
     * @brief Views appointment outcome records related to pharmacy tasks.
     */
    public void viewAppointmentOutcomeRecords() {
        // Implementation for viewing appointment outcome records
    }

    /**
     * @brief Updates the status of a patient's prescription.
     */
    public void updatePrescriptionStatus() {
        // Implementation for updating prescription status
    }

    /**
     * @brief Views the current inventory of medications.
     */
    public void viewMedicationInventory() {
        // Implementation for viewing medication inventory
    }

    /**
     * @brief Submits a request to replenish the medication inventory.
     */
    public void submitReplenishmentRequest() {
        // Implementation for submitting replenishment request
    }
}
