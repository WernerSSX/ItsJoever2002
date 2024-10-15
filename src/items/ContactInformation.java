package items;

public class ContactInformation {
    private String phoneNumber;
    private String emailAddress;

    public ContactInformation(String phoneNumber, String emailAddress) {
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }

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

    public void updateContactInfo(String newPhoneNumber, String newEmailAddress) {
        setPhoneNumber(newPhoneNumber);
        setEmailAddress(newEmailAddress);
    }
}
