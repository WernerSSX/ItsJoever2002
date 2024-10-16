package db;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import user_classes.*;
import items.*;

public class TextDB {
    private static TextDB instance;
    public static final String SEPARATOR = "|";
    private static List<User> users;
    private static List<Appointment> appointments;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private TextDB() {
        users = new ArrayList<>();
        appointments = new ArrayList<>();
    }

    public static TextDB getInstance() {
        if (instance == null) {
            instance = new TextDB();
        }
        return instance;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void removeUser(User user) {
        users.remove(user);
    }

    public User getUserByHospitalID(String hospitalID) {
        for (User user : users) {
            if (user.getHospitalID().equals(hospitalID)) {
                return user;
            }
        }
        return null;
    }

    public List<User> getUsers() {
        return Collections.unmodifiableList(users);
    }

    public static void updateUserPassword(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getHospitalID().equals(user.getHospitalID())) {
                users.set(i, user);
                break;
            }
        }
        try {
            saveToFile("users.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveToFile(String filename) throws IOException {
        List<String> stringList = new ArrayList<>();
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

    private static String serializeUser(User user) {
        return String.join(SEPARATOR,
                user.getHospitalID(),
                user.getPassword(),
                user.getName(),
                user.getDateOfBirth().format(DATE_FORMATTER),
                user.getGender(),
                user.getContactInformation().getPhoneNumber(),
                user.getContactInformation().getEmailAddress(),
                user.getRole());
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
        String phone = fields[5];
        String email = fields[6];
        String role = fields[7].toLowerCase();

        ContactInformation contactInformation = new ContactInformation(phone, email);

        switch (role) {
            case "administrator":
                return new Administrator(hospitalID, password, name, dateOfBirth, gender, contactInformation);
            case "doctor":
                return new Doctor(hospitalID, password, name, dateOfBirth, gender, contactInformation, new Schedule(null, null));
            case "patient":
                return new Patient(hospitalID, password, name, dateOfBirth, gender, contactInformation);
            case "pharmacist":
                return new Pharmacist(hospitalID, password, name, dateOfBirth, gender, contactInformation);
            default:
                throw new IllegalArgumentException("Unknown role: " + role);
        }
    }

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

    public boolean cancelAppointment(Patient patient, int appointmentId) {
        boolean removed = appointments.removeIf(appointment -> 
            appointment.getId() == appointmentId && appointment.getPatientId().equals(patient.getHospitalID())
        );

        if (removed) {
            try {
                saveAppointmentsToFile("appts.txt");
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        return removed;
    }

    public List<TimeSlot> getAvailableAppointmentSlots(LocalDate date) {
        List<TimeSlot> allTimeSlots = generateAllTimeSlotsForDate(date);
        List<Appointment> bookedAppointments = loadAppointmentsForDate(date);

        List<TimeSlot> bookedTimeSlots = bookedAppointments.stream()
                .map(Appointment::getTimeSlot)
                .collect(Collectors.toList());

        return allTimeSlots.stream()
                .filter(timeSlot -> !bookedTimeSlots.contains(timeSlot))
                .collect(Collectors.toList());
    }

    private List<TimeSlot> generateAllTimeSlotsForDate(LocalDate date) {
        List<TimeSlot> timeSlots = new ArrayList<>();
        LocalTime startTime = LocalTime.of(9, 0); // Assuming appointments start at 9 AM
        LocalTime endTime = LocalTime.of(17, 0); // Assuming appointments end at 5 PM
        int slotDurationMinutes = 30; // Assuming each slot is 30 minutes

        while (startTime.isBefore(endTime)) {
            LocalDateTime slotStart = LocalDateTime.of(date, startTime);
            LocalDateTime slotEnd = slotStart.plusMinutes(slotDurationMinutes);
            timeSlots.add(new TimeSlot(slotStart, slotEnd, true));
            startTime = startTime.plusMinutes(slotDurationMinutes);
        }

        return timeSlots;
    }

    private List<Appointment> loadAppointmentsForDate(LocalDate date) {
        List<Appointment> appointmentsForDate = new ArrayList<>();
        try {
            List<String> lines = read("appts.txt");
            for (String line : lines) {
                Appointment appointment = deserializeAppointment(line);
                if (appointment.getTimeSlot().getStartTime().toLocalDate().equals(date)) {
                    appointmentsForDate.add(appointment);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return appointmentsForDate;
    }


    // Appointment management methods
    public boolean addAppointment(Patient patient, LocalDate date, TimeSlot timeSlot) {
        // Check if the timeSlot is valid for the given date
        List<TimeSlot> availableSlots = getAvailableAppointmentSlots(date);
        if (!availableSlots.contains(timeSlot)) {
            System.out.println("The selected time slot is not available.");
            return false;
        }
    
        // Generate a new unique appointment ID
        int newAppointmentId = appointments.size() + 1; // Assuming appointment ID increments
    
        // Create the new appointment
        Appointment newAppointment = new Appointment(newAppointmentId, 
                                                     patient.getHospitalID(), 
                                                     "doctorID_placeholder", // Placeholder for now, can be assigned later
                                                     timeSlot, 
                                                     "Scheduled",
                                                     "Booked");
    
        // Add the new appointment to the list
        appointments.add(newAppointment);
        
        // Save the appointment to the file for persistence
        try {
            saveAppointmentsToFile("appts.txt");
        } catch (IOException e) {
            System.out.println("Failed to save the appointment to the file.");
            e.printStackTrace();
            return false;
        }
    
        System.out.println("Appointment successfully scheduled for " + date + " at " + timeSlot);
        return true;
    }
    

    public void removeAppointment(Appointment appointment) {
        appointments.remove(appointment);
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void loadAppointmentsFromFile(String filename) throws IOException {
        List<String> stringArray = read(filename);
        appointments.clear();
        for (String line : stringArray) {
            appointments.add(deserializeAppointment(line));
        }
    }

    public static void saveAppointmentsToFile(String filename) throws IOException {
        List<String> stringList = new ArrayList<>();
        for (Appointment appointment : appointments) {
            stringList.add(serializeAppointment(appointment));
        }
        write(filename, stringList);
    }

    private static String serializeAppointment(Appointment appointment) {
        return String.join(SEPARATOR,
                String.valueOf(appointment.getId()),
                appointment.getPatientId(),
                appointment.getDoctorId(),
                serializeTimeSlot(appointment.getTimeSlot()),
                appointment.getStatus(),
                appointment.getOutcomeRecord());
    }
    
    private static String serializeTimeSlot(TimeSlot timeSlot) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        return timeSlot.getStartTime().format(formatter) + "-" + timeSlot.getEndTime().format(formatter);
    }
    
    private Appointment deserializeAppointment(String appointmentData) {
        String[] fields = appointmentData.split("\\" + SEPARATOR);
        if (fields.length < 6) {
            throw new IllegalArgumentException("Invalid appointment data: " + appointmentData);
        }
    
        int id = Integer.parseInt(fields[0]);
        String patientId = fields[1];
        String doctorId = fields[2];
        TimeSlot timeSlot = TimeSlot.parse(fields[3]);
        String status = fields[4];
        String outcomeRecord = fields[5];
    
        return new Appointment(id, patientId, doctorId, timeSlot, status, outcomeRecord);
    }
    
}
