package items.medical_records;

/**
 * ContactInformation
 * Holds contact details for a user.
 *
 * The ContactInformation class stores the phone number and email address 
 * associated with a user. It provides methods to access and modify these 
 * contact details.
 */
public class ContactInformation {
    private String phoneNumber;    /**< Phone number of the user */
    private String emailAddress;   /**< Email address of the user */

    /****************
     * Constructors *
     ****************/

    /**
     * Constructs a ContactInformation instance with the specified phone number and email address.
     *
     * @param phoneNumber  The phone number of the user.
     * @param emailAddress The email address of the user.
     */
    public ContactInformation(String phoneNumber, String emailAddress) {
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }

    /***********************
     * Getters and Setters *
     ***********************/

    /**
     * Gets the phone number of the user.
     * @return The user's phone number.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phone number of the user.
     * @param phoneNumber The new phone number of the user.
     */
    public void setPhoneNumber(String phoneNumber) { 
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the email address of the user.
     * @return The user's email address.
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Sets the email address of the user.
     * @param emailAddress The new email address of the user.
     */
    public void setEmailAddress(String emailAddress) { 
        this.emailAddress = emailAddress;
    }

    /**********
     * Methods *
     **********/
    
    /**
     * Returns a string representation of the contact information.
     * @return A string containing the phone number and email address.
     */
    @Override
    public String toString() {
        return "ContactInformation{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                '}';
    }
}
