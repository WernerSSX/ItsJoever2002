package services;

import db.TextDB;
import items.Appointment;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import user_classes.Patient;

/**
 * AppointmentService
 * Manages appointment-related operations for patients.
 *
 * This service provides methods for retrieving past and current appointments
 * from the database, based on the patient's hospital ID and the date of the appointment.
 */
public class AppointmentService {
    private static TextDB textDB;

    /**
     * Constructor for AppointmentService.
     * @param textDB The instance of the TextDB database used for accessing appointments.
     *
     * Initializes the AppointmentService and sets up the connection to the
     * TextDB instance.
     */
    public AppointmentService(TextDB textDB) {
        this.textDB = TextDB.getInstance();
    }

    /**
     * Retrieves past appointments for a given patient.
     * @param patient The patient whose past appointments are to be retrieved.
     * @return A list of past appointments for the specified patient.
     *
     * This method filters appointments by the patient's hospital ID and ensures
     * that only past appointments (before the current date) are returned.
     */
    public static List<Appointment> getPastAppointments(Patient patient) {
        return textDB.getAppointments().stream()
                .filter(appointment -> appointment.getPatientId().equals(patient.getHospitalID()) && appointment.isPast())
                .collect(Collectors.toList());
    }

    /**
     * Retrieves current and upcoming appointments.
     * @return A list of appointments scheduled for today or future dates.
     *
     * This method filters appointments to include only those that occur on or
     * after the current date.
     */
    public List<Appointment> getCurrentAppointments() {
        List<Appointment> allAppointments = textDB.getAppointments();
        LocalDate today = LocalDate.now();
        return allAppointments.stream()
                .filter(appointment -> !appointment.getDate().isBefore(today))
                .collect(Collectors.toList());
    }
}
