package items;

import java.time.LocalDate;
import java.util.List;

public class MedicalRecord {
    private String patientID;
    private String name;
    private LocalDate dateOfBirth;
    private String gender;
    private ContactInformation contactInformation;
    private String bloodType;
    private List<Diagnosis> pastDiagnoses;
    private List<Treatment> pastTreatments;


    public MedicalRecord(String patientID, String name, LocalDate dateOfBirth, String gender, ContactInformation contactInformation) {
        this.patientID = patientID;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.contactInformation = contactInformation;
        this.bloodType = null; // Default value
        this.pastDiagnoses = null; // Default value
        this.pastTreatments = null; // Default value
    }

    public MedicalRecord(String patientID, String name, LocalDate dateOfBirth, String gender, ContactInformation contactInformation, String bloodType, List<Diagnosis> pastDiagnoses, List<Treatment> pastTreatments) {
        this.patientID = patientID;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.contactInformation = contactInformation;
        this.bloodType = bloodType;
        this.pastDiagnoses = pastDiagnoses;
        this.pastTreatments = pastTreatments;
    }

    public String getPatientID() {
        return patientID;
    }
    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }


    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }


    public ContactInformation getContactInformation() {
        return contactInformation;
    }
    public void setContactInformation(ContactInformation contactInformation) {
        this.contactInformation = contactInformation;
    }


    public String getBloodType() {
        return bloodType;
    }
    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public List<Diagnosis> getPastDiagnoses() {
        return pastDiagnoses;
    }
    public void AddDiagnoses(List<Diagnosis> pastDiagnoses) {
        this.pastDiagnoses = pastDiagnoses;
    }


    public List<Treatment> getPastTreatments() {
        return pastTreatments;
    }
    public void AddTreatments(List<Treatment> pastTreatments) {
        this.pastTreatments = pastTreatments;
    }
}