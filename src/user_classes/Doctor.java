package user_classes;
import items.ContactInformation;
import items.Schedule;
import java.time.LocalDate;

public class Doctor extends User {
    private Schedule availability;

    public Doctor(String hospitalID, String password, String name, LocalDate dateOfBirth, String gender, ContactInformation contactInformation, Schedule availability) {
        super(hospitalID, password, name, dateOfBirth, gender, contactInformation);
        this.availability = availability;
        this.role = "Doctor";
    }

    public Schedule getAvailability() {
        return availability;
    }

    public void setAvailability(Schedule availability) {
        this.availability = availability;
    }

    @Override
    public void login() {
        // Implementation for doctor login
    }

    @Override
    public void changePassword() {
        // Implementation for changing doctor password
    }

    @Override
    public void logout() {
        // Implementation for doctor logout
    }

    public void viewPatientMedicalRecords() {
        // Implementation for viewing patient medical records
    }

    public void updatePatientMedicalRecords() {
        // Implementation for updating patient medical records
    }

    public void viewPersonalSchedule() {
        // Implementation for viewing personal schedule
    }

    public void setAvailability() {
        // Implementation for setting availability
    }

    public void acceptOrDeclineAppointmentRequests() {
        // Implementation for accepting or declining appointment requests
    }

    public void viewUpcomingAppointments() {
        // Implementation for viewing upcoming appointments
    }

    public void recordAppointmentOutcome() {
        // Implementation for recording appointment outcome
    }
}