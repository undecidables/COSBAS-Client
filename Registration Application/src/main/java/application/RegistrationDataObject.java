package application;

/**
 * Created by Tienie on 9/23/2015.
 */
public class RegistrationDataObject {
    private String EMPLID;
    private String Email;

    public RegistrationDataObject() {

    }

    public String getEmplid() {
        return EMPLID;
    }

    public void setEmplid(String emp) {
        EMPLID = emp;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String mail) {
        Email = mail;
    }
}
