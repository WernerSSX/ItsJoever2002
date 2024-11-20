package items.appointments;

import items.appointments.appointments_interface.mainAppointmentsInterface;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Appointment
 * Represents an appointment for a patient with a doctor.
 *
 * The Appointment class stores information about a medical appointment, 
 * including the appointment ID, patient ID, doctor ID, time slot, status, 
 * and outcome record.
 */
public class Appointment implements mainAppointmentsInterface {
    private int id;               /**< Unique identifier for the appointment */ 
    private String patientId;     /**< Identifier for the patient */ 
    private String doctorId;      /**< Identifier for the doctor */ 
    private TimeSlot timeSlot;    /**< Time slot for the appointment */ 
    private String status;        /**< Current status of the appointment */ 
    private String outcomeRecord; /**< Record of the appointment outcome */ 

    /****************
     * Constructors *
     ****************/

    /**
     * Constructs an Appointment with the specified details.
     * 
     * @param id Unique identifier for the appointment.
     * @param patientId Identifier for the patient.
     * @param doctorId Identifier for the doctor.
     * @param timeSlot The time slot for the appointment.
     * @param status The current status of the appointment.
     * @param outcomeRecord Record of the appointment outcome.
     */
    public Appointment(int id, String patientId, String doctorId, TimeSlot timeSlot, String status, String outcomeRecord) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.timeSlot = timeSlot;
        this.status = status;
        this.outcomeRecord = outcomeRecord;
    }

    /***********************
     * Getters and Setters *
     ***********************/

    /**
     * Gets the unique identifier of the appointment.
     * @return The appointment ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the patient ID associated with the appointment.
     * @return The patient ID.
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     * Gets the doctor ID associated with the appointment.
     * @return The doctor ID.
     */
    public String getDoctorId() {
        return doctorId;
    }

    /**
     * Sets the doctor ID for the appointment.
     * @param doctorId The new doctor ID.
     */
    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    /**
     * Gets the time slot for the appointment.
     * @return The time slot.
     */
    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    /**
     * Sets the time slot for the appointment.
     * @param timeSlot The new time slot.
     */
    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    /**
     * Gets the current status of the appointment.
     * @return The status of the appointment.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status of the appointment.
     * @param status The new status.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets the outcome record of the appointment.
     * @return The outcome record.
     */
    public String getOutcomeRecord() {
        return outcomeRecord;
    }

    /**
     * Sets the outcome record of the appointment.
     * @param outcomeRecord The new outcome record.
     */
    public void setOutcomeRecord(String outcomeRecord) {
        this.outcomeRecord = outcomeRecord;
    }

    /**
     * Gets the date of the appointment.
     * @return The date of the appointment as a LocalDate.
     */
    public LocalDate getDate() {
        return timeSlot.getStartTime().toLocalDate();
    }

    /**
     * Checks if the appointment is in the past.
     * @return True if the appointment has ended, false otherwise.
     */
    public boolean isPast() {
        return timeSlot.getEndTime().isBefore(LocalDateTime.now());
    }

    /**********
     * Methods *
     **********/

    /**
     * Prints the details of the appointment.
     */
    public void print() {
        System.out.println("\nAppointment ID: " + id);
        System.out.println("Patient ID: " + patientId);
        System.out.println("Doctor ID: " + doctorId);
        System.out.println("Time Slot: " + timeSlot.toString());
        System.out.println("Status: " + status);
        System.out.println("Outcome Record: " + outcomeRecord);
    }
}
