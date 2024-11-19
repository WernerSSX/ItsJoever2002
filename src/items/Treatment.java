package items;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Treatment
 * The Treatment class represents a treatment plan, including prescribed medications, service type,
 * date of appointment, and comments.
 */
public class Treatment {
    private String serviceType;                  /**< Type of service provided (e.g., consultation, X-ray) */
    private LocalDate dateOfAppointment;         /**< Date of the appointment */
    private List<Prescription> allPrescribedMedicine; /**< List of all prescribed medications */
    private String treatmentComments;             /**< Comments regarding the treatment */
    private String doctorId;                      /**< ID of the doctor providing the treatment */

    /****************
     * Constructors *
     ****************/

    /**
     * Default constructor initializes the list of prescribed medicines and sets comments to an empty string.
     */
    public Treatment() {
        this.allPrescribedMedicine = new ArrayList<>();
        this.treatmentComments = "";
    }

    /**
     * Constructor with parameters.
     *
     * @param serviceType        Type of service provided (e.g., consultation, X-ray)
     * @param dateOfAppointment  Date of the appointment
     * @param treatmentComments  Comments regarding the treatment
     * @param doctorId          ID of the doctor providing the treatment
     */
    public Treatment(String serviceType, LocalDate dateOfAppointment, String treatmentComments, String doctorId) {
        this.serviceType = serviceType;
        this.dateOfAppointment = dateOfAppointment;
        this.allPrescribedMedicine = new ArrayList<>();
        this.treatmentComments = treatmentComments;
        this.doctorId = doctorId;
    }

    /***********************
     * Getters and Setters *
     ***********************/
    /**
     * Gets the service type of the treatment
     * @return Returns the service type of the treatment
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     * Sets the service type of the treatment
     * @param serviceType Service type of the treatment
     */
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    /**
     * Gets the date of appointment of treatment
     * @return Returns the date of appointment of treatment
     */
    public LocalDate getDateOfAppointment() {
        return dateOfAppointment;
    }

    /**
     * Sets the date of appointment of treatment
     * @param dateOfAppointment Date of appointment of treatment
     */
    public void setDateOfAppointment(LocalDate dateOfAppointment) {
        this.dateOfAppointment = dateOfAppointment;
    }

    /**
     * Gets a list of all prescribed medicine. Look at Prescription class
     * @return Returns a list of prescribed medicine
     */
    public List<Prescription> getAllPrescribedMedicine() {
        return allPrescribedMedicine;
    }

    /**
     * Adds a prescription to the list of prescribed medicines.
     * @param prescription The Prescription object to be added
     */
    public void addPrescription(Prescription prescription) {
        this.allPrescribedMedicine.add(prescription);
    }

    /**
     * Gets the comments of the treatment
     * @return Returns the comments of the treatment
     */
    public String getTreatmentComments() {
        return treatmentComments;
    }

    /**
     * Sets the comments of the treatment
     * @param treatmentComments Comments of the treatment
     */
    public void setTreatmentComments(String treatmentComments) {
        this.treatmentComments = treatmentComments;
    }

    /**
     * Gets the id of the doctor doing the treatment
     * @return Returns the id of the doctor doing the treatment
     */
    public String getDoctorId() {
        return doctorId;
    }

    /**
     * Sets the id of the doctor doing the treatment
     * @param doctorId id of the doctor doing the treatment
     */
    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    /**********
     * Methods *
     **********/

    /**
     * Prints all prescribed medicines with their details.
     */
    public void printAllPrescribedMedicine() {
        if (allPrescribedMedicine.isEmpty()) {
            System.out.println("No medications prescribed."); // Inform if no medications are prescribed
            return;
        }
        System.out.println("Prescribed Medications:");
        for (Prescription p : allPrescribedMedicine) {
            System.out.println("- " + p.getMedicationName() + " | Status: " + p.getStatus()); // Print each prescribed medication
        }
        System.out.println("-------------------------");
    }

    /**
     * Prints the treatment comments.
     */
    public void printTreatmentComments() {
        if (treatmentComments == null || treatmentComments.trim().isEmpty()) {
            System.out.println("No treatment comments."); // Inform if no comments are present
            return;
        }
        System.out.println("Treatment Comments: " + treatmentComments); // Print treatment comments
        System.out.println("-------------------------");
    }

    /**
     * Serializes the Treatment object into a string.
     *
     * Format:
     * serviceType;dateOfAppointment;med1:status1,med2:status2;treatmentComments;doctorId
     *
     * @return Serialized string representation of the Treatment
     */
    public String serialize() {
        StringBuilder sb = new StringBuilder();
        sb.append(serviceType != null ? serviceType : "NULL").append(";"); // Append service type
        sb.append(dateOfAppointment != null ? dateOfAppointment.format(DateTimeFormatter.ISO_LOCAL_DATE) : "NULL").append(";"); // Append date of appointment
        
        // Serialize Prescribed Medications
        if (allPrescribedMedicine != null && !allPrescribedMedicine.isEmpty()) {
            String meds = String.join(",",
                    allPrescribedMedicine.stream()
                            .map(p -> p.getMedicationName() + ":" + p.getStatus())
                            .toArray(String[]::new));
            sb.append(meds); // Append serialized medications
        } else {
            sb.append("NULL"); // Indicate no medications prescribed
        }
        sb.append(";");

        // Serialize Treatment Comments (escape semicolons)
        sb.append(treatmentComments != null ? treatmentComments.replace(";", "\\;") : "NULL").append(";");

        // Serialize Doctor ID
        sb.append(doctorId != null ? doctorId : "NULL"); // Append doctor's ID

        return sb.toString(); // Return the serialized string
    }

    /**
     * Deserializes a Treatment object from a string.
     *
     * Expected Format:
     * serviceType;dateOfAppointment;med1:status1,med2:status2;treatmentComments;doctorId
     *
     * @param data Serialized string representation of the Treatment
     * @return Treatment object
     */
    public static Treatment deserialize(String data) {
        String[] parts = data.split(";", -1); // -1 to include trailing empty strings
        if (parts.length != 5) {
            throw new IllegalArgumentException("Invalid Treatment data: " + data); // Validate the expected format
        }

        String serviceType = parts[0].equals("NULL") ? null : parts[0]; // Parse service type
        LocalDate dateOfAppointment = parts[1].equals("NULL") ? null : LocalDate.parse(parts[1], DateTimeFormatter.ISO_LOCAL_DATE); // Parse date of appointment
        String medsPart = parts[2]; // Store medications part
        String treatmentComments = parts[3].equals("NULL") ? "" : parts[3].replace("\\;", ";"); // Parse treatment comments
        String doctorId = parts[4].equals("NULL") ? null : parts[4]; // Parse doctor's ID

        // Create a new Treatment object and set its fields
        Treatment treatment = new Treatment();
        treatment.setServiceType(serviceType);
        treatment.setDateOfAppointment(dateOfAppointment);
        treatment.setTreatmentComments(treatmentComments);
        treatment.setDoctorId(doctorId);

        // Deserialize prescribed medications if present
        if (!medsPart.equals("NULL") && !medsPart.trim().isEmpty()) {
            String[] meds = medsPart.split(","); // Split medications
            for (String med : meds) {
                String[] medParts = med.split(":"); // Split medication name and status
                if (medParts.length == 2) {
                    treatment.addPrescription(new Prescription(medParts[0], medParts[1])); // Add each prescription
                }
            }
        }

        return treatment; // Return the deserialized Treatment object
    }

    /**
     * Displays the Treatment details including Doctor ID.
     */
    public void display() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.println("  ------------------------------------------------");
        System.out.println("    Service Type        : " + (serviceType != null ? serviceType : "N/A")); // Display service type
        System.out.println("    Date of Appointment  : " + (dateOfAppointment != null ? dateOfAppointment.format(dateFormatter) : "N/A")); // Display date of appointment
        
        // Prescribed Medications
        System.out.println("    Prescribed Medications:");
        if (allPrescribedMedicine == null || allPrescribedMedicine.isEmpty()) {
            System.out.println("      - NULL"); // Inform if no medications are prescribed
        } else {
            for (Prescription presc : allPrescribedMedicine) {
                System.out.println("      - " + presc.getMedicationName() + " | Status: " + presc.getStatus()); // Print each prescribed medication
            }
        }

        // Treatment Comments
        System.out.println("    Consultation Notes   : " + (treatmentComments != null && !treatmentComments.trim().isEmpty() ? treatmentComments : "None")); // Display comments
        
        // Display Doctor ID
        System.out.println("    Doctor ID            : " + (doctorId != null ? doctorId : "N/A")); // Display doctor ID
        System.out.println("  ------------------------------------------------");
    }

    /**
     * Returns the string representation of the Treatment object
     * @return Returns the string representation of the Treatment object
     */
    @Override
    public String toString() {
        return "Treatment{" +
                "serviceType='" + serviceType + '\'' +
                ", dateOfAppointment=" + dateOfAppointment +
                ", allPrescribedMedicine=" + allPrescribedMedicine +
                ", treatmentComments='" + treatmentComments + '\'' +
                '}';
    }
}
