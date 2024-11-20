package items.appointments;

import items.appointments.schedule_interface.mainScheduleInterface;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Schedule
 * The Schedule class manages a doctor's availability across different dates.
 */
public class Schedule implements mainScheduleInterface {
    // Mapping from date to list of available TimeSlots
    private Map<LocalDate, List<TimeSlot>> availability; /**< Map to store availability of time slots keyed by date */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd"); /**< Formatter for date */
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm"); /**< Formatter for time */

    /****************
     * Constructors *
     ****************/

    /**
     * Constructor for Schedule. Initializes the availability map.
     */
    public Schedule() {
        this.availability = new HashMap<>();
    }

    /***********************
     * Getters and Setters *
     ***********************/

    /**
     * Sets availability for a specific date.
     *
     * @param date      The date for which to set availability
     * @param timeSlots List of TimeSlot objects representing available times
     */
    public void setAvailability(LocalDate date, List<TimeSlot> timeSlots) {
        availability.put(date, new ArrayList<>(timeSlots)); // Store the provided time slots for the specified date
    }

    /**
     * Retrieves available TimeSlots for a specific date.
     *
     * @param date The date for which to retrieve availability
     * @return List of available TimeSlot objects, or an empty list if none are set
     */
    public List<TimeSlot> getAvailableTimeSlots(LocalDate date) {
        return availability.getOrDefault(date, new ArrayList<>()); // Return available time slots or an empty list if not found
    }

    /**
     * Retrieves the entire availability map.
     *
     * @return Map of dates to lists of available TimeSlots
     */
    public Map<LocalDate, List<TimeSlot>> getAvailability() {
        return availability; // Return the entire availability map
    }

    /**
     * Sets the entire availability map.
     *
     * @param availability Map of dates to lists of available TimeSlots
     */
    public void setAvailabilityMap(Map<LocalDate, List<TimeSlot>> availability) {
        this.availability = availability; // Update the availability map with the provided one
    }

    /**********
     * Methods *
     **********/

    /**
     * Provides a string representation of the Schedule.
     *
     * @return A formatted string showing the availability for each date and corresponding time slots
     */
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
        return sb.toString(); // Return the constructed string representation of the schedule
    }

    // Additional methods can be added here as needed
}
