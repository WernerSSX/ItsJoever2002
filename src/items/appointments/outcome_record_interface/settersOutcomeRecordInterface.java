package items.appointments.outcome_record_interface;

import java.time.LocalDate;
import java.util.List;

import items.Prescription;

public interface settersOutcomeRecordInterface {
    public void setDateOfAppointment(LocalDate dateOfAppointment);
    public void setServiceType(String serviceType);
    public void setPrescribedMedications(List<Prescription> prescribedMedications);
    public void setConsultationNotes(String consultationNotes);
}
