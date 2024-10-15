package db;

import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.FileInputStream;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import user_classes.*;
import items.*;
import java.util.Collections; // Import Collections

public class TextDB {
    public static final String SEPARATOR = "|";
    private List<User> users;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public TextDB() {
        users = new ArrayList<>();
    }

    public void addUser(User user) {
        users.add(user);
    }

    public List<User> getUsers() {
        return Collections.unmodifiableList(users);
    }

    public void saveToFile(String filename) throws IOException {
        List<String> stringList = new ArrayList<>(); // to store User data

        for (User user : users) {
            stringList.add(serializeUser(user));
        }
        write(filename, stringList);
    }

    public void loadFromFile(String filename) throws IOException {
        List<String> stringArray = read(filename);
        users.clear();

        for (String line : stringArray) {
            users.add(deserializeUser(line));
        }
    }

    private String serializeUser(User user) {
        // Updated to use SEPARATOR and include role as the last field
        return String.join(SEPARATOR,
                user.getHospitalID(),
                user.getPassword(),
                user.getName(),
                user.getDateOfBirth().format(DATE_FORMATTER),
                user.getGender(),
                user.getContactInformation().getEmailAddress(),
                user.getContactInformation().getPhoneNumber(),
                user.getClass().getSimpleName()
        );
    }

    private User deserializeUser(String userData) {
        String[] fields = userData.split("\\" + SEPARATOR);
        if (fields.length < 8) {
            throw new IllegalArgumentException("Invalid user data: " + userData);
        }

        String hospitalID = fields[0];
        String password = fields[1];
        String name = fields[2];
        LocalDate dateOfBirth = LocalDate.parse(fields[3], DATE_FORMATTER);
        String gender = fields[4];
        String email = fields[5];
        String phone = fields[6];
        String role = fields[7];

        ContactInformation contactInformation = new ContactInformation(email, phone);

        // Create user based on role
        switch (role.toLowerCase()) {
            case "administrator":
                return new Administrator(hospitalID, password, name, dateOfBirth, gender, contactInformation);
            case "doctor":
                return new Doctor(hospitalID, password, name, dateOfBirth, gender, contactInformation, null); // Add additional parameters as needed
            case "patient":
                return new Patient(hospitalID, password, name, dateOfBirth, gender, contactInformation);
            case "pharmacist":
                return new Pharmacist(hospitalID, password, name, dateOfBirth, gender, contactInformation);
            default:
                throw new IllegalArgumentException("Unknown role: " + role);
        }
    }

    // Helper method to write to the file
    public static void write(String fileName, List<String> data) throws IOException {
        PrintWriter out = new PrintWriter(new FileWriter(fileName));

        try {
            for (String line : data) {
                out.println(line);
            }
        } finally {
            out.close();
        }
    }

    // Helper method to read from the file
    public static List<String> read(String fileName) throws IOException {
        List<String> data = new ArrayList<>();
        Scanner scanner = new Scanner(new FileInputStream(fileName));
        try {
            while (scanner.hasNextLine()) {
                data.add(scanner.nextLine());
            }
        } finally {
            scanner.close();
        }
        return data;
    }
}
