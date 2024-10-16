package items;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

/**
 * The TimeSlot class represents a specific time interval, such as an appointment slot.
 */
public class TimeSlot {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isAvailable;

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

    // Getters and Setters
    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) { 
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) { 
        this.endTime = endTime;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) { 
        isAvailable = available;
    }

    /**
     * Parses a TimeSlot from a serialized string.
     *
     * @param serialized String in the format "yyyy-MM-ddTHH:mm-yyyy-MM-ddTHH:mm"
     * @return TimeSlot object
     */
    public static TimeSlot parse(String timeSlotString) {
        boolean debug = false;
        
        timeSlotString = timeSlotString.trim();
    
        String[] parts = timeSlotString.split("(?<=\\d{2}T\\d{2}:\\d{2})-(?=\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2})");
        
        if (debug) {
            System.out.println("Parsing time slot string: " + timeSlotString);
            System.out.println("Split parts length: " + parts.length);
            for (int i = 0; i < parts.length; i++) {
                System.out.println("Part " + i + ": '" + parts[i] + "'");
            }
        }
        
        
        // Check if the split resulted in exactly two parts
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid time slot format: " + timeSlotString);
        }
        
        // Trim each part to remove any leading or trailing whitespace
        String startTimeString = parts[0].trim();
        String endTimeString = parts[1].trim();
        
        if (debug) {
            System.out.println("Start time part: '" + startTimeString + "'");
            System.out.println("End time part: '" + endTimeString + "'");
        }
        // Define the date-time formatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        
        // Parse the start and end times
        LocalDateTime startTime = LocalDateTime.parse(startTimeString, formatter);
        LocalDateTime endTime = LocalDateTime.parse(endTimeString, formatter);
        
        if (debug) {
            System.out.println("Parsed start time: " + startTime);
            System.out.println("Parsed end time: " + endTime);
        }
        boolean isAvailable = false;
        return new TimeSlot(startTime, endTime, isAvailable);
    }


    @Override
    public String toString() {
        return startTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")) +
               " - " + endTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TimeSlot timeSlot = (TimeSlot) o;

        if (!Objects.equals(startTime, timeSlot.startTime)) return false;
        return Objects.equals(endTime, timeSlot.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime);
    }
}
