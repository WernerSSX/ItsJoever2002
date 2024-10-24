package items;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * The MedicalRecord class holds comprehensive medical information about a patient.
 */
public class MedicalRecord {
    private String patientID;
    private String name;
    private LocalDate dateOfBirth;
    private String gender;
    private ContactInformation contactInformation;
    private String bloodType;
    private List<Diagnosis> pastDiagnoses;
    private List<Treatment> pastTreatments;

    /**
     * Constructor for MedicalRecord.
     *
     * @param patientID          Unique identifier for the patient
     * @param name               Full name of the patient
     * @param dateOfBirth        Date of birth
     * @param gender             Gender of the patient
     * @param contactInformation Contact information (phone number, email)
     */
    public MedicalRecord(String patientID, String name, LocalDate dateOfBirth, String gender,
                        ContactInformation contactInformation) {
        this.patientID = patientID;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.contactInformation = contactInformation;
        this.bloodType = "";
        this.pastDiagnoses = new ArrayList<>();
        this.pastTreatments = new ArrayList<>();
        
    }

    /**
     * Overloaded constructor with additional fields.
     *
     * @param patientID          Unique identifier for the patient
     * @param name               Full name of the patient
     * @param dateOfBirth        Date of birth
     * @param gender             Gender of the patient
     * @param contactInformation Contact information (phone number, email)
     * @param bloodType          Blood type of the patient
     * @param pastDiagnoses      List of past diagnoses
     * @param pastTreatments     List of past treatments
     */
    public MedicalRecord(String patientID, String name, LocalDate dateOfBirth, String gender,
                        ContactInformation contactInformation, String bloodType,
                        List<Diagnosis> pastDiagnoses, List<Treatment> pastTreatments) {
        this.patientID = patientID;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.contactInformation = contactInformation;
        this.bloodType = bloodType;
        this.pastDiagnoses = pastDiagnoses;
        this.pastTreatments = pastTreatments;
    }

    // Getters and Setters

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


    /**
     * Adds a diagnosis to the patient's medical record.
     *
     * @param diagnosis Diagnosis object to add
     */
    public void addDiagnosis(Diagnosis diagnosis) {
        this.pastDiagnoses.add(diagnosis);
    }

    public List<Treatment> getPastTreatments() {
        return pastTreatments;
    }

    /**
     * Adds a treatment to the medical record.
     *
     * @param treatment The Treatment object to add.
     */
    public void addTreatment(Treatment treatment) {
        if (this.pastTreatments == null) {
            this.pastTreatments = new ArrayList<>();
        }
        this.pastTreatments.add(treatment);
    }

    /**
     * Adds a prescription to the patient's medical record.
     * This assumes each prescription is part of a treatment.
     *
     * @param prescription Prescription object to add
     */
    public void addPrescription(Prescription prescription) {
        // Find or create a treatment to add the prescription
        if (this.pastTreatments.isEmpty()) {
            Treatment treatment = new Treatment();
            treatment.addPrescription(prescription);
            this.pastTreatments.add(treatment);
        } else {
            // For simplicity, add to the last treatment
            this.pastTreatments.get(this.pastTreatments.size() - 1).addPrescription(prescription);
        }
    }


    public void display() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.println("--------------------------------------------------");
        System.out.println("               Medical Record");
        System.out.println("--------------------------------------------------");
        System.out.println("Patient ID       : " + patientID);
        System.out.println("Name             : " + name);
        System.out.println("Date of Birth    : " + dateOfBirth.format(dateFormatter));
        System.out.println("Gender           : " + gender);
        System.out.println("Contact Information:");
        System.out.println("  Phone Number   : " + contactInformation.getPhoneNumber());
        System.out.println("  Email Address  : " + contactInformation.getEmailAddress());
        System.out.println("Blood Type       : " + (bloodType != null ? bloodType : "N/A"));
        System.out.println();

        // Display Past Diagnoses
        System.out.println("Past Diagnoses:");
        if (pastDiagnoses == null || pastDiagnoses.isEmpty()) {
            System.out.println("  - No past diagnoses recorded.");
        } else {
            for (Diagnosis diag : pastDiagnoses) {
                System.out.println("  - " + diag.getDescription() + " (Date: " + diag.getDate().format(dateFormatter) + ")");
            }
        }
        System.out.println();

        // Display Past Treatments
        System.out.println("Past Treatments:");
        if (pastTreatments == null || pastTreatments.isEmpty()) {
            System.out.println("  - No past treatments recorded.");
        } else {
            for (Treatment treat : pastTreatments) {
                treat.display();
            }
        }
        System.out.println();
    }


    @Override
    public String toString() {
        return "MedicalRecord{" +
                "patientID='" + patientID + '\'' +
                ", name='" + name + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", gender='" + gender + '\'' +
                ", contactInformation=" + contactInformation +
                ", bloodType='" + bloodType + '\'' +
                ", pastDiagnoses=" + pastDiagnoses +
                ", pastTreatments=" + pastTreatments +
                '}';
    }

    // Additional methods can be added here as needed
}
