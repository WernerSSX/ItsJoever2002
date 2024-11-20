package user_classes;
import db.*;
import java.io.IOException;
import java.time.LocalDate;
import user_classes.user_interface.mainUserInterface;

/**
 * User
 * An abstract base class representing a generic user in the hospital management system.
 * 
 * The User class contains common attributes and methods shared by all user types,
 * such as hospital ID, password, name, date of birth, and gender. Specific user roles
 * (e.g., Patient, Doctor) should extend this class and implement role-specific behavior.
 */
public abstract class User implements mainUserInterface{
    protected String hospitalID;       /**< Unique identifier for the user in the hospital system */
    protected String password;         /**< User's password for authentication */
    protected String name;             /**< User's full name */
    protected LocalDate dateOfBirth;   /**< User's date of birth */
    protected String gender;           /**< User's gender */

    static int userCount = 0;          /**< Static counter for the number of users */
    public String role;                /**< Role of the user (e.g., Patient, Doctor) */

    /**
     * Constructs a User object with the specified attributes.
     *
     * @param hospitalID  Unique identifier for the user
     * @param password    Password for authentication
     * @param name        Full name of the user
     * @param dateOfBirth Date of birth of the user
     * @param gender      Gender of the user
     */
    public User(String hospitalID, String password, String name, LocalDate dateOfBirth, String gender) {
        this.hospitalID = hospitalID;
        this.password = password;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
    }

    /**
     * Gets the user's role.
     * 
     * @return Role of the user as a string.
     */
    public String getRole() {
        return role;
    }

    /**
     * Gets the user's age.
     * 
     * @return Age of the user as a string.
     */
    public String getAge() {
        return role;
    }

    /**
     * Gets the hospital ID of the user.
     * 
     * @return Hospital ID as a string.
     */
    public String getHospitalID() {
        return hospitalID;
    }

    /**
     * Sets the hospital ID of the user.
     * 
     * @param hospitalID New hospital ID to set.
     */
    public void setHospitalID(String hospitalID) {
        this.hospitalID = hospitalID;
    }

    /**
     * Gets the user's password.
     * 
     * @return Password as a string.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user's password and updates it in the database.
     * 
     * @param password New password to set.
     * @throws IOException if an I/O error occurs while updating the password in the database.
     */
    public void setPassword(String password) throws IOException {
        this.password = password;
        TextDB.updateUserPassword(this);
    }

    /**
     * Gets the user's full name.
     * 
     * @return Name of the user as a string.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the user's full name.
     * 
     * @param name New name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the user's date of birth.
     * 
     * @return Date of birth as a LocalDate object.
     */
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets the user's date of birth.
     * 
     * @param dateOfBirth New date of birth to set.
     */
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Gets the user's gender.
     * 
     * @return Gender of the user as a string.
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the user's gender.
     * 
     * @param gender New gender to set.
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Abstract method for user login. To be implemented by subclasses.
     */
    public abstract void login();

    /**
     * Abstract method for changing user password. To be implemented by subclasses.
     */
    public abstract void changePassword();

    /**
     * Abstract method for user logout. To be implemented by subclasses.
     */
    public abstract void logout();
}
