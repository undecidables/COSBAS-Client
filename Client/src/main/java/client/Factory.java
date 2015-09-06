package client;

import java.util.ArrayList;

/**
 * Created by POLSKA on 07/07/2015.
 */
public class Factory {
    public ArrayList<BiometricData> produce()
    {
        Biometric face = new Face();
        Biometric finger = new Fingerprint();
        ArrayList<BiometricData> data = new ArrayList<BiometricData>();

        face.fillData(data);
        finger.fillData(data);

        return data;
    }
}
