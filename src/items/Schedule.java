package items;
import user_classes.*;

import java.util.List;

public class Schedule {
    private Doctor doctor;
    private List<TimeSlot> availableSlots;


    public Schedule(Doctor doctor, List<TimeSlot> availableSlots) {
        this.doctor = doctor;
        this.availableSlots = availableSlots;
    }

    public Schedule() {
        //TODO Auto-generated constructor stub
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public List<TimeSlot> getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(List<TimeSlot> availableSlots) {
        this.availableSlots = availableSlots;
    }

    public void setAvailability(List<TimeSlot> newAvailableSlots) {
        setAvailableSlots(newAvailableSlots);
    }

    public void viewSchedule() {
        // Implementation for viewing the schedule
    }
}