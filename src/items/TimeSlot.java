package items;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * @class TimeSlot
 * @brief The TimeSlot class represents a specific time interval, such as an appointment slot.
 */
public class TimeSlot {
    private LocalDateTime startTime; /**< Start time of the time slot */
    private LocalDateTime endTime;   /**< End time of the time slot */
    private boolean isAvailable;      /**< Availability status of the time slot */

    /****************
     * Constructors *
     ****************/

    /**
     * Constructor for TimeSlot.
     *
     * @param startTime   Start time of the slot
     * @param endTime     End time of the slot
     * @param isAvailable Availability status of the slot
     */
    public TimeSlot(LocalDateTime startTime, LocalDateTime endTime, boolean isAvailable) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.isAvailable = isAvailable;
    }

    /***********************
     * Getters and Setters *
     ***********************/

    /**
     * @brief Gets the start time of the timeslot
     * @return Returns the start time of the timeslot
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * @brief Sets the start time of the timeslot
     * @param startTime Start time of the timeslot
     */
    public void setStartTime(LocalDateTime startTime) { 
        this.startTime = startTime;
    }

    /**
     * @brief Gets the end time of the timeslot
     * @return Returns the end time of the timeslot
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * @brief Sets the end time of the timeslot
     * @param endTime End time of the timeslot
     */
    public void setEndTime(LocalDateTime endTime) { 
        this.endTime = endTime;
    }

    /**
     * @brief Checks if the timeslot is available
     * @return availability of the timeslot
     */
    public boolean isAvailable() {
        return isAvailable;
    }

    /**
     * @brief Sets availability of the timeslot
     * @param available Availability
     */
    public void setAvailable(boolean available) { 
        isAvailable = available;
    }

    /**********
     * Methods *
     **********/

    /**
     * Parses a TimeSlot from a serialized string.
     *
     * @param timeSlotString String in the format "yyyy-MM-ddTHH:mm-yyyy-MM-ddTHH:mm"
     * @return TimeSlot object
     */
    public static TimeSlot parse(String timeSlotString) {
        boolean debug = false; // Debug flag for tracing parsing

        timeSlotString = timeSlotString.trim(); // Trim leading and trailing whitespace

        // Split the time slot string into start and end parts based on the defined pattern
        String[] parts = timeSlotString.split("(?<=\\d{2}T\\d{2}:\\d{2})-(?=\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2})");
        
        // Debugging output
        if (debug) {
            System.out.println("Parsing time slot string: " + timeSlotString);
            System.out.println("Split parts length: " + parts.length);
            for (int i = 0; i < parts.length; i++) {
                System.out.println("Part " + i + ": '" + parts[i] + "'");
            }
        }

        // Check if the split resulted in exactly two parts
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid time slot format: " + timeSlotString); // Error for invalid format
        }

        // Trim each part to remove any leading or trailing whitespace
        String startTimeString = parts[0].trim();
        String endTimeString = parts[1].trim();
        
        // Debugging output
        if (debug) {
            System.out.println("Start time part: '" + startTimeString + "'");
            System.out.println("End time part: '" + endTimeString + "'");
        }

        // Define the date-time formatter for parsing
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        // Parse the start and end times
        LocalDateTime startTime = LocalDateTime.parse(startTimeString, formatter);
        LocalDateTime endTime = LocalDateTime.parse(endTimeString, formatter);
        
        // Debugging output
        if (debug) {
            System.out.println("Parsed start time: " + startTime);
            System.out.println("Parsed end time: " + endTime);
        }

        boolean isAvailable = false; // Default availability status (could be modified later)
        return new TimeSlot(startTime, endTime, isAvailable); // Return a new TimeSlot object
    }

    /**
     * Provides a string representation of the TimeSlot in a human-readable format.
     *
     * @return A string representing the time interval of the slot in HH:mm format
     */
    @Override
    public String toString() {
        return startTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")) +
               " - " + endTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")); // Format the start and end time
    }

    /**
     * @brief Checks if two timeslots are the same
     * @param o Object that is checked to be a Timeslot
     * @return True if the two timeslots are the same
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Check reference equality
        if (o == null || getClass() != o.getClass()) return false; // Check for null and class type

        TimeSlot timeSlot = (TimeSlot) o; // Cast to TimeSlot

        // Compare start and end times for equality
        if (!Objects.equals(startTime, timeSlot.startTime)) return false;
        return Objects.equals(endTime, timeSlot.endTime);
    }

    /**
     * @brief Gets HashCode of Timeslot
     * @return Returns HashCode based on startTime and endTime of TimeSlot
     */
    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime); // Generate hash code based on start and end times
    }
}
