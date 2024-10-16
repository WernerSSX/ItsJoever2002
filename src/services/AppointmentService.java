package services;

import db.TextDB;
import user_classes.Patient;
import items.Appointment;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class AppointmentService {
    private static TextDB textDB;

    public AppointmentService(TextDB textDB) {
        this.textDB = TextDB.getInstance();
    }

    public static List<Appointment> getPastAppointments(Patient patient) {
        return textDB.getAppointments().stream()
                .filter(appointment -> appointment.getPatientId().equals(patient.getHospitalID()) && appointment.isPast())
                .collect(Collectors.toList());
    }
    public List<Appointment> getCurrentAppointments() {
        List<Appointment> allAppointments = textDB.getAppointments();
        LocalDate today = LocalDate.now();
        return allAppointments.stream()
                .filter(appointment -> !appointment.getDate().isBefore(today))
                .collect(Collectors.toList());
    }
}