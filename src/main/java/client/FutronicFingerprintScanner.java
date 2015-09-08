package client;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

        String dirName = "fingerprints";
        ArrayList<byte[]> images = new ArrayList<byte[]>();
        File dir = new File(dirName);
        dir.mkdir();

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

                    Process process = new ProcessBuilder("programs/FingerprintScanner/ftrScanAPI_Ex-0").start();
                    InputStream processStream = process.getInputStream();
                    InputStreamReader processStreamReader = new InputStreamReader(processStream);
                    BufferedReader reader = new BufferedReader(processStreamReader);
                    String output;



                    while ((output = reader.readLine()) != null)
                    {
                        System.out.println("This is the ouput: " + output);
                        if(output.contains("Fingerprint image is written to file:"))
                        {
                            Path path = Paths.get(dirName+"/frame_Ex.bmp");
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
                    }

                }
            }

        }

        dir.delete();

        return null;
    }


}
