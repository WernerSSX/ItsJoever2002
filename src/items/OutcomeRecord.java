package items;

import java.time.LocalDate;
import java.util.List;

/**
 * OutcomeRecord
 * Represents the outcome details of a medical appointment, including date, type of service, prescribed medications, and consultation notes.
 */
public class OutcomeRecord {
    private LocalDate dateOfAppointment; /**< Date when the appointment took place */
    private String serviceType; /**< Type of service provided, e.g., Consultation, X-ray */
    private List<Prescription> prescribedMedications; /**< List of prescribed medications during the appointment */
    private String consultationNotes; /**< Notes from the consultation */

    /****************
     * Constructors *
     ****************/

    /**
     * Constructs an OutcomeRecord with specified details.
     *
     * @param dateOfAppointment    Date when the appointment took place.
     * @param serviceType          Type of service provided, such as consultation or procedure.
     * @param prescribedMedications List of medications prescribed during the appointment.
     * @param consultationNotes    Notes from the consultation.
     */
    public OutcomeRecord(LocalDate dateOfAppointment, String serviceType,
                        List<Prescription> prescribedMedications, String consultationNotes) {
        this.dateOfAppointment = dateOfAppointment;
        this.serviceType = serviceType;
        this.prescribedMedications = prescribedMedications;
        this.consultationNotes = consultationNotes;
    }

    /***********************
     * Getters and Setters *
     ***********************/

    /**
     * Gets the date of the appointment.
     * @return Date when the appointment took place.
     */
    public LocalDate getDateOfAppointment() {
        return dateOfAppointment;
    }

    /**
     * Sets the date of the appointment.
     * @param dateOfAppointment The new date of the appointment.
     */
    public void setDateOfAppointment(LocalDate dateOfAppointment) { 
        this.dateOfAppointment = dateOfAppointment;
    }

    /**
     * Gets the type of service provided during the appointment.
     * @return Type of service, e.g., Consultation, X-ray.
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     * Sets the type of service provided.
     * @param serviceType New type of service provided during the appointment.
     */
    public void setServiceType(String serviceType) { 
        this.serviceType = serviceType;
    }

    /**
     * Gets the list of prescribed medications.
     * @return List of medications prescribed during the appointment.
     */
    public List<Prescription> getPrescribedMedications() {
        return prescribedMedications;
    }

    /**
     * Sets the list of prescribed medications.
     * @param prescribedMedications New list of medications prescribed.
     */
    public void setPrescribedMedications(List<Prescription> prescribedMedications) { 
        this.prescribedMedications = prescribedMedications;
    }

    /**
     * Gets the consultation notes.
     * @return Notes from the consultation.
     */
    public String getConsultationNotes() {
        return consultationNotes;
    }

    /**
     * Sets the consultation notes.
     * @param consultationNotes New notes from the consultation.
     */
    public void setConsultationNotes(String consultationNotes) { 
        this.consultationNotes = consultationNotes;
    }

    /**********
     * Methods *
     **********/

    /**
     * Provides a string representation of the outcome record.
     * @return A string containing the date, service type, prescribed medications, and consultation notes.
     */
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
