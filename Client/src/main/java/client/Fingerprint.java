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

    public void fillData(ArrayList<BiometricData> _data, int number)
    {
        ArrayList<byte[]> images = new ArrayList<byte[]>();

        for(int i = 0; i < number; i++)
        {
            byte[] image = fingerprintScanner.getImage();
            if(image!=null)
            {
                images.add(image);
            }

        }
        for(byte[] image : images)
        {
            _data.add(new BiometricData("biometric-FINGER", image));
        }
    }

}
