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

    private static final String CONFIG_FILE_NAME = "config.properties";
    private PropertiesConfiguration config = new PropertiesConfiguration();

    public FutronicFingerprintScanner()
    {
        try {
            config.load(CONFIG_FILE_NAME);
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<byte[]> getImages()
    {
        //this can only be tested with futronic device when run on pi. wait can look into windows java as well will do.
        System.out.println("Call to getimages");
        ArrayList<byte[]> images = new ArrayList<byte[]>();
        try
        {
            images = captureFrames();
        }
        catch (IOException e)
        {
            //e.printStackTrace();
            System.out.println("An error occured: IOException");
        }

        return images;
    }

    public ArrayList<byte[]> captureFrames() throws IOException {
        //do a check for system so

        ArrayList<byte[]> images = new ArrayList<byte[]>();
        int dirName = (int) (Math.random() * 100);
        File dir = new File(dirName + "");
        dir.mkdir();

        if(!System.getProperty("os.arch").equals("arm"))
        {
            //not runnong on arm/pi, just grab an image from folder
        }
        else
        {
            int count = 0;
            boolean scannerError = false;
            while(!scannerError)
            {
                if(count > 2)
                {
                    break;
                }
                else
                {

                    Process process = new ProcessBuilder(config.getProperty("fingerprintScannerProgam").toString()).start();
                    InputStream processStream = process.getInputStream();
                    InputStreamReader processStreamReader = new InputStreamReader(processStream);
                    BufferedReader reader = new BufferedReader(processStreamReader);
                    String output;



                    while ((output = reader.readLine()) != null)
                    {
                        System.out.println("This is the ouput: " + output);
                        if(output.contains("Fingerprint image is written to file:"))
                        {
                            Path path = Paths.get(dirName+"/"+config.getProperty("fingerPrintOutput"));
                            File image = path.toFile();
                            if(image.exists())
                            {
                                images.add(Files.readAllBytes(path));
                                image.delete();
                                count++;
                                break;
                            }
                            else
                            {

                            }

                        }
                        else if (output.contains("Failed to open device!") || output.contains("Failed to get image size"))
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
        return images;
    }


}
