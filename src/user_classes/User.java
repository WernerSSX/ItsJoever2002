package user_classes;
import java.io.IOException;
import java.time.LocalDate;
import items.ContactInformation;
import db.*;

public abstract class User {
    protected String hospitalID;
    protected String password;
    protected String name;
    protected LocalDate dateOfBirth; // Changed to LocalDate
    protected String gender;
    protected ContactInformation contactInformation;
    static int userCount = 0;
    public String role;

    public User(String hospitalID, String password, String name, LocalDate dateOfBirth, String gender, ContactInformation contactInformation) {
        this.hospitalID = hospitalID;
        this.password = password;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.contactInformation = contactInformation;
    }

    public String getRole() {
        return role;
    }

    public String getAge() {
        return role;
    }

    public String getHospitalID() {
        return hospitalID;
    }

    public void setHospitalID(String hospitalID) {
        this.hospitalID = hospitalID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) throws IOException {
        this.password = password;
        TextDB.updateUserPassword(this);
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

    public abstract void login();

    public abstract void changePassword();

    public abstract void logout();
}