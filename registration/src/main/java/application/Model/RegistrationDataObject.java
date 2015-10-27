package application.Model;

import modules.BiometricData;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

/**
 * {@author Tienie}
 */
public class RegistrationDataObject {
    private String empID;
    private String email;
    private String registratorID;
    private ArrayList<BiometricData> biometricData;
    private ArrayList<ContactDetail> contactDetails;

    @Autowired
    public RegistrationDataObject() {
        biometricData = new ArrayList<BiometricData>();
        contactDetails = new ArrayList<ContactDetail>();

    }

    public void setContactDetails(ArrayList<ContactDetail> contactDetails)
    {
        this.contactDetails = contactDetails;
    }

    public void addContactDetails(ContactDetail contactDetail)
    {
        this.contactDetails.add(contactDetail);
    }

    public ArrayList<ContactDetail> getContactDetails()
    {
        return contactDetails;
    }

    public void setBiometricData(ArrayList<BiometricData> biometricData)
    {
        this.biometricData = biometricData;
    }

    public ArrayList<BiometricData> getBiometricData(){
        return biometricData;
    }

    public String getRegistratorID()
    {
        return registratorID;
    }

    public void setRegistratorID(String registratorID)
    {
        this.registratorID = registratorID;
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
