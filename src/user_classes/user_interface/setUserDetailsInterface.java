package user_classes.user_interface;

import java.io.IOException;
import java.time.LocalDate;

/*
 * setInterface
 * 
 * Setters for User interface
 */
public interface setUserDetailsInterface {
    public void setHospitalID(String hospitalID);
    public void setPassword(String password) throws IOException;
    public void setName(String name);
    public void setDateOfBirth(LocalDate dateOfBirth);
    public void setGender(String gender);
}
