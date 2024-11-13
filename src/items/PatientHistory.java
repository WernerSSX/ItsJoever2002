package items;
import java.time.LocalDateTime;

// This file is outdated and will not be used in the implementation
public class PatientHistory {
    private final Diagnosis pastDiagnosis;
    private final Treatment pastTreatment;
    private final LocalDateTime appointmentDateTime; // Changed to LocalDateTime
    private final String treatedByDoctor; 

    public PatientHistory() {
        pastDiagnosis = null;
        pastTreatment = null;
        appointmentDateTime = LocalDateTime.now();
        treatedByDoctor = "Dr. John Doe";
    }

    public void printHistory() {
        pastDiagnosis.printDiagnosedIllnessWithComments();
        pastTreatment.printAllPrescribedMedicine();
        pastTreatment.printTreatmentComments();
        System.out.printf("This appointment was conducted on %s\n", appointmentDateTime);
        System.out.printf("This patient was treated by %s\n", treatedByDoctor);
    }

}
