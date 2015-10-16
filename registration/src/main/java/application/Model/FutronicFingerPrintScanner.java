package application.Model;

import com.Futronic.ScanApiHelper.Scanner;
import modules.FingerPrintScannerInterface;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * {@author Szymon}
 */
public class FutronicFingerPrintScanner implements FingerPrintScannerInterface {
    @Override
    public byte[] getImage() throws Exception {

        byte[] image = null;
        boolean emptyFrameOrMovableFinger = true;
        boolean scannerError = false;
        String error = "";
        int imageWidth, imageHeight;
        String strInfo;
        Scanner scanner = new Scanner();

        while(emptyFrameOrMovableFinger)
        {

            if( !scanner.OpenDevice() )
            {
                error = "Device is not connected";
                scannerError = true;
                break;
            }
            strInfo = scanner.GetVersionInfo();
            if( strInfo == null)
            {
                error = scanner.GetErrorMessage();
                scannerError = true;
                break;
            }
            if( !scanner.GetImageSize() )
            {
                error = scanner.GetErrorMessage();
                scannerError = true;
                break;
            }
            imageWidth = scanner.GetImageWidth();
            imageHeight = scanner.GetImaegHeight();

            if( !scanner.SetOptions(scanner.FTR_OPTIONS_CHECK_FAKE_REPLICA, 0))
            {
                error = scanner.GetErrorMessage();
                scannerError = true;
                scanner.CloseDevice();
                break;
            }
            if( !scanner.SetOptions(scanner.FTR_OPTIONS_INVERT_IMAGE, 0))
            {
                error = scanner.GetErrorMessage();
                scannerError = true;
                scanner.CloseDevice();
                break;
            }

            byte[] frame = new byte[imageWidth*imageHeight];
            if( !scanner.GetFrame(frame) )
            {
                error = scanner.GetErrorMessage();
                if(error.toLowerCase().contains("empty frame") || error.toLowerCase().contains("moveable finger"))
                {
                }
                else
                {
                    scannerError = true;
                    scanner.CloseDevice();
                    break;
                }
            }
            else
            {
                emptyFrameOrMovableFinger = false;
                scanner.CloseDevice();
                if( frame != null  && emptyFrameOrMovableFinger != true)
                {
                    emptyFrameOrMovableFinger = false;
                    BufferedImage bufferedImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_BYTE_GRAY);
                    byte[] imgData = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();
                    DataBuffer db1 = bufferedImage.getRaster().getDataBuffer();
                    System.arraycopy(frame, 0, imgData, 0, db1.getSize());

                    ByteArrayOutputStream output = new ByteArrayOutputStream();
                    try {
                        ImageIO.write(bufferedImage, "bmp", output);
                        image = output.toByteArray();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            }



        }
        if(scannerError)
        {
            throw new Exception("Error with device");
        }

        return image;
    }
}
