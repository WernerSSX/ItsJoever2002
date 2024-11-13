package db;

import items.Schedule;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import user_classes.Administrator;
import user_classes.Doctor;
import user_classes.Patient;
import user_classes.Pharmacist;
import user_classes.User;

/**
 * @class UsersLoader
 * @brief Manages loading and saving of User objects (e.g., Administrator, Doctor, Patient, Pharmacist) from/to a file.
 *
 * This class extends DataLoader to handle various User types, reading from and writing to a file.
 */
public class UsersLoader extends DataLoader<User> {
    private List<User> users; /**< List to store loaded User objects. */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd"); /**< Date formatter for serialization. */
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm"); /**< Time formatter for serialization. */

    /**
     * @brief Constructs a UsersLoader with the specified file path.
     *
     * @param filePath Path to the file associated with this UsersLoader.
     */
    public UsersLoader(String filePath) {
        super(filePath);
        this.users = new ArrayList<>();
    }

    /**
     * @brief Loads user data from the specified file.
     *
     * Reads each line from the file, deserializing each line into a User object (specific type depends on role).
     *
     * @throws IOException If an I/O error occurs during file reading.
     */
    @Override
    public void loadData() throws IOException {
        List<String> lines = read(filePath);
        users.clear();
        for (String line : lines) {
            users.add(deserialize(line));
        }
    }

    /**
     * @brief Saves all loaded user data to the associated file.
     *
     * Serializes each User object and writes it as a line in the file.
     *
     * @throws IOException If an I/O error occurs during file writing.
     */
    @Override
    public void saveData() throws IOException {
        List<String> lines = new ArrayList<>();
        for (User record : users) {
            lines.add(serialize(record));
        }
        write(filePath, lines);
    }

    /**
     * @brief Deserializes a User object from a string representation.
     *
     * Expected Format: hospitalID|password|name|dateOfBirth|gender|role
     * 
     * Based on the role, this method returns an appropriate subclass of User.
     *
     * @param userData Serialized string representation of the User.
     * @return Deserialized User object.
     * @throws IllegalArgumentException If the data format is invalid or if the role is unknown.
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
     * @brief Retrieves the list of loaded User objects.
     *
     * @return Unmodifiable list of User objects.
     */
    public List<User> getUsers() {
        return Collections.unmodifiableList(users);
    }

    /**
     * @brief Serializes a User object into a string representation.
     *
     * Format: hospitalID|password|name|dateOfBirth|gender|role.
     *
     * @param user User object to serialize.
     * @return Serialized string representation of the User.
     */
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
