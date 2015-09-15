package client;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * {@author Szymon}
 */
public class Factory {
    public ArrayList<BiometricData> produce() throws IOException {
        Biometric face = new Face();
        Biometric finger = new Fingerprint();
        ArrayList<BiometricData> data = new ArrayList<BiometricData>();

        face.fillData(data);
        finger.fillData(data);

        return data;
    }
}
