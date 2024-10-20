package items;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Appointment {
    private int id;
    private String patientId;
    private String doctorId;
    private TimeSlot timeSlot;
    private String status;
    private String outcomeRecord;

    

    public Appointment(int id, String patientId, String doctorId, TimeSlot timeSlot, String status, String outcomeRecord) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.timeSlot = timeSlot;
        this.status = status;
        this.outcomeRecord = outcomeRecord;
    }

    public void print() {
        System.out.println("\nAppointment ID: " + id);
        System.out.println("Patient ID: " + patientId);
        System.out.println("Doctor ID: " + doctorId);
        System.out.println("Time Slot: " + timeSlot.toString());
        System.out.println("Status: " + status);
        System.out.println("Outcome Record: " + outcomeRecord);
    }

    public int getId() {
        return id;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOutcomeRecord() {
        return outcomeRecord;
    }

    public void setOutcomeRecord(String outcomeRecord) {
        this.outcomeRecord = outcomeRecord;
    }
    
    public LocalDate getDate() {
        return timeSlot.getStartTime().toLocalDate();
    }
    public boolean isPast() {
        return timeSlot.getEndTime().isBefore(LocalDateTime.now());
    }
}