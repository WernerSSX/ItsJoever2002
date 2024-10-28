package db;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import items.ReplenishmentRequest;

public class ReplenishmentRequestsLoader extends DataLoader<ReplenishmentRequest> {
    private List<ReplenishmentRequest> replenishmentRequests;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public ReplenishmentRequestsLoader(String filePath) {
        super(filePath);
        this.replenishmentRequests = new ArrayList<>();
    }

    @Override
    public void loadData() throws IOException {
    	List<String> lines = read(filePath);
        replenishmentRequests.clear();
        for (String line : lines) {
            replenishmentRequests.add(deserialize(line));
        }
    }

    // Implement the abstract saveData() method from DataLoader
    @Override
    public void saveData() throws IOException {
    	List<String> lines = new ArrayList<>();
        for (ReplenishmentRequest request : replenishmentRequests) {
            lines.add(serialize(request));
        }
        write(filePath, lines);
    }
    
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
    
    public List<ReplenishmentRequest> getReplenishmentRequests() {
        return Collections.unmodifiableList(replenishmentRequests);
    }
    
    protected String serialize(ReplenishmentRequest request) {
    	return String.join(";",
                request.getMedicationName() != null ? request.getMedicationName() : "NULL",
                String.valueOf(request.getQuantity()),
                request.getRequestedBy() != null ? request.getRequestedBy() : "NULL",
                request.getRequestDate() != null ? request.getRequestDate().format(DateTimeFormatter.ISO_LOCAL_DATE) : "NULL"
        );
    }
}
