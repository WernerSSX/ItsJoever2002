package items.appointments.outcome_record_interface;

import java.time.LocalDate;
import java.util.List;

import items.Prescription;

public interface gettersOutcomeRecordInterface {
    public LocalDate getDateOfAppointment();
    public String getServiceType();
    public List<Prescription> getPrescribedMedications();
    public String getConsultationNotes();
}
