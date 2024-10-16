package items;

/**
 * The ContactInformation class holds contact details for a user.
 */
public class ContactInformation {
    private String phoneNumber;
    private String emailAddress;

    /**
     * Constructor for ContactInformation.
     *
     * @param phoneNumber  Phone number of the user
     * @param emailAddress Email address of the user
     */
    public ContactInformation(String phoneNumber, String emailAddress) {
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }

    // Getters and Setters
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) { 
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) { 
        this.emailAddress = emailAddress;
    }

    @Override
    public String toString() {
        return "ContactInformation{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                '}';
    }
}
