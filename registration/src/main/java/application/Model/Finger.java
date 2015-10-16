package application.Model;

import application.RegistrationApplication;
import modules.Biometric;
import modules.BiometricData;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;

/**
 * {@author Szymon}
 */
public class Finger implements Biometric {

    ApplicationContext context;
    FutronicFingerPrintScanner scanner;

    public Finger()
    {
        context = RegistrationApplication.context;
        scanner = (FutronicFingerPrintScanner) context.getBean("fingerPrintScan");

    }


    @Override
    public void fillData(ArrayList<BiometricData> _data, int _number) {
        ArrayList<byte[]> images = new ArrayList<>();

        for(int i = 0; i < _number; i++)
        {
            try {
                images.add(scanner.getImage());

            } catch (Exception e) {
                break;
            }
        }

        for (byte[] image : images)
        {
            if(image!=null) {
                BiometricData data = new BiometricData("biometric-FINGER", image);
                _data.add(data);
            }
        }


    }
}
