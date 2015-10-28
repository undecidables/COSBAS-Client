package client;

import java.util.ArrayList;
import modules.*;
import org.springframework.context.ApplicationContext;

/**
 * {@author Szymon}
 */
public class Face implements Biometric
{
    private OPENCVFaceDetection detectFaces;
    private OPENCVCamera camera;
    private ApplicationContext context;


    public Face()
    {
        context = Client.context;
        detectFaces = (OPENCVFaceDetection) context.getBean("detectFaces");
        camera = (OPENCVCamera) context.getBean("camera");
    }

    public void fillData(ArrayList<BiometricData> datas, int number)
    {
        LCDDisplay.write("SMILE FOR THE CAMERA");
        ArrayList<byte[]> images =  new ArrayList<byte[]>();
        for(int i = 0; i < number; i++)
        {
            images.add(camera.getImage());
        }
        camera.releaseCamera();
       // images = detectFaces.detectFaces(images);


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