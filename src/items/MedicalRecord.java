package items;
import java.util.List;

public class MedicalRecord {
    private String patientID;
    private List<Diagnosis> pastDiagnoses;
    private List<Treatment> pastTreatments;

    public MedicalRecord(String patientID, List<Diagnosis> pastDiagnoses, List<Treatment> pastTreatments) {
        this.patientID = patientID;
        this.pastDiagnoses = pastDiagnoses;
        this.pastTreatments = pastTreatments;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public List<Diagnosis> getPastDiagnoses() {
        return pastDiagnoses;
    }

    public void setPastDiagnoses(List<Diagnosis> pastDiagnoses) {
        this.pastDiagnoses = pastDiagnoses;
    }

    public List<Treatment> getPastTreatments() {
        return pastTreatments;
    }

    public void setPastTreatments(List<Treatment> pastTreatments) {
        this.pastTreatments = pastTreatments;
    }

    public void addDiagnosis(Diagnosis diagnosis) {
        this.pastDiagnoses.add(diagnosis);
    }

    public void addTreatment(Treatment treatment) {
        this.pastTreatments.add(treatment);
    }
}