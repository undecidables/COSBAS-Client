package client;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import modules.*;

/**
 * {@author Szymon}
 */
public class Face implements Biometric
{


    public void fillData(ArrayList<BiometricData> datas)
    {
        OPENCVFaceDetection detectFaces = new OPENCVFaceDetection();

        OPENCVCamera c = new OPENCVCamera();
        ArrayList<byte[]> images =  new ArrayList<byte[]>();
        images.add(c.getImage());
        images = detectFaces.detectFaces(images);


        if(images != null)
        {
            for(byte[] image : images)
            {
                BiometricData data = new BiometricData("biometric-FACE", image);
                datas.add(data);
            }
        }
    }


}
