package client;

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
        c.startCamera();
        for(int i = 0; i < 3; i++)
        {
            images.add(c.getImage());
        }
        c.releaseCamera();
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