package items;

import java.time.LocalDate;
import java.util.List;

/**
 * The OutcomeRecord class captures the outcome details of an appointment.
 */
public class OutcomeRecord {
    private LocalDate dateOfAppointment;
    private String serviceType;
    private List<Prescription> prescribedMedications;
    private String consultationNotes;

    /**
     * Constructor for OutcomeRecord.
     *
     * @param dateOfAppointment    Date when the appointment took place
     * @param serviceType          Type of service provided (e.g., Consultation, X-ray)
     * @param prescribedMedications List of prescribed medications
     * @param consultationNotes    Notes from the consultation
     */
    public OutcomeRecord(LocalDate dateOfAppointment, String serviceType,
                        List<Prescription> prescribedMedications, String consultationNotes) {
        this.dateOfAppointment = dateOfAppointment;
        this.serviceType = serviceType;
        this.prescribedMedications = prescribedMedications;
        this.consultationNotes = consultationNotes;
    }

    // Getters and Setters

    public LocalDate getDateOfAppointment() {
        return dateOfAppointment;
    }

    public void setDateOfAppointment(LocalDate dateOfAppointment) { 
        this.dateOfAppointment = dateOfAppointment;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) { 
        this.serviceType = serviceType;
    }

    public List<Prescription> getPrescribedMedications() {
        return prescribedMedications;
    }

    public void setPrescribedMedications(List<Prescription> prescribedMedications) { 
        this.prescribedMedications = prescribedMedications;
    }

    public String getConsultationNotes() {
        return consultationNotes;
    }

    public void setConsultationNotes(String consultationNotes) { 
        this.consultationNotes = consultationNotes;
    }

    @Override
    public String toString() {
        return "OutcomeRecord{" +
                "dateOfAppointment=" + dateOfAppointment +
                ", serviceType='" + serviceType + '\'' +
                ", prescribedMedications=" + prescribedMedications +
                ", consultationNotes='" + consultationNotes + '\'' +
                '}';
    }
}
