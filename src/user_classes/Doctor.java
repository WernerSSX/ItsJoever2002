package user_classes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import db.TextDB;

import java.io.IOException;
import items.*;

/**
 * The Doctor class represents a doctor in the hospital management system.
 * It extends the User class and includes additional attributes and methods
 * specific to doctors, such as managing patient lists, schedules, and availability.
 */
public class Doctor extends User {
    private List<String> patientIds; // List of patient hospital IDs assigned to the doctor
    private Schedule schedule;       // Doctor's schedule for managing availability

    /**
     * Constructor for the Doctor class.
     *
     * @param hospitalID        Unique identifier for the doctor within the hospital
     * @param password          Password for authentication
     * @param name              Full name of the doctor
     * @param dateOfBirth       Date of birth
     * @param gender            Gender of the doctor
     * @param schedule          Initial schedule of the doctor
     */
    public Doctor(String hospitalID, String password, String name, LocalDate dateOfBirth,
                  String gender, Schedule schedule) {
        super(hospitalID, password, name, dateOfBirth, gender);
        this.patientIds = new ArrayList<>();
        this.schedule = schedule != null ? schedule : new Schedule();
        this.role = "Doctor";
    }

    /**
     * Constructor used during deserialization when patient IDs are known.
     *
     * @param hospitalID         Unique identifier for the doctor within the hospital
     * @param password           Password for authentication
     * @param name               Full name of the doctor
     * @param dateOfBirth        Date of birth
     * @param gender             Gender of the doctor
     * @param contactInformation Contact information (phone number, email)
     * @param schedule           Initial schedule of the doctor
     * @param patientIds         List of patient hospital IDs assigned to the doctor
     */
    public Doctor(String hospitalID, String password, String name, LocalDate dateOfBirth,
                  String gender, Schedule schedule,
                  List<String> patientIds) {
        super(hospitalID, password, name, dateOfBirth, gender);
        this.schedule = schedule != null ? schedule : new Schedule();
        this.patientIds = patientIds != null ? patientIds : new ArrayList<>();
    }

    /**
     * Retrieves the list of patient hospital IDs assigned to the doctor.
     *
     * @return List of patient IDs
     */
    public List<String> getPatients() {
        return patientIds;
    }

    /**
     * Adds a patient to the doctor's list of assigned patients.
     *
     * @param patientId Hospital ID of the patient to add
     */
    public void addPatient(String patientId) {
        if (!patientIds.contains(patientId)) {
            patientIds.add(patientId);
        }
    }

    /**
     * Removes a patient from the doctor's list of assigned patients.
     *
     * @param patientId Hospital ID of the patient to remove
     */
    public void removePatient(String patientId) {
        patientIds.remove(patientId);
    }

    /**
     * Retrieves the doctor's schedule.
     *
     * @return Schedule object representing the doctor's availability
     */
    public Schedule getSchedule() {
        return schedule;
    }

    /**
     * Sets the doctor's schedule.
     *
     * @param schedule Schedule object to set
     */
    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    
    /**
     * Retrieves the MedicalRecord of a specific patient assigned to the doctor.
     *
     * @param patientId Hospital ID of the patient
     * @return MedicalRecord object if found, else null
     */
    public MedicalRecord getPatientMedicalRecord(String patientId) {
        if (!patientIds.contains(patientId)) {
            return null;
        }
        return TextDB.getInstance().getMedicalRecordByPatientId(patientId);
    }

    /**
     * Updates the MedicalRecord of a specific patient.
     *
     * @param patientId     Hospital ID of the patient
     * @param updatedRecord The updated MedicalRecord object
     * @throws IOException If an I/O error occurs during the update
     */
    public void updatePatientMedicalRecord(String patientId, MedicalRecord updatedRecord) throws IOException {
        if (!patientIds.contains(patientId)) {
            throw new IllegalArgumentException("Patient not assigned to this doctor.");
        }
        TextDB.getInstance().updateMedicalRecord(updatedRecord);
    }

    /**
     * Sets the doctor's availability for a specific date.
     *
     * @param date        The date for which to set availability
     * @param availability List of TimeSlot objects representing available times
     * @throws IOException If an I/O error occurs during the update
     */
    public void setAvailability(LocalDate date, List<TimeSlot> availability) throws IOException {
        schedule.setAvailability(date, availability);
        // Update the schedule in TextDB
        TextDB.getInstance().updateDoctorSchedule(this.hospitalID, this.schedule);
    }

    /**
     * Retrieves the doctor's available TimeSlots for a specific date.
     *
     * @param date The date for which to retrieve availability
     * @return List of available TimeSlot objects
     */
    public List<TimeSlot> getAvailableTimeSlots(LocalDate date) {
        return schedule.getAvailableTimeSlots(date);
    }

    /**
     * Overrides the toString method to provide a string representation of the Doctor.
     *
     * @return String representation of the Doctor
     */
    @Override
    public String toString() {
        return "Doctor: " +
                "hospitalID='" + hospitalID + '\'' +
                ", name='" + name + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", gender='" + gender + '\'' +
                ", patientIds=" + patientIds +
                ", schedule=" + schedule;
    }

    @Override
    public void login() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'login'");
    }

    @Override
    public void changePassword() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'changePassword'");
    }

    @Override
    public void logout() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'logout'");
    }

    // Additional methods can be added here as needed for extended functionalities
}
