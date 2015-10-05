package application;

/**
 * Created by Tienie on 9/23/2015.
 */
public class RegistrationDataObject {
    private String empID;
    private String email;
    private String registratorsID;

    public RegistrationDataObject() {

    }

    public String getRegistratorsID()
    {
        return registratorsID;
    }

    public void setRegistratorsID(String registratorsID)
    {
        this.registratorsID = registratorsID;
    }

    public String getEmplid() {
        return empID;
    }

    public void setEmplid(String emp) {
        empID = emp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String mail) {
        email = mail;
    }
}
