package db;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

import items.Medication;

public class MedicationInventoryLoader extends DataLoader<Medication> {
    private List<Medication> medications;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public MedicationInventoryLoader(String filePath) {
        super(filePath);
        this.medications = new ArrayList<>();
    }

    @Override
    /**
     * Loads medication inventory from the specified file.
     *
     * @param filename The name of the inventory file.
     * @throws IOException If an I/O error occurs.
     */
    public void loadData() throws IOException {
    	List<String> lines = read(filePath);
        medications.clear();
        for (String line : lines) {
            medications.add(deserialize(line));
        }
    }

    // Implement the abstract saveData() method from DataLoader
    @Override
    public void saveData() throws IOException {
    	List<String> lines = new ArrayList<>();
        for (Medication med : medications) {
            lines.add(serialize(med));
        }
        write(filePath, lines);
    }
    /**
     * Deserializes a MedicalRecord object from a string.
     *
     * Expected Format:
     * patientID|name|dateOfBirth|gender|phone|email|bloodType|diag1;date1,diag2;date2|treatment1^treatment2
     *
     * @param data Serialized string representation of the MedicalRecord
     * @return MedicalRecord object
     */
    protected Medication deserialize(String data) {
    	String[] parts = data.split("\\|", -1);
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid Medication data: " + data);
        }
        String name = parts[0];
        int quantity = Integer.parseInt(parts[1]);
        String supplier = parts[2].equals("NULL") ? null : parts[2];
        return new Medication(name, quantity, supplier);
    }

    /**
     * Retrieves the list of loaded MedicalRecords.
     *
     * @return List of MedicalRecord objects.
     */
    public List<Medication> getMedicationInventory() {
        return Collections.unmodifiableList(medications);
    }
    
    protected String serialize(Medication medication) {
    	return String.join("|",
                medication.getName(),
                String.valueOf(medication.getQuantity()),
                medication.getSupplier() != null ? medication.getSupplier() : "NULL"
        );
    }
}
