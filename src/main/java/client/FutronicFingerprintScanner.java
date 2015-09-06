package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by simon on 2015-09-06.
 */
public class FutronicFingerprintScanner implements FingerPrintScannerInterface {

    public ArrayList<byte[]> getImages()
    {
        //this can only be tested with futronic device when run on pi. wait can look into windows java as well will do.
        System.out.println("Call to getimages");
        try {
            ArrayList<byte[]> images = captureFrames();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ArrayList<byte[]> captureFrames() throws IOException {
        //do a check for system so
        if(!System.getProperty("os.arch").equals("arm"))
        {
            //not runnong on arm/pi, just grab an image from folder
        }
        else
        {
            int count = 0;
            while(true)
            {
                if(count > 2) {
                    break;
                }
                else {
                    count++;
                    Process process = new ProcessBuilder("programs/FingerprintScanner/ftrScanAPI_Ex-0").start();
                    InputStream processStream = process.getInputStream();
                    InputStreamReader processStreamReader = new InputStreamReader(processStream);
                    BufferedReader reader = new BufferedReader(processStreamReader);
                    String output;

                    while ((output = reader.readLine()) != null)
                    {
                        System.out.println("This is the ouput: " + output);
                    }

                }
            }

        }



        return null;
    }


}
