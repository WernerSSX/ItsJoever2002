package db;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import items.Schedule;
import user_classes.Administrator;
import user_classes.Doctor;
import user_classes.Patient;
import user_classes.Pharmacist;
import user_classes.User;

public class UsersLoader extends DataLoader<User> {
    private List<User> users;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public UsersLoader(String filePath) {
        super(filePath);
        this.users = new ArrayList<>();
    }

    @Override
    public void loadData() throws IOException {
        List<String> lines = read(filePath);
        users.clear();
        for (String line : lines) {
            users.add(deserialize(line));
        }
    }

    // Implement the abstract saveData() method from DataLoader
    @Override
    public void saveData() throws IOException {
        List<String> lines = new ArrayList<>();
        for (User record : users) {
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
    protected User deserialize(String userData) {
    	String[] fields = userData.split("\\" + SEPARATOR);
        if (fields.length < 6) {
            throw new IllegalArgumentException("Invalid user data: " + userData);
        }
    
        String hospitalID = fields[0];
        String password = fields[1];
        String name = fields[2];
        LocalDate dateOfBirth = LocalDate.parse(fields[3], DATE_FORMATTER);
        String gender = fields[4];
        String role = fields[5].toLowerCase();
    
        switch (role) {
            case "administrator":
                return new Administrator(hospitalID, password, name, dateOfBirth, gender);
            case "doctor":
                return new Doctor(hospitalID, password, name, dateOfBirth, gender, new Schedule());
            case "patient":
                return new Patient(hospitalID, password, name, dateOfBirth, gender);
            case "pharmacist":
                return new Pharmacist(hospitalID, password, name, dateOfBirth, gender);
            default:
                throw new IllegalArgumentException("Unknown role: " + role);
        }
    }

    /**
     * Retrieves the list of loaded MedicalRecords.
     *
     * @return List of MedicalRecord objects.
     */
    public List<User> getUsers() {
        return Collections.unmodifiableList(users);
    }
    
    protected String serialize(User user) {
    	return String.join(SEPARATOR,
                user.getHospitalID(),
                user.getPassword(),
                user.getName(),
                user.getDateOfBirth().format(DATE_FORMATTER),
                user.getGender(),
                user.getRole());
    }
}
