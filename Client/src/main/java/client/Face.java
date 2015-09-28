package client;

import java.util.ArrayList;

/**
 * {@author Szymon}
 */
public class Face implements Biometric
{


    public void fillData(ArrayList<BiometricData> datas)
    {
        OPENCVFaceDetection detectFaces = new OPENCVFaceDetection();

        OPENCVCamera c = new OPENCVCamera();
        ArrayList<byte[]> images = c.getImages();
        images = detectFaces.detectFaces(images);


        if(images != null)
        {
            for(byte[] image : images)
            {
                BiometricData data = new BiometricData("face", image);
                datas.add(data);

            }
        }
    }


}
