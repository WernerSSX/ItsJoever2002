package items;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * The ReplenishmentRequest class represents a request to replenish a specific medication.
 */
public class ReplenishmentRequest {
    private String medicationName;
    private int quantity;
    private String requestedBy;
    private LocalDate requestDate;

    public ReplenishmentRequest(String medicationName, int quantity, String requestedBy, LocalDate requestDate) {
        this.medicationName = medicationName;
        this.quantity = quantity;
        this.requestedBy = requestedBy;
        this.requestDate = requestDate;
    }

    // Getters and Setters

    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

	public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
	
	public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    /**
     * Serializes the ReplenishmentRequest into a string.
     *
     * Format:
     * medicationName;quantity;requestedBy;requestDate
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
     * Expected Format:
     * medicationName;quantity;requestedBy;requestDate
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
