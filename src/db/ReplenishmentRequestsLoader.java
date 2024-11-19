package db;

import items.ReplenishmentRequest;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ReplenishmentRequestsLoader
 * Manages the loading and saving of replenishment requests data from/to a file.
 *
 * This class extends DataLoader to handle ReplenishmentRequest objects, providing methods
 * for reading from and writing to a file in a specified format.
 */
public class ReplenishmentRequestsLoader extends DataLoader<ReplenishmentRequest> {
    private List<ReplenishmentRequest> replenishmentRequests; /**< List to store loaded ReplenishmentRequest objects. */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd"); /**< Date formatter for serialization. */
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm"); /**< Time formatter for serialization. */

    /**
     * Constructs a ReplenishmentRequestsLoader with the specified file path.
     *
     * @param filePath Path to the file associated with this ReplenishmentRequestsLoader.
     */
    public ReplenishmentRequestsLoader(String filePath) {
        super(filePath);
        this.replenishmentRequests = new ArrayList<>();
    }

    /**
     * Loads replenishment requests from the specified file.
     *
     * Reads each line from the file, deserializing each line to a ReplenishmentRequest object.
     *
     * @throws IOException If an I/O error occurs during file reading.
     */
    @Override
    public void loadData() throws IOException {
        List<String> lines = read(filePath);
        replenishmentRequests.clear();
        for (String line : lines) {
            replenishmentRequests.add(deserialize(line));
        }
    }

    /**
     * Saves all loaded replenishment requests to the associated file.
     *
     * Serializes each ReplenishmentRequest object and writes it as a line in the file.
     *
     * @throws IOException If an I/O error occurs during file writing.
     */
    @Override
    public void saveData() throws IOException {
        List<String> lines = new ArrayList<>();
        for (ReplenishmentRequest request : replenishmentRequests) {
            lines.add(serialize(request));
        }
        write(filePath, lines);
    }

    /**
     * Deserializes a ReplenishmentRequest object from a string representation.
     *
     * Expected Format: medicationName;quantity;requestedBy;requestDate
     *
     * @param data Serialized string representation of the ReplenishmentRequest.
     * @return Deserialized ReplenishmentRequest object.
     * @throws IllegalArgumentException If the data format is invalid.
     */
    protected ReplenishmentRequest deserialize(String data) {
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
     * Retrieves the list of loaded ReplenishmentRequests.
     *
     * @return Unmodifiable list of ReplenishmentRequest objects.
     */
    public List<ReplenishmentRequest> getReplenishmentRequests() {
        return Collections.unmodifiableList(replenishmentRequests);
    }

    /**
     * Serializes a ReplenishmentRequest object into a string representation.
     *
     * The format is medicationName;quantity;requestedBy;requestDate, where fields can be "NULL" if not provided.
     *
     * @param request ReplenishmentRequest object to serialize.
     * @return Serialized string representation of the ReplenishmentRequest.
     */
    protected String serialize(ReplenishmentRequest request) {
        return String.join(";",
                request.getMedicationName() != null ? request.getMedicationName() : "NULL",
                String.valueOf(request.getQuantity()),
                request.getRequestedBy() != null ? request.getRequestedBy() : "NULL",
                request.getRequestDate() != null ? request.getRequestDate().format(DateTimeFormatter.ISO_LOCAL_DATE) : "NULL"
        );
    }
}
