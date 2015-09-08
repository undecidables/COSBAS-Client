package client;

import java.util.ArrayList;

/**
 * {@author Szymon}
 */
public class Fingerprint implements Biometric {

    /**
     *
     * @param _data takes in an arraylist of BiometricData and will add fingerprint data to it
     */
    public void fillData(ArrayList<BiometricData> _data)
    {
        FingerPrintScannerInterface fingerprintScanner = new FutronicFingerprintScanner();
        ArrayList<byte[]> images = new ArrayList<byte[]>();
        images = fingerprintScanner.getImages();

        for(byte[] image : images)
        {
            _data.add(new BiometricData("finger", image));
        }
    }

    /**
     * This method will capture data from IO device and store somewhere on the client
     */



}
