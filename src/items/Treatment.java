package items;

import java.util.ArrayList;
import java.util.List;

/**
 * The Treatment class represents a treatment plan, including prescribed medications and comments.
 */
public class Treatment {
    private List<Prescription> allPrescribedMedicine;
    private String treatmentComments;

    /**
     * Default constructor initializes the list of prescribed medicines.
     */
    public Treatment() {
        this.allPrescribedMedicine = new ArrayList<>();
        this.treatmentComments = "";
    }

    /**
     * Constructor with parameters.
     *
     * @param treatmentComments Comments regarding the treatment
     */
    public Treatment(String treatmentComments) {
        this.allPrescribedMedicine = new ArrayList<>();
        this.treatmentComments = treatmentComments;
    }

    // Getters and Setters

    public List<Prescription> getAllPrescribedMedicine() {
        return allPrescribedMedicine;
    }

    public void addPrescription(Prescription prescription) {
        this.allPrescribedMedicine.add(prescription);
    }

    public String getTreatmentComments() {
        return treatmentComments;
    }

    public void setTreatmentComments(String treatmentComments) { 
        this.treatmentComments = treatmentComments;
    }

    /**
     * Prints all prescribed medicines with their details.
     */
    public void printAllPrescribedMedicine() {
        if (allPrescribedMedicine.isEmpty()) {
            System.out.println("No medications prescribed.");
            return;
        }
        System.out.println("Prescribed Medications:");
        for (Prescription p : allPrescribedMedicine) {
            System.out.println("- " + p.getMedicationName() + " | Status: " + p.getStatus());
        }
        System.out.println("-------------------------");
    }

    /**
     * Prints the treatment comments.
     */
    public void printTreatmentComments() {
        if (treatmentComments == null || treatmentComments.trim().isEmpty()) {
            System.out.println("No treatment comments.");
            return;
        }
        System.out.println("Treatment Comments: " + treatmentComments);
        System.out.println("-------------------------");
    }

    /**
     * Serializes the Treatment object into a string.
     *
     * @return Serialized string representation of the Treatment
     */
    public String serialize() {
        StringBuilder sb = new StringBuilder();
        sb.append(treatmentComments).append(";");
        for (Prescription p : allPrescribedMedicine) {
            sb.append(p.getMedicationName()).append(",").append(p.getStatus()).append("|");
        }
        // Remove the last "|" if exists
        if (sb.charAt(sb.length() - 1) == '|') {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * Deserializes a Treatment object from a string.
     *
     * @param data Serialized string representation of the Treatment
     * @return Treatment object
     */
    public static Treatment deserialize(String data) {
        String[] parts = data.split(";");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid Treatment data: " + data);
        }
        Treatment treatment = new Treatment(parts[0]);
        String[] prescriptions = parts[1].split("\\|");
        for (String presc : prescriptions) {
            String[] prescParts = presc.split(",");
            if (prescParts.length != 2) {
                continue; // Skip invalid entries
            }
            Prescription prescription = new Prescription(prescParts[0], prescParts[1]);
            treatment.addPrescription(prescription);
        }
        return treatment;
    }

    @Override
    public String toString() {
        return "Treatment{" +
                "allPrescribedMedicine=" + allPrescribedMedicine +
                ", treatmentComments='" + treatmentComments + '\'' +
                '}';
    }
}
