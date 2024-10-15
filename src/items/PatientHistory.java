package items;
import java.time.LocalDateTime;

public class PatientHistory {
    private final Diagnosis pastDiagnosis;
    private final Treatment pastTreatment;
    private final LocalDateTime appointmentDateTime; // Changed to LocalDateTime
    private final String treatedByDoctor; 

    public PatientHistory(int numIllnesses, int numPrescribedMedicine, LocalDateTime appointment_date_time, String treated_by_doctor) {
        pastDiagnosis = new Diagnosis(numIllnesses);
        pastTreatment = new Treatment(numPrescribedMedicine);
        appointmentDateTime = appointment_date_time;
        treatedByDoctor = treated_by_doctor;
    }

    public void printHistory() {
        pastDiagnosis.printDiagnosedIllnessWithComments();
        pastTreatment.printAllPrescribedMedicine();
        pastTreatment.printTreatmentComments();
        System.out.printf("This appointment was conducted on %s\n", appointmentDateTime);
        System.out.printf("This patient was treated by %s\n", treatedByDoctor);
    }

}
