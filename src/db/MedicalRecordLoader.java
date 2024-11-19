package db;

import items.ContactInformation;
import items.Diagnosis;
import items.MedicalRecord;
import items.Treatment;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MedicalRecordLoader
 * Class responsible for loading and saving MedicalRecord data from and to files.
 *
 * MedicalRecordLoader extends DataLoader to handle MedicalRecord objects, managing
 * serialization and deserialization from a specified file format.
 */
public class MedicalRecordLoader extends DataLoader<MedicalRecord> {
    private List<MedicalRecord> medicalRecords; /**< List to store loaded MedicalRecord objects. */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd"); /**< Date formatter for serialization. */
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm"); /**< Time formatter for serialization. */

    /**
     * Constructs a MedicalRecordLoader with the specified file path.
     *
     * @param filePath Path to the file associated with this MedicalRecordLoader.
     */
    public MedicalRecordLoader(String filePath) {
        super(filePath);
        this.medicalRecords = new ArrayList<>();
    }

    /**
     * Loads medical records from the associated file.
     *
     * Reads each line from the file, deserializing each line to a MedicalRecord object.
     *
     * @throws IOException If an error occurs during file reading.
     */
    @Override
    public void loadData() throws IOException {
        List<String> lines = read(filePath);
        medicalRecords.clear();
        for (String line : lines) {
            medicalRecords.add(deserialize(line));
        }
    }

    /**
     * Saves all loaded medical records to the associated file.
     *
     * Serializes each MedicalRecord object and writes it as a line in the file.
     *
     * @throws IOException If an error occurs during file writing.
     */
    @Override
    public void saveData() throws IOException {
        List<String> lines = new ArrayList<>();
        for (MedicalRecord record : medicalRecords) {
            lines.add(serialize(record));
        }
        write(filePath, lines); // Assuming there's a 'write' method to write lines to a file
    }

    /**
     * Deserializes a MedicalRecord object from a string representation.
     *
     * Expected Format: patientID|name|dateOfBirth|gender|phone|email|bloodType|diag1;date1,diag2;date2|treatment1^treatment2
     *
     * @param data Serialized string representation of the MedicalRecord.
     * @return Deserialized MedicalRecord object.
     * @throws IllegalArgumentException If the data format is invalid.
     */
    protected MedicalRecord deserialize(String data) {
        String[] fields = data.split("\\" + SEPARATOR, -1); // -1 to include empty trailing fields

        if (fields.length < 9) {
            throw new IllegalArgumentException("Invalid medical record data: " + data);
        }

        String patientID = fields[0];
        String name = fields[1];
        LocalDate dob = LocalDate.parse(fields[2], DATE_FORMATTER);
        String gender = fields[3];
        String phone = fields[4];
        String email = fields[5];
        String bloodType = fields[6].equals("NULL") ? null : fields[6];

        ContactInformation contactInfo = new ContactInformation(phone, email);

        List<Diagnosis> diagnoses = new ArrayList<>();
        if (!fields[7].equals("NULL") && !fields[7].trim().isEmpty()) {
            String[] diagParts = fields[7].split(",");
            for (String diag : diagParts) {
                String[] diagFields = diag.split(";");
                if (diagFields.length == 2) {
                    diagnoses.add(new Diagnosis(diagFields[0], LocalDate.parse(diagFields[1], DATE_FORMATTER)));
                }
            }
        }

        List<Treatment> treatments = new ArrayList<>();
        if (!fields[8].equals("NULL") && !fields[8].trim().isEmpty()) {
            String[] treatParts = fields[8].split("\\^");
            for (String treat : treatParts) {
                treatments.add(Treatment.deserialize(treat));
            }
        }

        MedicalRecord record = new MedicalRecord(patientID, name, dob, gender, contactInfo, bloodType, diagnoses, treatments);

        return record;
    }

    /**
     * Retrieves the list of loaded MedicalRecords.
     *
     * @return Unmodifiable list of MedicalRecord objects.
     */
    public List<MedicalRecord> getMedicalRecords() {
        return Collections.unmodifiableList(medicalRecords);
    }

    /**
     * Serializes a MedicalRecord object into a string representation.
     *
     * The format is patientID|name|dateOfBirth|gender|phone|email|bloodType|diagnoses|treatments
     * where diagnoses and treatments are serialized lists.
     *
     * @param record MedicalRecord object to serialize.
     * @return Serialized string representation of the MedicalRecord.
     */
    protected String serialize(MedicalRecord record) {
        StringBuilder sb = new StringBuilder();

        sb.append(record.getPatientID()).append(SEPARATOR);
        sb.append(record.getName()).append(SEPARATOR);
        sb.append(record.getDateOfBirth().format(DATE_FORMATTER)).append(SEPARATOR);
        sb.append(record.getGender()).append(SEPARATOR);
        sb.append(record.getContactInformation().getPhoneNumber()).append(SEPARATOR);
        sb.append(record.getContactInformation().getEmailAddress()).append(SEPARATOR);
        sb.append(record.getBloodType() != null ? record.getBloodType() : "NULL").append(SEPARATOR);

        if (record.getPastDiagnoses() != null && !record.getPastDiagnoses().isEmpty()) {
            String diagnoses = record.getPastDiagnoses().stream()
                .map(d -> d.getDescription() + ";" + d.getDate().format(DATE_FORMATTER))
                .collect(Collectors.joining(","));
            sb.append(diagnoses);
        } else {
            sb.append("NULL");
        }
        sb.append(SEPARATOR);

        if (record.getPastTreatments() != null && !record.getPastTreatments().isEmpty()) {
            String treatments = record.getPastTreatments().stream()
                .map(Treatment::serialize)
                .collect(Collectors.joining("^"));
            sb.append(treatments);
        } else {
            sb.append("NULL");
        }

        return sb.toString();
    }
}
