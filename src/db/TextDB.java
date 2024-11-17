package db;

import items.*;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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

/**
 * @class TextDB
 * @brief This class 
 */
public class TextDB {
	private List<DataLoader> loaders;
    private static TextDB instance;
    private List<MedicalRecord> medicalRecords;
    public static final String SEPARATOR = "|";
    private static List<User> users;
    private static List<Appointment> appointments;
    private List<Medication> medications;
    public List<ReplenishmentRequest> replenishmentRequests;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * @brief Default constructor.
     */
    private TextDB() {
    	loaders = new ArrayList<>();
    	loaders.add(new MedicalRecordLoader("med_records.txt"));
    	loaders.add(new UsersLoader("users.txt"));
    	loaders.add(new AppointmentsLoader("appts.txt"));
    	loaders.add(new MedicationInventoryLoader("inventory.txt"));
    	loaders.add(new ReplenishmentRequestsLoader("replenishment_requests.txt"));
        users = new ArrayList<>();
        appointments = new ArrayList<>();
        medicalRecords = new ArrayList<>();
        medications = new ArrayList<>();
        replenishmentRequests = new ArrayList<>();
    }
    
    /**
     * @brief Calculates the result.
     *
     * @param a First parameter for calculation.
     * @param b Second parameter for calculation.
     * @return The calculated result.
     */
    public static TextDB getInstance() {
        if (instance == null) {
            instance = new TextDB();
            try {
                instance.loadAllData();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    /**
     * Loads all data including users, appointments, medical records, and schedules.
     */
    private void loadAllData() throws IOException {
    	for (DataLoader loader : loaders) {
            loader.loadData();
            if (loader instanceof MedicalRecordLoader) {
                medicalRecords = new ArrayList<>(((MedicalRecordLoader) loader).getMedicalRecords());
            }
            if (loader instanceof UsersLoader) {
            	users = new ArrayList<>(((UsersLoader) loader).getUsers());
            }
            if (loader instanceof AppointmentsLoader) {
            	appointments = new ArrayList<>(((AppointmentsLoader) loader).getAppointments());
            }
            if (loader instanceof MedicationInventoryLoader) {
            	medications = new ArrayList<>(((MedicationInventoryLoader) loader).getMedicationInventory());
            }
            if (loader instanceof ReplenishmentRequestsLoader) {
            	replenishmentRequests = new ArrayList<>(((ReplenishmentRequestsLoader) loader).getReplenishmentRequests());
            }
        }
    	/*
        loadFromFile("users.txt");
        loadAppointmentsFromFile("appts.txt");
        loadMedicationInventory("inventory.txt");
        loadReplenishmentRequests("replenishment_requests.txt");
        */
    	
        loadSchedulesFromFile("schedules.txt");
    }
    


    // Existing methods for Users, Appointments, and MedicalRecords...

    // ====================== Schedule Management ========================= //

    /**
     * Loads schedules from the specified file and assigns them to respective doctors.
     *
     * @param filename The name of the schedules file.
     * @throws IOException If an I/O error occurs.
     */
    public void loadSchedulesFromFile(String filename) throws IOException {
        List<String> lines = read(filename);
        for (String line : lines) {
            String[] fields = line.split("\\" + SEPARATOR);
            if (fields.length < 3) {
                System.err.println("Invalid schedule entry: " + line);
                continue;
            }

            String doctorId = fields[0];
            LocalDate date = LocalDate.parse(fields[1], DATE_FORMATTER);
            String timeSlotsStr = fields[2];

            List<TimeSlot> timeSlots = new ArrayList<>();
            if (!timeSlotsStr.isEmpty()) {
                String[] slots = timeSlotsStr.split(",");
                for (String slot : slots) {
                    String[] times = slot.split("-");
                    if (times.length != 2) {
                        System.err.println("Invalid time slot format: " + slot);
                        continue;
                    }
                    LocalTime startTime = LocalTime.parse(times[0], TIME_FORMATTER);
                    LocalTime endTime = LocalTime.parse(times[1], TIME_FORMATTER);

                    // Split the time range into 30-minute slots
                    List<TimeSlot> splitSlots = splitInto30MinSlots(date, startTime, endTime);
                    timeSlots.addAll(splitSlots);
                }
            }

            // Assign the time slots to the doctor's schedule
            Doctor doctor = (Doctor) getUserByHospitalID(doctorId);
            if (doctor != null) {
                doctor.getSchedule().setAvailability(date, timeSlots);
            } else {
                System.err.println("Doctor with ID " + doctorId + " not found.");
            }
        }
    }

        /**
     * Splits a given time range into 30-minute TimeSlots.
     *
     * @param date      The date of the slots.
     * @param startTime The start time of the range.
     * @param endTime   The end time of the range.
     * @return A list of 30-minute TimeSlots.
     */
    private List<TimeSlot> splitInto30MinSlots(LocalDate date, LocalTime startTime, LocalTime endTime) {
        List<TimeSlot> slots = new ArrayList<>();
        LocalDateTime slotStart = LocalDateTime.of(date, startTime);
        LocalDateTime slotEnd = slotStart.plusMinutes(30);

        while (!slotStart.toLocalTime().isAfter(endTime.minusMinutes(30))) {
            // Ensure that slotEnd does not exceed the overall endTime
            if (slotEnd.toLocalTime().isAfter(endTime)) {
                slotEnd = LocalDateTime.of(date, endTime);
            }
            slots.add(new TimeSlot(slotStart, slotEnd, true));
            slotStart = slotEnd;
            slotEnd = slotStart.plusMinutes(30);
        }

        return slots;
    }

    
    

    /**
     * Saves all doctors' schedules to the specified file.
     *
     * @param filename The name of the schedules file.
     * @throws IOException If an I/O error occurs.
     */
    public void saveSchedulesToFile(String filename) throws IOException {
        List<String> lines = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Doctor) {
                Doctor doctor = (Doctor) user;
                Schedule schedule = doctor.getSchedule();
                for (LocalDate date : schedule.getAvailability().keySet()) {
                    List<TimeSlot> slots = schedule.getAvailableTimeSlots(date);
                    StringBuilder slotsStr = new StringBuilder();
                    for (int i = 0; i < slots.size(); i++) {
                        TimeSlot slot = slots.get(i);
                        slotsStr.append(slot.getStartTime().toLocalTime().format(TIME_FORMATTER))
                                .append("-")
                                .append(slot.getEndTime().toLocalTime().format(TIME_FORMATTER));
                        if (i < slots.size() - 1) {
                            slotsStr.append(",");
                        }
                    }
                    String line = String.join(SEPARATOR,
                            doctor.getHospitalID(),
                            date.format(DATE_FORMATTER),
                            slotsStr.toString()
                    );
                    lines.add(line);
                }
            }
        }
        write(filename, lines);
    }


    /**
     * Updates a doctor's schedule and saves the changes to the schedules file.
     *
     * @param doctorId The ID of the doctor whose schedule is to be updated.
     * @param schedule The updated Schedule object.
     * @throws IOException If an I/O error occurs.
     */
    public void updateDoctorSchedule(String doctorId, Schedule schedule) throws IOException {
        Doctor doctor = (Doctor) getUserByHospitalID(doctorId);
        if (doctor != null) {
            doctor.setSchedule(schedule);
            saveSchedulesToFile("schedules.txt");
        } else {
            System.err.println("Doctor with ID " + doctorId + " not found.");
        }
    }

    // ====================== Existing Methods ========================= //

    /**
     * @brief Sets the object's name.
     *
     * @param name The new name.
     */
    public void addUser(User user) {
        users.add(user);
    }

    /**
     * @brief Sets the object's name.
     *
     * @param name The new name.
     */
    public void removeUser(User user) {
        users.remove(user);
    }

    /**
     * @brief Calculates the result.
     *
     * @param a First parameter for calculation.
     * @param b Second parameter for calculation.
     * @return The calculated result.
     */
    public User getUserByHospitalID(String hospitalID) {
        for (User user : users) {
            if (user.getHospitalID().equals(hospitalID)) {
                return user;
            }
        }
        return null;
    }

    /**
     * @brief Calculates the result.
     *
     * @param a First parameter for calculation.
     * @param b Second parameter for calculation.
     * @return The calculated result.
     */
    public List<User> getUsers() {
        return Collections.unmodifiableList(users);
    }

    /**
     * @brief Sets the object's name.
     *
     * @param name The new name.
     */
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

    /**
     * @brief Sets the object's name.
     *
     * @param name The new name.
     */
    public static void saveToFile(String filename) throws IOException {
        List<String> stringList = new ArrayList<>();
        for (User user : users) {
            stringList.add(serializeUser(user));
        }
        write(filename, stringList);
    }

    private static String serializeUser(User user) {
        return String.join(SEPARATOR,
                user.getHospitalID(),
                user.getPassword(),
                user.getName(),
                user.getDateOfBirth().format(DATE_FORMATTER),
                user.getGender(),
                user.getRole());
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

    /**
     * @brief Calculates the result.
     *
     * @param a First parameter for calculation.
     * @param b Second parameter for calculation.
     * @return The calculated result.
     */
    public static List<String> read(String fileName) throws IOException {
        List<String> data = new ArrayList<>();
        FileInputStream fis = null;
        Scanner scanner = null;
        try {
            fis = new FileInputStream(fileName);
            scanner = new Scanner(fis);
            while (scanner.hasNextLine()) {
                data.add(scanner.nextLine());
            }
        } finally {
            if (scanner != null) {
                scanner.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        return data;
    }

    // ====================== Appointment Management ========================= //

    /**
     * @brief Calculates the result.
     *
     * @param a First parameter for calculation.
     * @param b Second parameter for calculation.
     * @return The calculated result.
     */
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

    /**
     * @brief Calculates the result.
     *
     * @param a First parameter for calculation.
     * @param b Second parameter for calculation.
     * @return The calculated result.
     */
    private int generateNewAppointmentId() {
        return appointments.stream()
                           .mapToInt(Appointment::getId)
                           .max()
                           .orElse(0) + 1;
    }

    /**
     * @brief Calculates the result.
     *
     * @param a First parameter for calculation.
     * @param b Second parameter for calculation.
     * @return The calculated result.
     */
    public List<Doctor> getAllDoctors() {
        return users.stream()
                    .filter(user -> user instanceof Doctor)
                    .map(user -> (Doctor) user)
                    .collect(Collectors.toList());
    }

    /**
     * @brief Calculates the result.
     *
     * @param a First parameter for calculation.
     * @param b Second parameter for calculation.
     * @return The calculated result.
     */
    public List<TimeSlot> getAvailableAppointmentSlots(LocalDate date, Doctor doctor) {
        // Retrieve the doctor's available slots for the date
        List<TimeSlot> doctorAvailableSlots = doctor.getAvailableTimeSlots(date);
        
        if (doctorAvailableSlots.isEmpty()) {
            // Doctor has not set availability for this date
            return Collections.emptyList();
        }

        // Retrieve booked and requested appointments for the doctor on the date
        List<Appointment> bookedOrRequestedAppointments = appointments.stream()
                .filter(appt -> appt.getDoctorId().equals(doctor.getHospitalID()) &&
                                appt.getDate().equals(date) &&
                                (appt.getStatus().equalsIgnoreCase("Scheduled") || appt.getStatus().equalsIgnoreCase("Requested")))
                .collect(Collectors.toList());

        // Extract the booked TimeSlots
        List<TimeSlot> occupiedSlots = bookedOrRequestedAppointments.stream()
                .map(Appointment::getTimeSlot)
                .collect(Collectors.toList());

        // Filter out the occupied slots from the doctor's available slots
        List<TimeSlot> availableSlots = doctorAvailableSlots.stream()
                .filter(slot -> !occupiedSlots.contains(slot))
                .collect(Collectors.toList());

        return availableSlots;
    }

    /**
     * @brief Calculates the result.
     *
     * @param a First parameter for calculation.
     * @param b Second parameter for calculation.
     * @return The calculated result.
     */
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

    /**
     * @brief Calculates the result.
     *
     * @param a First parameter for calculation.
     * @param b Second parameter for calculation.
     * @return The calculated result.
     */
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

    /**
     * @brief Calculates the result.
     *
     * @param a First parameter for calculation.
     * @param b Second parameter for calculation.
     * @return The calculated result.
     */
    private List<Appointment> loadAppointmentsForDateAndDoctor(LocalDate date, Doctor doctor) {
        List<Appointment> appointmentsForDateAndDoctor = new ArrayList<>();
        try {
            List<String> lines = read("appts.txt");
            for (String line : lines) {
                Appointment appointment = deserializeAppointment(line);
                if (appointment.getDoctorId().equals(doctor.getHospitalID()) &&
                    appointment.getTimeSlot().getStartTime().toLocalDate().equals(date)) {
                    appointmentsForDateAndDoctor.add(appointment);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return appointmentsForDateAndDoctor;
    }

    /**
     * @brief Calculates the result.
     *
     * @param a First parameter for calculation.
     * @param b Second parameter for calculation.
     * @return The calculated result.
     */
    // Appointment management methods
    public boolean addAppointment(Patient patient, Doctor doctor, LocalDate date, TimeSlot timeSlot) {
        List<TimeSlot> availableSlots = getAvailableAppointmentSlots(date, doctor);
        if (!availableSlots.contains(timeSlot)) {
            System.out.println("The selected time slot is not available.");
            return false;
        }

        // Generate a new unique appointment ID
        int newAppointmentId = generateNewAppointmentId();

        // Create the new appointment with status "Requested"
        Appointment newAppointment = new Appointment(newAppointmentId, 
                                                    patient.getHospitalID(), 
                                                    doctor.getHospitalID(), 
                                                    timeSlot, 
                                                    "Requested",
                                                    "Pending");

        // Add the new appointment to the list
        appointments.add(newAppointment);
        
        // Mark the TimeSlot as unavailable to prevent double booking
        timeSlot.setAvailable(false);

        // Save the appointment to the file for persistence
        try {
            saveAppointmentsToFile("appts.txt");
            saveSchedulesToFile("schedules.txt"); // Save the updated schedule
        } catch (IOException e) {
            System.out.println("Failed to save the appointment to the file.");
            e.printStackTrace();
            return false;
        }

        System.out.println("Appointment request successfully submitted with Dr. " + doctor.getName() + " on " + date + " at " + timeSlot + ".");
        System.out.println("Please await doctor's approval.");
        return true;
    }

    /**
     * @brief Calculates the result.
     *
     * @param a First parameter for calculation.
     * @param b Second parameter for calculation.
     * @return The calculated result.
     */
    public List<Appointment> getRequestedAppointmentsByDoctor(String doctorId) {
        return appointments.stream()
                .filter(appt -> appt.getDoctorId().equals(doctorId) && appt.getStatus().equalsIgnoreCase("Requested"))
                .collect(Collectors.toList());
    }

    /**
     * @brief Calculates the result.
     *
     * @param a First parameter for calculation.
     * @param b Second parameter for calculation.
     * @return The calculated result.
     */
    public Appointment getAppointmentById(int appointmentId) {
        for (Appointment appt : appointments) {
            if (appt.getId() == appointmentId) {
                return appt;
            }
        }
        return null;
    }

    /**
     * @brief Calculates the result.
     *
     * @param a First parameter for calculation.
     * @param b Second parameter for calculation.
     * @return The calculated result.
     */
    public void updateAppointmentStatus(int appointmentId, String newStatus) throws IOException {
        Appointment appointment = getAppointmentById(appointmentId);
        if (appointment != null) {
            appointment.setStatus(newStatus);
            
            switch (newStatus.toLowerCase()) {
                case "completed":
                    // Do not set outcomeRecord here; it will be handled in recordAppointmentOutcome
                    break;
                case "declined":
                case "cancelled":
                case "scheduled":
                    // Make the TimeSlot available again if necessary
                    if (newStatus.equalsIgnoreCase("declined") || newStatus.equalsIgnoreCase("cancelled")) {
                        appointment.getTimeSlot().setAvailable(true);
                    }
                    // Set outcomeRecord to "NULL" since there's no detailed outcome
                    appointment.setOutcomeRecord("NULL");
                    break;
                default:
                    // Handle other statuses if any
                    appointment.setOutcomeRecord("NULL");
                    break;
            }
            
            saveAppointmentsToFile("appts.txt");
            saveSchedulesToFile("schedules.txt"); // Save updated TimeSlot availability
        } else {
            System.err.println("Appointment with ID " + appointmentId + " not found.");
        }
    }
    
    /**
     * @brief Calculates the result.
     *
     * @param a First parameter for calculation.
     * @param b Second parameter for calculation.
     * @return The calculated result.
     */
    public void removeAppointment(Appointment appointment) {
        appointments.remove(appointment);
    }

    /**
     * @brief Calculates the result.
     *
     * @param a First parameter for calculation.
     * @param b Second parameter for calculation.
     * @return The calculated result.
     */
    public List<Appointment> getAppointments() {
        return appointments;
    }

    public static void saveAppointmentsToFile(String filename) throws IOException {
        List<String> stringList = new ArrayList<>();
        for (Appointment appointment : appointments) {
            stringList.add(serializeAppointment(appointment));
        }
        write(filename, stringList);
    }

    /**
     * @brief Calculates the result.
     *
     * @param a First parameter for calculation.
     * @param b Second parameter for calculation.
     * @return The calculated result.
     */
    private static String serializeAppointment(Appointment appointment) {
        return String.join(SEPARATOR,
                String.valueOf(appointment.getId()),
                appointment.getPatientId(),
                appointment.getDoctorId(),
                serializeTimeSlot(appointment.getTimeSlot()),
                appointment.getStatus(),
                appointment.getOutcomeRecord());
    }
    
    /**
     * @brief Calculates the result.
     *
     * @param a First parameter for calculation.
     * @param b Second parameter for calculation.
     * @return The calculated result.
     */
    private static String serializeTimeSlot(TimeSlot timeSlot) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        return timeSlot.getStartTime().format(formatter) + "-" + timeSlot.getEndTime().format(formatter);
    }
    
    /**
     * @brief Calculates the result.
     *
     * @param a First parameter for calculation.
     * @param b Second parameter for calculation.
     * @return The calculated result.
     */
    private Appointment deserializeAppointment(String appointmentData) {
        String[] fields = appointmentData.split("\\" + SEPARATOR);
        
        if (fields.length < 6) {
            System.err.println("Invalid appointment data: " + appointmentData);
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

    // Additional Appointment Management Enhancements...

    // ====================== Doctor Assignment and Medical Records ========================= //

    /**
     * @brief Calculates the result.
     *
     * @param a First parameter for calculation.
     * @param b Second parameter for calculation.
     * @return The calculated result.
     */
    public void addMedicalRecord(MedicalRecord record) throws IOException {
        medicalRecords.add(record);
        saveMedicalRecordsToFile("med_records.txt");
    }
    
    private void saveMedicalRecordsToFile(String filename) throws IOException {
        List<String> stringList = new ArrayList<>();
        for (MedicalRecord record : medicalRecords) {
            stringList.add(serializeMedicalRecord(record));
        }
        write(filename, stringList);
    }
    

    /**
     * Serializes the MedicalRecord object into a string.
     *
     * Format:
     * patientID|name|dateOfBirth|gender|phone|email|bloodType|diag1;date1,diag2;date2|treatment1^treatment2|NULL
     *
     * Note: The last field for Assigned Doctor ID is removed.
     *
     * @return Serialized string representation of the MedicalRecord
     */
    private String serializeMedicalRecord(MedicalRecord record) {
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
    

    /**
     * Retrieves the MedicalRecord object for a given patient ID.
     *
     * @param patientId The hospital ID of the patient.
     * @return The MedicalRecord object, or null if not found.
     */
    public MedicalRecord getMedicalRecordByPatientId(String patientId) {
        for (MedicalRecord record : medicalRecords) {
            if (record.getPatientID().equals(patientId)) {
                return record;
            }
        }
        return null;
    }

    /**
     * Updates an existing MedicalRecord in the list and persists the changes.
     *
     * @param updatedRecord The updated MedicalRecord object.
     * @throws IOException If an I/O error occurs during saving.
     */
    public void updateMedicalRecord(MedicalRecord updatedRecord) throws IOException {
        boolean found = false;
        for (int i = 0; i < medicalRecords.size(); i++) {
            if (medicalRecords.get(i).getPatientID().equals(updatedRecord.getPatientID())) {
                medicalRecords.set(i, updatedRecord);
                found = true;
                break;
            }
        }
    
        if (found) {
            saveMedicalRecordsToFile("med_records.txt");
        } else {
            System.err.println("Medical record for patient ID " + updatedRecord.getPatientID() + " not found.");
        }
    }
    
    

    // Additional Appointment Management Enhancements
    /**
     * @brief Retrieves appointments associated with a specific doctor by their ID.
     *
     * @param doctorId The ID of the doctor.
     * @return List of appointments for the specified doctor.
     */
    public List<Appointment> getAppointmentsByDoctorId(String doctorId) {
        return appointments.stream()
                .filter(appt -> appt.getDoctorId().equals(doctorId))
                .collect(Collectors.toList());
    }
    
    /**
     * @brief Retrieves pending appointments for a specific doctor.
     *
     * @param doctorId The ID of the doctor.
     * @return List of pending appointments for the specified doctor.
     */
    public List<Appointment> getPendingAppointmentsByDoctorId(String doctorId) {
        return appointments.stream()
                .filter(appt -> appt.getDoctorId().equals(doctorId) && appt.getStatus().equalsIgnoreCase("Pending"))
                .collect(Collectors.toList());
    }
    
    /**
     * @brief Retrieves upcoming appointments for a specific doctor, excluding declined ones.
     *
     * @param doctorId The ID of the doctor.
     * @return List of upcoming appointments for the specified doctor.
     */
    public List<Appointment> getUpcomingAppointmentsByDoctorId(String doctorId) {
        LocalDateTime now = LocalDateTime.now();
        return appointments.stream()
                .filter(appt -> appt.getDoctorId().equals(doctorId) &&
                                appt.getTimeSlot().getStartTime().isAfter(now) &&
                                !appt.getStatus().equalsIgnoreCase("Declined"))
                .collect(Collectors.toList());
    }
    
    

    /**
     * @brief Updates an appointment with a new status and saves changes.
     *
     * @param updatedAppt The appointment with updated details.
     * @throws IOException If an I/O error occurs while saving appointments or schedules.
     */
    public void updateAppointment(Appointment updatedAppt) throws IOException {
        for (int i = 0; i < appointments.size(); i++) {
            if (appointments.get(i).getId() == updatedAppt.getId()) {
                appointments.set(i, updatedAppt);
                // Update TimeSlot availability based on status
                if (updatedAppt.getStatus().equalsIgnoreCase("Scheduled")) {
                    // Slot already marked as unavailable during request
                    // No action needed
                } else if (updatedAppt.getStatus().equalsIgnoreCase("Declined")) {
                    // Make the TimeSlot available again
                    updatedAppt.getTimeSlot().setAvailable(true);
                }
                saveAppointmentsToFile("appts.txt");
                saveSchedulesToFile("schedules.txt"); // Save updated TimeSlot availability
                break;
            }
        }
    }



// ====================== Medication and prescription ========================= //


    /**
     * Saves medication inventory to the specified file.
     *
     * @param filename The name of the inventory file.
     * @throws IOException If an I/O error occurs.
     */
    public void saveMedicationInventory(String filename) throws IOException {
        List<String> lines = new ArrayList<>();
        for (Medication med : medications) {
            lines.add(serializeMedication(med));
        }
        write(filename, lines);
    }

    private String serializeMedication(Medication medication) {
        return String.join("|",
                medication.getName(),
                String.valueOf(medication.getQuantity()),
                medication.getSupplier() != null ? medication.getSupplier() : "NULL"
        );
    }

    /**
     * Retrieves an unmodifiable list of medications.
     *
     * @return Unmodifiable list of medications.
     */
    public List<Medication> getMedications() {
        //return Collections.unmodifiableList(medications);
        return medications;
    }

    /**
     * Updates a medication in the inventory.
     *
     * @param updatedMedication The updated Medication object.
     */
    public void updateMedication(Medication updatedMedication) throws IOException {
        boolean found = false;
        for (int i = 0; i < medications.size(); i++) {
            if (medications.get(i).getName().equalsIgnoreCase(updatedMedication.getName())) {
                medications.set(i, updatedMedication);
                found = true;
                break;
            }
        }
        if (found) {
            saveMedicationInventory("inventory.txt");
        } else {
            System.err.println("Medication " + updatedMedication.getName() + " not found in inventory.");
        }
    }

    /**
     * Saves replenishment requests to the specified file.
     *
     * @param filename The name of the replenishment requests file.
     * @throws IOException If an I/O error occurs.
     */
    public void saveReplenishmentRequests(String filename) throws IOException {
        List<String> lines = new ArrayList<>();
        for (ReplenishmentRequest request : replenishmentRequests) {
            lines.add(request.serialize());
        }
        write(filename, lines);
    }

    /**
     * Adds a new replenishment request.
     *
     * @param request The ReplenishmentRequest object to add.
     */
    public void addReplenishmentRequest(ReplenishmentRequest request) throws IOException {
        replenishmentRequests.add(request);
        saveReplenishmentRequests("replenishment_requests.txt");
    }





}
