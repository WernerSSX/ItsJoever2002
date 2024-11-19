package items;

import java.time.LocalDate;

/**
 * Diagnosis
 * Represents a medical diagnosis with a description, date, and additional comments.
 *
 * The Diagnosis class stores information about a medical diagnosis, including a description
 * of the diagnosed illness, the date of diagnosis, and optional comments.
 */
public class Diagnosis {
    private String description; /**< Description of the diagnosed illness */
    private LocalDate date;     /**< Date of the diagnosis */
    private String comments;    /**< Additional comments regarding the diagnosis */

    /****************
     * Constructors *
     ****************/

    /**
     * Constructs a Diagnosis with the specified description, date, and comments.
     *
     * @param description Description of the diagnosed illness.
     * @param date        Date of diagnosis.
     * @param comments    Additional comments regarding the diagnosis.
     */
    public Diagnosis(String description, LocalDate date, String comments) {
        this.description = description;
        this.date = date;
        this.comments = comments;
    }

    /**
     * Constructs a Diagnosis with the specified description and date.
     *
     * This constructor is provided for cases where comments are not needed.
     *
     * @param description Description of the diagnosed illness.
     * @param date        Date of diagnosis.
     */
    public Diagnosis(String description, LocalDate date) {
        this(description, date, "");
    }

    /***********************
     * Getters and Setters *
     ***********************/

    /**
     * Gets the description of the diagnosed illness.
     * @return The description of the illness.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the diagnosed illness.
     * @param description The new description of the illness.
     */
    public void setDescription(String description) { 
        this.description = description;
    }

    /**
     * Gets the date of the diagnosis.
     * @return The date of diagnosis.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the date of the diagnosis.
     * @param date The new date of diagnosis.
     */
    public void setDate(LocalDate date) { 
        this.date = date;
    }

    /**
     * Gets additional comments about the diagnosis.
     * @return Comments regarding the diagnosis.
     */
    public String getComments() {
        return comments;
    }

    /**
     * Sets additional comments about the diagnosis.
     * @param comments The new comments.
     */
    public void setComments(String comments) { 
        this.comments = comments;
    }

    /**
     * Prints the diagnosed illness details along with any associated comments.
     *
     * This method outputs the description, date, and comments (if any) related to the diagnosis.
     */
    public void printDiagnosedIllnessWithComments() {
        System.out.println("Diagnosis: " + description);
        System.out.println("Date: " + date);
        if (comments != null && !comments.trim().isEmpty()) {
            System.out.println("Comments: " + comments);
        }
        System.out.println("-------------------------");
    }

    /**********
     * Methods *
     **********/

    /**
     * Returns a string representation of the diagnosis.
     * @return A string containing the description, date, and comments.
     */
    @Override
    public String toString() {
        return "Diagnosis{" +
                "description='" + description + '\'' +
                ", date=" + date +
                ", comments='" + comments + '\'' +
                '}';
    }
}
