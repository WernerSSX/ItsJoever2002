package items;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * The Treatment class represents a treatment plan, including prescribed medications, service type,
 * date of appointment, and comments.
 */
public class Treatment {
    private String serviceType;
    private LocalDate dateOfAppointment;
    private List<Prescription> allPrescribedMedicine;
    private String treatmentComments;
    private String doctorId;

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
     * @param serviceType        Type of service provided (e.g., consultation, X-ray)
     * @param dateOfAppointment  Date of the appointment
     * @param treatmentComments  Comments regarding the treatment
     */
    public Treatment(String serviceType, LocalDate dateOfAppointment, String treatmentComments, String doctorId) {
        this.serviceType = serviceType;
        this.dateOfAppointment = dateOfAppointment;
        this.allPrescribedMedicine = new ArrayList<>();
        this.treatmentComments = treatmentComments;
        this.doctorId = doctorId;
    }

    // Getters and Setters

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public LocalDate getDateOfAppointment() {
        return dateOfAppointment;
    }

    public void setDateOfAppointment(LocalDate dateOfAppointment) {
        this.dateOfAppointment = dateOfAppointment;
    }

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

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
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
     * Format:
     * serviceType;dateOfAppointment;med1:status1,med2:status2;treatmentComments;doctorId
     *
     * @return Serialized string representation of the Treatment
     */
    public String serialize() {
        StringBuilder sb = new StringBuilder();
        sb.append(serviceType != null ? serviceType : "NULL").append(";");
        sb.append(dateOfAppointment != null ? dateOfAppointment.format(DateTimeFormatter.ISO_LOCAL_DATE) : "NULL").append(";");
        
        // Serialize Prescribed Medications
        if (allPrescribedMedicine != null && !allPrescribedMedicine.isEmpty()) {
            String meds = String.join(",",
                    allPrescribedMedicine.stream()
                            .map(p -> p.getMedicationName() + ":" + p.getStatus())
                            .toArray(String[]::new));
            sb.append(meds);
        } else {
            sb.append("NULL");
        }
        sb.append(";");
        
        // Serialize Treatment Comments (escape semicolons)
        sb.append(treatmentComments != null ? treatmentComments.replace(";", "\\;") : "NULL").append(";");
        
        // Serialize Doctor ID
        sb.append(doctorId != null ? doctorId : "NULL");
        
        return sb.toString();
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
            throw new IllegalArgumentException("Invalid Treatment data: " + data);
        }

        String serviceType = parts[0].equals("NULL") ? null : parts[0];
        LocalDate dateOfAppointment = parts[1].equals("NULL") ? null : LocalDate.parse(parts[1], DateTimeFormatter.ISO_LOCAL_DATE);
        String medsPart = parts[2];
        String treatmentComments = parts[3].equals("NULL") ? "" : parts[3].replace("\\;", ";");
        String doctorId = parts[4].equals("NULL") ? null : parts[4];

        Treatment treatment = new Treatment();
        treatment.setServiceType(serviceType);
        treatment.setDateOfAppointment(dateOfAppointment);
        treatment.setTreatmentComments(treatmentComments);
        treatment.setDoctorId(doctorId);

        if (!medsPart.equals("NULL") && !medsPart.trim().isEmpty()) {
            String[] meds = medsPart.split(",");
            for (String med : meds) {
                String[] medParts = med.split(":");
                if (medParts.length == 2) {
                    treatment.addPrescription(new Prescription(medParts[0], medParts[1]));
                }
            }
        }

        return treatment;
    }


    /**
     * Displays the Treatment details including Doctor ID.
     */
    public void display() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.println("  ------------------------------------------------");
        System.out.println("    Service Type        : " + (serviceType != null ? serviceType : "N/A"));
        System.out.println("    Date of Appointment  : " + (dateOfAppointment != null ? dateOfAppointment.format(dateFormatter) : "N/A"));
        
        // Prescribed Medications
        System.out.println("    Prescribed Medications:");
        if (allPrescribedMedicine == null || allPrescribedMedicine.isEmpty()) {
            System.out.println("      - NULL");
        } else {
            for (Prescription presc : allPrescribedMedicine) {
                System.out.println("      - " + presc.getMedicationName() + " | Status: " + presc.getStatus());
            }
        }
        
        // Treatment Comments
        System.out.println("    Consultation Notes   : " + (treatmentComments != null && !treatmentComments.trim().isEmpty() ? treatmentComments : "None"));
        
        // Display Doctor ID
        System.out.println("    Doctor ID            : " + (doctorId != null ? doctorId : "N/A"));
        System.out.println("  ------------------------------------------------");
    }

    
    
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
