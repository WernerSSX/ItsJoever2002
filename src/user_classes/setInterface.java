package user_classes;

import java.io.IOException;
import java.time.LocalDate;

public interface setInterface {
    public void setHospitalID(String hospitalID);
    public void setPassword(String password) throws IOException;
    public void setName(String name);
    public void setDateOfBirth(LocalDate dateOfBirth);
    public void setGender(String gender);
}
