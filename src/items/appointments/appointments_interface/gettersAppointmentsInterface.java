package items.appointments.appointments_interface;

import items.appointments.TimeSlot;
import java.time.LocalDate;

public interface gettersAppointmentsInterface {
    public int getId();
    public String getPatientId();
    public String getDoctorId();
    public TimeSlot getTimeSlot();
    public String getStatus();
    public String getOutcomeRecord();
    public LocalDate getDate();
}
