package db;

import items.Medication;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * MedicationInventoryLoader
 * Handles the loading and saving of medication inventory data from/to a file.
 *
 * This class extends DataLoader to manage Medication objects, providing functionality
 * for reading from and writing to a file in a specified format.
 */
public class MedicationInventoryLoader extends DataLoader<Medication> {
    private List<Medication> medications; /**< List to store loaded Medication objects. */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd"); /**< Date formatter for serialization. */
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm"); /**< Time formatter for serialization. */

    /**
     * Constructs a MedicationInventoryLoader with the specified file path.
     *
     * @param filePath Path to the file associated with this MedicationInventoryLoader.
     */
    public MedicationInventoryLoader(String filePath) {
        super(filePath);
        this.medications = new ArrayList<>();
    }

    /**
     * Loads medication inventory from the specified file.
     *
     * Reads each line from the file, deserializing each line to a Medication object.
     *
     * @throws IOException If an I/O error occurs during file reading.
     */
    @Override
    public void loadData() throws IOException {
        List<String> lines = read(filePath);
        medications.clear();
        for (String line : lines) {
            medications.add(deserialize(line));
        }
    }

    /**
     * Saves all loaded medications to the associated file.
     *
     * Serializes each Medication object and writes it as a line in the file.
     *
     * @throws IOException If an I/O error occurs during file writing.
     */
    @Override
    public void saveData() throws IOException {
        List<String> lines = new ArrayList<>();
        for (Medication med : medications) {
            lines.add(serialize(med));
        }
        write(filePath, lines);
    }

    /**
     * Deserializes a Medication object from a string representation.
     *
     * Expected Format: name|quantity|supplier
     *
     * @param data Serialized string representation of the Medication.
     * @return Deserialized Medication object.
     * @throws IllegalArgumentException If the data format is invalid.
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
     * Retrieves the list of loaded Medications.
     *
     * @return Unmodifiable list of Medication objects.
     */
    public List<Medication> getMedicationInventory() {
        return Collections.unmodifiableList(medications);
    }

    /**
     * Serializes a Medication object into a string representation.
     *
     * The format is name|quantity|supplier, where supplier can be "NULL" if not provided.
     *
     * @param medication Medication object to serialize.
     * @return Serialized string representation of the Medication.
     */
    protected String serialize(Medication medication) {
        return String.join("|",
                medication.getName(),
                String.valueOf(medication.getQuantity()),
                medication.getSupplier() != null ? medication.getSupplier() : "NULL"
        );
    }
}
