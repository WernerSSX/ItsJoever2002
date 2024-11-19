package db;

import items.Appointment;
import items.TimeSlot;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * AppointmentsLoader
 * Handles loading, saving, and managing appointment data from a file.
 * 
 * This class extends DataLoader and provides functionality to load and save Appointment
 * objects from and to a specified file. It includes methods to serialize and deserialize
 * appointments for persistence.
 */
public class AppointmentsLoader extends DataLoader<Appointment> {
    private List<Appointment> appointments;   /**< List of appointments loaded from the file */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd"); /**< Formatter for date */
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");      /**< Formatter for time */

    /**
     * Constructs an AppointmentsLoader with a specified file path.
     *
     * @param filePath Path to the file containing appointment data.
     */
    public AppointmentsLoader(String filePath) {
        super(filePath);
        this.appointments = new ArrayList<>();
    }

    /**
     * Loads appointment data from the specified file.
     * 
     * @throws IOException if an error occurs while reading from the file.
     */
    @Override
    public void loadData() throws IOException {
        List<String> lines = read(filePath);
        appointments.clear();
        for (String line : lines) {
            appointments.add(deserialize(line));
        }
    }

    /**
     * Saves the current list of appointments to the specified file.
     * 
     * @throws IOException if an error occurs while writing to the file.
     */
    @Override
    public void saveData() throws IOException {
        List<String> lines = new ArrayList<>();
        for (Appointment record : appointments) {
            lines.add(serialize(record));
        }
        write(filePath, lines); // Assuming there's a 'write' method to write lines to a file
    }

    /**
     * Deserializes an Appointment object from a string.
     * 
     * Expected Format:
     * id|patientId|doctorId|timeSlot|status|outcomeRecord
     *
     * @param appointmentData Serialized string representation of the Appointment.
     * @return Appointment object deserialized from the input data.
     * @throws IllegalArgumentException if the appointment data is invalid.
     */
    protected Appointment deserialize(String appointmentData) {
        String[] fields = appointmentData.split("\\" + SEPARATOR);
        
        if (fields.length < 6) {
            System.err.println("Invalid appointment data: " + appointmentData);
            throw new IllegalArgumentException("Invalid appointment data: " + appointmentData);
        }
    
        int id = Integer.parseInt(fields[0]);
        String patientId = fields[1];
        String doctorId = fields[2];
        TimeSlot timeSlot = TimeSlot.parse(fields[3]);
        String status = fields[4];
        String outcomeRecord = fields[5];
    
        return new Appointment(id, patientId, doctorId, timeSlot, status, outcomeRecord);
    }

    /**
     * Retrieves an unmodifiable list of loaded appointments.
     * 
     * @return List of Appointment objects.
     */
    public List<Appointment> getAppointments() {
        return Collections.unmodifiableList(appointments);
    }
    
    /**
     * Serializes an Appointment object into a string for storage.
     *
     * @param appointment Appointment object to serialize.
     * @return Serialized string representation of the Appointment.
     */
    protected String serialize(Appointment appointment) {
        return String.join(SEPARATOR,
                String.valueOf(appointment.getId()),
                appointment.getPatientId(),
                appointment.getDoctorId(),
                serializeTimeSlot(appointment.getTimeSlot()),
                appointment.getStatus(),
                appointment.getOutcomeRecord());
    }

    /**
     * Serializes a TimeSlot object into a formatted string.
     * 
     * @param timeSlot TimeSlot object to serialize.
     * @return Serialized string representation of the TimeSlot.
     */
    private static String serializeTimeSlot(TimeSlot timeSlot) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        return timeSlot.getStartTime().format(formatter) + "-" + timeSlot.getEndTime().format(formatter);
    }
}
