package items;
import java.time.LocalDateTime;

public class TimeSlot {
    private LocalDateTime dateTime; // Changed to LocalDateTime
    private boolean isAvailable;

    public TimeSlot(LocalDateTime dateTime, boolean isAvailable) {
        this.dateTime = dateTime;
        this.isAvailable = isAvailable;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public void reserveSlot() {
        if (isAvailable) {
            isAvailable = false;
            System.out.println("Slot reserved for " + dateTime);
        } else {
            System.out.println("Slot is already reserved.");
        }
    }

    public void releaseSlot() {
        if (!isAvailable) {
            isAvailable = true;
            System.out.println("Slot released for " + dateTime);
        } else {
            System.out.println("Slot is already available.");
        }
    }
}