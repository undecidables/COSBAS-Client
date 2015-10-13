package client;

import modules.Biometric;
import modules.BiometricData;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;

/**
 * {@author Szymon}
 */
public class Fingerprint implements Biometric {

    /**
     *
     * @param _data takes in an arraylist of BiometricData and will add fingerprint data to it
     */

    ApplicationContext context;
    FutronicFingerprintScanner fingerprintScanner;

    public Fingerprint()
    {
        context = Client.context;
        fingerprintScanner = (FutronicFingerprintScanner) context.getBean("fingerPrintScanner");
    }

    public void fillData(ArrayList<BiometricData> _data)
    {
        //FutronicFingerprintScanner fingerprintScanner = new FutronicFingerprintScanner();

        //make it loop a few times
        ArrayList<byte[]> images = fingerprintScanner.getImages();

        for(byte[] image : images)
        {
            _data.add(new BiometricData("finger", image));
        }
    }

    /**
     * This method will capture data from IO device and store somewhere on the client
     */



}
