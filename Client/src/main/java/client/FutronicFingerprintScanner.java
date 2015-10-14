package client;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Created by simon on 2015-09-06.
 */
public class FutronicFingerprintScanner implements FingerPrintScannerInterface {

    public FutronicFingerprintScanner()
    {
    }

    public byte[] getImage()
    {
        //this can only be tested with futronic device when run on pi. wait can look into windows java as well will do.
        ArrayList<byte[]> images = new ArrayList<byte[]>();
        try
        {
            return captureFrame();
        }
        catch (IOException e)
        {
            //e.printStackTrace();
            System.out.println("An error occured: IOException");
        }

        return null;
    }

    public byte[] captureFrame() throws IOException {
        //do a check for system so

        ArrayList<byte[]> images = new ArrayList<byte[]>();
        byte[] fingerprint = null;
        //int dirName = (int) (Math.random() * 100);
	    String dirName = "fingerprints";
        File dir = new File(dirName);
        dir.mkdir();

        if(!System.getProperty("os.arch").equals("arm"))
        {
        }
        else
        {
            int count = 0;
            boolean scannerError = false;
            while(!scannerError)
            {
                if(count > 0)
                {
                    break;
                }
                else
                {

                    Process process = new ProcessBuilder("programs/FingerprintScanner/ftrScanAPI_Ex-0").start();
                    InputStream processStream = process.getInputStream();
                    InputStreamReader processStreamReader = new InputStreamReader(processStream);
                    BufferedReader reader = new BufferedReader(processStreamReader);
                    String output;



                    while ((output = reader.readLine()) != null)
                    {
                        if(output.contains("Fingerprint image is written to file:"))
                        {
                            Path path = Paths.get(dirName+"/frame_Ex.bmp");
                            File image = path.toFile();
                            if(image.exists())
                            {
                                fingerprint = (Files.readAllBytes(path));
                                image.delete();
                                count++;
                                break;
                            }
                            else
                            {

                            }

                        }
                        else if (output.contains("Failed to open device!"))
                        {
                            scannerError = true;
                            break;
                        }
                        else if(output.contains("Failed to get image size"))
                        {
                            System.out.println("its failing here");
                                        scannerError = true;
                                        break;
                        }
                        else if(output.contains("Failed to write to file"))
                        {
                            scannerError = true;
                            break;
                        }
                    }
                }
            }
        }

        if(dir.exists())
        {

            dir.delete();
        }
        return fingerprint;
    }


}
