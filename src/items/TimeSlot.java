package items;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class TimeSlot {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isAvailable;

    public TimeSlot(LocalDateTime startTime, LocalDateTime endTime, boolean isAvailable) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.isAvailable = isAvailable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        TimeSlot timeSlot = (TimeSlot) o;
        return Objects.equals(startTime, timeSlot.startTime) &&
               Objects.equals(endTime, timeSlot.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime);
    }

    @Override
    public String toString() {
        return "TimeSlot{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                ", isAvailable=" + isAvailable +
                '}';
    }


    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String toCalendarString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return String.format("%s - %s : %s", 
                startTime.format(formatter), 
                endTime.format(formatter), 
                isAvailable ? "Available" : "Not Available");
    }

    

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
}