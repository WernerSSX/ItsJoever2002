package items;

/**
 * The Prescription class represents a medication prescribed to a patient.
 */
public class Prescription {
    private String medicationName;
    private String status; // e.g., Pending, Approved, etc.

    /**
     * Constructor for Prescription.
     *
     * @param medicationName Name of the medication
     * @param status         Status of the prescription (default is "Pending")
     */
    public Prescription(String medicationName, String status) {
        this.medicationName = medicationName;
        this.status = status.isEmpty() ? "Pending" : status;
    }

    // Getters and Setters

    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String medicationName) { 
        this.medicationName = medicationName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) { 
        this.status = status;
    }

    @Override
    public String toString() {
        return "Prescription{" +
                "medicationName='" + medicationName + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
