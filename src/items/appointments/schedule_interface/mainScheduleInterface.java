package items.appointments.schedule_interface;

import items.appointments.TimeSlot;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface mainScheduleInterface{

    public void setAvailability(LocalDate date, List<TimeSlot> timeSlots);
    public List<TimeSlot> getAvailableTimeSlots(LocalDate date);
    public Map<LocalDate, List<TimeSlot>> getAvailability();
    public void setAvailabilityMap(Map<LocalDate, List<TimeSlot>> availability); 
    public String toString();

}