package items;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @class ReplenishmentRequest
 * @brief Represents a request to replenish a specific medication.
 */
public class ReplenishmentRequest {
    private String medicationName; /**< Name of the medication requested for replenishment */
    private int quantity; /**< Quantity of the medication requested */
    private String requestedBy; /**< Name of the person requesting the medication */
    private LocalDate requestDate; /**< Date when the replenishment request was made */

    /****************
     * Constructors *
     ****************/

    /**
     * Constructor for ReplenishmentRequest.
     *
     * @param medicationName Name of the medication requested
     * @param quantity       Quantity of the medication requested
     * @param requestedBy    Name of the person making the request
     * @param requestDate    Date of the request
     */
    public ReplenishmentRequest(String medicationName, int quantity, String requestedBy, LocalDate requestDate) {
        this.medicationName = medicationName;
        this.quantity = quantity;
        this.requestedBy = requestedBy;
        this.requestDate = requestDate;
    }

    /***********************
     * Getters and Setters *
     ***********************/

    /**
     * @brief Gets the name of the medication
     * @return The name of the medication requested.
     */
    public String getMedicationName() {
        return medicationName;
    }

    /**
     * @brief Sets the name of the medication
     * @param medicationName The new name of the medication requested.
     */
    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    /**
     * @brief Gets the quantity of the medication
     * @return The quantity of the medication requested.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @brief Sets the quantity of the medication
     * @param quantity The new quantity of the medication requested.
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * @brief Gets the name of the person who requested the medication
     * @return The name of the person who requested the medication.
     */
    public String getRequestedBy() {
        return requestedBy;
    }

    /**
     * @brief Sets the name of the person who requested the medication
     * @param requestedBy The new name of the person making the request.
     */
    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    /**
     * @brief Gets the date of when the replenishment request was made
     * @return The date when the replenishment request was made.
     */
    public LocalDate getRequestDate() {
        return requestDate;
    }

    /**
     * @brief Sets the new date for the replenishment request
     * @param requestDate The new date for the request.
     */
    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    /**********
     * Methods *
     **********/

    /**
     * Serializes the ReplenishmentRequest into a string.
     *
     * The format of the serialized string is:
     * medicationName;quantity;requestedBy;requestDate
     *
     * @return A string representation of the ReplenishmentRequest.
     */
    public String serialize() {
        return String.join(";",
                medicationName != null ? medicationName : "NULL",
                String.valueOf(quantity),
                requestedBy != null ? requestedBy : "NULL",
                requestDate != null ? requestDate.format(DateTimeFormatter.ISO_LOCAL_DATE) : "NULL"
        );
    }

    /**
     * Deserializes a ReplenishmentRequest from a string.
     *
     * The expected format of the string is:
     * medicationName;quantity;requestedBy;requestDate
     *
     * @param data The serialized string containing the request details.
     * @return A new ReplenishmentRequest object created from the serialized data.
     * @throws IllegalArgumentException if the data string does not contain the expected format.
     */
    public static ReplenishmentRequest deserialize(String data) {
        String[] parts = data.split(";", -1);
        if (parts.length != 4) {
            throw new IllegalArgumentException("Invalid ReplenishmentRequest data: " + data);
        }

        String medicationName = parts[0].equals("NULL") ? null : parts[0];
        int quantity = Integer.parseInt(parts[1]);
        String requestedBy = parts[2].equals("NULL") ? null : parts[2];
        LocalDate requestDate = parts[3].equals("NULL") ? null : LocalDate.parse(parts[3], DateTimeFormatter.ISO_LOCAL_DATE);

        return new ReplenishmentRequest(medicationName, quantity, requestedBy, requestDate);
    }

    /**
     * Provides a string representation of the ReplenishmentRequest.
     *
     * @return A string describing the ReplenishmentRequest.
     */
    @Override
    public String toString() {
        return "ReplenishmentRequest{" +
                "medicationName='" + medicationName + '\'' +
                ", quantity=" + quantity +
                ", requestedBy='" + requestedBy + '\'' +
                ", requestDate=" + requestDate +
                '}';
    }
}
