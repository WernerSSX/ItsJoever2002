package items;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The Schedule class manages a doctor's availability across different dates.
 */
public class Schedule {
    // Mapping from date to list of available TimeSlots
    private Map<LocalDate, List<TimeSlot>> availability;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public Schedule() {
        this.availability = new HashMap<>();
    }

    /**
     * Sets availability for a specific date.
     *
     * @param date         The date for which to set availability
     * @param timeSlots List of TimeSlot objects representing available times
     */
    public void setAvailability(LocalDate date, List<TimeSlot> timeSlots) {
        availability.put(date, new ArrayList<>(timeSlots));
    }

    /**
     * Retrieves available TimeSlots for a specific date.
     *
     * @param date The date for which to retrieve availability
     * @return List of available TimeSlot objects, or an empty list if none are set
     */
    public List<TimeSlot> getAvailableTimeSlots(LocalDate date) {
        return availability.getOrDefault(date, new ArrayList<>());
    }

    /**
     * Retrieves the entire availability map.
     *
     * @return Map of dates to lists of available TimeSlots
     */
    public Map<LocalDate, List<TimeSlot>> getAvailability() {
        return availability;
    }

    /**
     * Sets the entire availability map.
     *
     * @param availability Map of dates to lists of available TimeSlots
     */
    public void setAvailabilityMap(Map<LocalDate, List<TimeSlot>> availability) {
        this.availability = availability;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Schedule:\n");
        for (Map.Entry<LocalDate, List<TimeSlot>> entry : availability.entrySet()) {
            sb.append("Date: ").append(entry.getKey().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).append("\n");
            for (TimeSlot slot : entry.getValue()) {
                sb.append("  ").append(slot.getStartTime().toLocalTime().format(TIME_FORMATTER))
                .append(" - ").append(slot.getEndTime().toLocalTime().format(TIME_FORMATTER))
                .append("\n");
            }
        }
        return sb.toString();
    }


    // Additional methods can be added here as needed
}
