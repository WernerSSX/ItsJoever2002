package db;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

import items.TimeSlot;
import items.Appointment;

public class AppointmentsLoader extends DataLoader<Appointment> {
    private List<Appointment> appointments;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public AppointmentsLoader(String filePath) {
        super(filePath);
        this.appointments = new ArrayList<>();
    }

    @Override
    public void loadData() throws IOException {
        List<String> lines = read(filePath);
        appointments.clear();
        for (String line : lines) {
        	appointments.add(deserialize(line));
        }
    }

    // Implement the abstract saveData() method from DataLoader
    @Override
    public void saveData() throws IOException {
        List<String> lines = new ArrayList<>();
        for (Appointment record : appointments) {
            lines.add(serialize(record));
        }
        write(filePath, lines); // Assuming there's a 'write' method to write lines to a file
    }
    /**
     * Deserializes a MedicalRecord object from a string.
     *
     * Expected Format:
     * patientID|name|dateOfBirth|gender|phone|email|bloodType|diag1;date1,diag2;date2|treatment1^treatment2
     *
     * @param data Serialized string representation of the MedicalRecord
     * @return MedicalRecord object
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
     * Retrieves the list of loaded MedicalRecords.
     *
     * @return List of MedicalRecord objects.
     */
    public List<Appointment> getAppointments() {
        return Collections.unmodifiableList(appointments);
    }
    
    protected String serialize(Appointment appointment) {
    	return String.join(SEPARATOR,
                String.valueOf(appointment.getId()),
                appointment.getPatientId(),
                appointment.getDoctorId(),
                serializeTimeSlot(appointment.getTimeSlot()),
                appointment.getStatus(),
                appointment.getOutcomeRecord());
    }
    private static String serializeTimeSlot(TimeSlot timeSlot) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        return timeSlot.getStartTime().format(formatter) + "-" + timeSlot.getEndTime().format(formatter);
    }
}
