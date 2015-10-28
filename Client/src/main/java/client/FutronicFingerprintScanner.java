package client;

import modules.FingerPrintScannerInterface;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * {@author Szymon}
 */
public class FutronicFingerprintScanner implements FingerPrintScannerInterface {

    public FutronicFingerprintScanner()
    {
    }

    public byte[] getImage()
    {
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

        byte[] fingerprint = null;

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
                if(count > 0)//fix this
                {
                    break;
                }
                else
                {

                    LCDDisplay.write("PRESENT FINGER");

                    Process process = new ProcessBuilder("programs/FingerprintScanner/ftrScanAPI_Ex-0").start();
                    InputStream processStream = process.getInputStream();
                    InputStreamReader processStreamReader = new InputStreamReader(processStream);
                    BufferedReader reader = new BufferedReader(processStreamReader);
                    String output;




                    while ((output = reader.readLine()) != null)
                    {
                        System.out.println(output);
                        if(output.contains("Image size is"))
                        {

                        }
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
                            LCDDisplay.write("");
                            scannerError = true;
                            break;
                        }
                        else if(output.contains("Failed to get image size"))
                        {
                            LCDDisplay.write("");
                            System.out.println("its failing here");
                            scannerError = true;
                            break;
                        }
                        else if(output.contains("Failed to write to file"))
                        {
                            LCDDisplay.write("");
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
