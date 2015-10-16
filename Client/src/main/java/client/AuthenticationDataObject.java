package client;

import modules.BiometricData;

import java.util.ArrayList;

/**
 * {@author Szymon}
 */
public class AuthenticationDataObject {
    private ArrayList<BiometricData> biometricData;
    private String doorID;
    private String action;

    AuthenticationDataObject(String doorID, String action)
    {
        biometricData = new ArrayList<>();
        this.doorID = doorID;
        this.action = action;
    }

    AuthenticationDataObject()
    {
        biometricData = new ArrayList<>();
        doorID = "";
        action = "";
    }



    public void setBiometricData(ArrayList<BiometricData> biometricData)
    {
        this.biometricData = biometricData;
    }

    public void setDoorID(String doorID)
    {
        this.doorID = doorID;
    }

    public void setAction(String action)
    {
        this.action = action;
    }

    public ArrayList<BiometricData> getBiometricData()
    {
        return biometricData;
    }

    public String getDoorID()
    {
        return doorID;
    }

    public String getAction()
    {
        return action;
    }

}
