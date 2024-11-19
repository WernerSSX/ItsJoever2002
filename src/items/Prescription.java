package items;

/**
 * Prescription
 * Represents a medication prescribed to a patient with a specified status.
 */
public class Prescription {
    private String medicationName; /**< Name of the prescribed medication */
    private String status; /**< Status of the prescription, e.g., Pending, Approved */

    /****************
     * Constructors *
     ****************/

    /**
     * Constructs a Prescription with a given medication name and status.
     *
     * @param medicationName Name of the medication prescribed.
     * @param status         Status of the prescription; if empty, defaults to "Pending".
     */
    public Prescription(String medicationName, String status) {
        this.medicationName = medicationName;
        this.status = status.isEmpty() ? "Pending" : status;
    }

    /***********************
     * Getters and Setters *
     ***********************/

    /**
     * Gets the name of the prescribed medication.
     * @return The medication name.
     */
    public String getMedicationName() {
        return medicationName;
    }

    /**
     * Sets the name of the prescribed medication.
     * @param medicationName The new medication name.
     */
    public void setMedicationName(String medicationName) { 
        this.medicationName = medicationName;
    }

    /**
     * Gets the status of the prescription.
     * @return The status of the prescription (e.g., Pending, Approved).
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status of the prescription.
     * @param status The new status for the prescription.
     */
    public void setStatus(String status) { 
        this.status = status;
    }

    /**********
     * Methods *
     **********/

    /**
     * Provides a string representation of the prescription.
     * @return A string containing the medication name and status.
     */
    @Override
    public String toString() {
        return "Prescription{" +
                "medicationName='" + medicationName + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
