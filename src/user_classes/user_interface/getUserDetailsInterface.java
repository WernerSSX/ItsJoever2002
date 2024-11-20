package user_classes.user_interface;

import java.time.LocalDate;

/*
 * getInterface
 * 
 * Getters for User interface
 */
public interface getUserDetailsInterface {
    public String getRole();
    public String getAge();
    public String getHospitalID();
    public String getPassword();
    public String getName();
    public LocalDate getDateOfBirth();
    public String getGender();
    
}
