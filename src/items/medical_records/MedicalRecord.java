package items.medical_records;

import items.Prescription;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * MedicalRecord
 * Holds comprehensive medical information about a patient.
 *
 * The MedicalRecord class stores details about a patient's personal and medical information,
 * including diagnoses, treatments, and contact information.
 */
public class MedicalRecord {
    private String patientID; /**< Unique identifier for the patient */
    private String name; /**< Full name of the patient */
    private LocalDate dateOfBirth; /**< Date of birth of the patient */
    private String gender; /**< Gender of the patient */
    private ContactInformation contactInformation; /**< Contact information for the patient */
    private String bloodType; /**< Blood type of the patient */
    private List<Diagnosis> pastDiagnoses; /**< List of past diagnoses */
    private List<Treatment> pastTreatments; /**< List of past treatments */

    /****************
     * Constructors *
     ****************/

    /**
     * Constructs a MedicalRecord with specified basic details.
     *
     * @param patientID          Unique identifier for the patient.
     * @param name               Full name of the patient.
     * @param dateOfBirth        Date of birth.
     * @param gender             Gender of the patient.
     * @param contactInformation Contact information (phone number, email).
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
     * Constructs a MedicalRecord with additional fields.
     *
     * @param patientID          Unique identifier for the patient.
     * @param name               Full name of the patient.
     * @param dateOfBirth        Date of birth.
     * @param gender             Gender of the patient.
     * @param contactInformation Contact information (phone number, email).
     * @param bloodType          Blood type of the patient.
     * @param pastDiagnoses      List of past diagnoses.
     * @param pastTreatments     List of past treatments.
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

    /***********************
     * Getters and Setters *
     ***********************/

    /**
     * Gets the patient's unique identifier.
     * @return The patient's unique identifier.
     */
    public String getPatientID() {
        return patientID;
    }

    /**
     * Sets the patient's unique identifier.
     * @param patientID The new unique identifier for the patient.
     */
    public void setPatientID(String patientID) { 
        this.patientID = patientID;
    }

    /**
     * Gets the patient's full name.
     * @return The patient's full name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the patient's full name.
     * @param name The new full name of the patient.
     */
    public void setName(String name) { 
        this.name = name;
    }

    /**
     * Gets the patient's date of birth.
     * @return The date of birth.
     */
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets the patient's date of birth.
     * @param dateOfBirth The new date of birth.
     */
    public void setDateOfBirth(LocalDate dateOfBirth) { 
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Gets the patient's gender.
     * @return The gender of the patient.
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the patient's gender.
     * @param gender The new gender of the patient.
     */
    public void setGender(String gender) { 
        this.gender = gender;
    }

    /**
     * Gets the patient's contact information.
     * @return The contact information.
     */
    public ContactInformation getContactInformation() {
        return contactInformation;
    }

    /**
     * Sets the patient's contact information.
     * @param contactInformation The new contact information.
     */
    public void setContactInformation(ContactInformation contactInformation) { 
        this.contactInformation = contactInformation;
    }

    /**
     * Gets the patient's blood type.
     * @return The blood type.
     */
    public String getBloodType() {
        return bloodType;
    }

    /**
     * Sets the patient's blood type.
     * @param bloodType The new blood type.
     */
    public void setBloodType(String bloodType) { 
        this.bloodType = bloodType;
    }

    /**
     * Gets the list of past diagnoses.
     * @return List of past diagnoses.
     */
    public List<Diagnosis> getPastDiagnoses() {
        return pastDiagnoses;
    }

    /**
     * Adds a diagnosis to the patient's medical record.
     * @param diagnosis Diagnosis object to add.
     */
    public void addDiagnosis(Diagnosis diagnosis) {
        this.pastDiagnoses.add(diagnosis);
    }

    /**
     * Gets the list of past treatments.
     * @return List of past treatments.
     */
    public List<Treatment> getPastTreatments() {
        return pastTreatments;
    }

    /**
     * Adds a treatment to the medical record.
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
     *
     * This method assumes each prescription is part of a treatment.
     * If no treatments exist, a new treatment is created.
     *
     * @param prescription Prescription object to add.
     */
    public void addPrescription(Prescription prescription) {
        if (this.pastTreatments.isEmpty()) {
            Treatment treatment = new Treatment();
            treatment.addPrescription(prescription);
            this.pastTreatments.add(treatment);
        } else {
            this.pastTreatments.get(this.pastTreatments.size() - 1).addPrescription(prescription);
        }
    }
    
    /**********
     * Methods *
     **********/

    /**
     * Displays the complete medical record details.
     *
     * This method prints out all patient information, past diagnoses, and treatments.
     */
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

        System.out.println("Past Diagnoses:");
        if (pastDiagnoses == null || pastDiagnoses.isEmpty()) {
            System.out.println("  - No past diagnoses recorded.");
        } else {
            for (Diagnosis diag : pastDiagnoses) {
                System.out.println("  - " + diag.getDescription() + " (Date: " + diag.getDate().format(dateFormatter) + ")");
            }
        }
        System.out.println();

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

    /**
     * Returns a string representation of the medical record.
     * @return A string containing the patient's information, diagnoses, and treatments.
     */
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
