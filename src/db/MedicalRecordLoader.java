package db;

import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Collections;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import items.ContactInformation;
import items.Diagnosis;
import items.MedicalRecord;
import items.Treatment;

public class MedicalRecordLoader extends DataLoader<MedicalRecord> {
    private List<MedicalRecord> medicalRecords;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public MedicalRecordLoader(String filePath) {
        super(filePath);
        this.medicalRecords = new ArrayList<>();
    }

    @Override
    public void loadData() throws IOException {
        List<String> lines = read(filePath);
        medicalRecords.clear();
        for (String line : lines) {
            medicalRecords.add(deserialize(line));
        }
    }

    // Implement the abstract saveData() method from DataLoader
    @Override
    public void saveData() throws IOException {
        List<String> lines = new ArrayList<>();
        for (MedicalRecord record : medicalRecords) {
            lines.add(serialize(record));
        }
        write(filePath, lines); // Assuming there's a 'write' method to write lines to a file
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
    protected MedicalRecord deserialize(String data) {
    	String[] fields = data.split("\\" + SEPARATOR, -1); // -1 to include empty trailing fields

        if (fields.length < 9) { // Expecting 9 fields now
            throw new IllegalArgumentException("Invalid medical record data: " + data);
        }

        // Basic information
        String patientID = fields[0];
        String name = fields[1];
        LocalDate dob = LocalDate.parse(fields[2], DATE_FORMATTER);
        String gender = fields[3];
        String phone = fields[4];
        String email = fields[5];
        String bloodType = fields[6].equals("NULL") ? null : fields[6];

        ContactInformation contactInfo = new ContactInformation(phone, email);

        // Deserialize Diagnoses
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

        // Deserialize Treatments using '^' as the separator
        List<Treatment> treatments = new ArrayList<>();
        if (!fields[8].equals("NULL") && !fields[8].trim().isEmpty()) {
            String[] treatParts = fields[8].split("\\^"); // Ensure '^' is used here
            for (String treat : treatParts) {
                treatments.add(Treatment.deserialize(treat)); // Ensure Treatment.deserialize handles the format correctly
            }
        }

        // Create and return the MedicalRecord
        MedicalRecord record = new MedicalRecord(patientID, name, dob, gender, contactInfo, bloodType, diagnoses, treatments);
        // No Assigned Doctor ID
        // record.setAssignedDoctorId(assignedDoctorId);

        return record;
    }

    /**
     * Retrieves the list of loaded MedicalRecords.
     *
     * @return List of MedicalRecord objects.
     */
    public List<MedicalRecord> getMedicalRecords() {
        return Collections.unmodifiableList(medicalRecords);
    }
    
    protected String serialize(MedicalRecord record) {
        StringBuilder sb = new StringBuilder();

        // Basic information
        sb.append(record.getPatientID()).append(SEPARATOR);
        sb.append(record.getName()).append(SEPARATOR);
        sb.append(record.getDateOfBirth().format(DATE_FORMATTER)).append(SEPARATOR);
        sb.append(record.getGender()).append(SEPARATOR);
        sb.append(record.getContactInformation().getPhoneNumber()).append(SEPARATOR);
        sb.append(record.getContactInformation().getEmailAddress()).append(SEPARATOR);
        sb.append(record.getBloodType() != null ? record.getBloodType() : "NULL").append(SEPARATOR);

        // Serialize Diagnoses
        if (record.getPastDiagnoses() != null && !record.getPastDiagnoses().isEmpty()) {
            String diagnoses = record.getPastDiagnoses().stream()
                .map(d -> d.getDescription() + ";" + d.getDate().format(DATE_FORMATTER))
                .collect(Collectors.joining(","));
            sb.append(diagnoses);
        } else {
            sb.append("NULL");
        }
        sb.append(SEPARATOR);

        // Serialize Treatments using '^' as the separator
        if (record.getPastTreatments() != null && !record.getPastTreatments().isEmpty()) {
            String treatments = record.getPastTreatments().stream()
                .map(Treatment::serialize)
                .collect(Collectors.joining("^")); // Ensure '^' is used here
            sb.append(treatments);
        } else {
            sb.append("NULL");
        }
        // No Assigned Doctor ID field
        // sb.append(SEPARATOR).append(record.getAssignedDoctorId() != null ? record.getAssignedDoctorId() : "NULL");

        return sb.toString();
    }
}
