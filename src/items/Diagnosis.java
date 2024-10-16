package items;

import java.time.LocalDate;

/**
 * The Diagnosis class represents a medical diagnosis with a description, date, and comments.
 */
public class Diagnosis {
    private String description;
    private LocalDate date;
    private String comments; // Additional field for comments

    /**
     * Constructor for Diagnosis.
     *
     * @param description Description of the diagnosed illness
     * @param date        Date of diagnosis
     * @param comments    Additional comments regarding the diagnosis
     */
    public Diagnosis(String description, LocalDate date, String comments) {
        this.description = description;
        this.date = date;
        this.comments = comments;
    }

    // Existing Constructor without comments (optional)
    public Diagnosis(String description, LocalDate date) {
        this(description, date, "");
    }

    // Getters and Setters

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) { 
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) { 
        this.date = date;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) { 
        this.comments = comments;
    }

    /**
     * Prints the diagnosed illness along with any associated comments.
     */
    public void printDiagnosedIllnessWithComments() {
        System.out.println("Diagnosis: " + description);
        System.out.println("Date: " + date);
        if (comments != null && !comments.trim().isEmpty()) {
            System.out.println("Comments: " + comments);
        }
        System.out.println("-------------------------");
    }

    @Override
    public String toString() {
        return "Diagnosis{" +
                "description='" + description + '\'' +
                ", date=" + date +
                ", comments='" + comments + '\'' +
                '}';
    }
}
