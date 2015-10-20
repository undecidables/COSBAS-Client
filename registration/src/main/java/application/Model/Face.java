package application.Model;

import application.RegistrationApplication;
import modules.Biometric;
import modules.BiometricData;
import modules.OPENCVCamera;
import modules.OPENCVFaceDetection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;

/**
 * {@author Szymon}
 */
public class Face implements Biometric {

    OPENCVCamera cameraObject;
    OPENCVFaceDetection detectFaces;

    @Autowired
    public Face()
    {
        ApplicationContext app = RegistrationApplication.context;
        cameraObject = (OPENCVCamera) app.getBean("camera");
        detectFaces = (OPENCVFaceDetection) app.getBean("faceDetection");
    }

    @Override
    public void fillData(ArrayList<BiometricData> datas, int number) {

        ArrayList<byte[]> images =  new ArrayList<byte[]>();
        for (int i = 0; i < number; i++)
        {
            images.add(cameraObject.getImage());
        }

        images = detectFaces.detectFaces(images);


        if(images != null)
        {
            for(byte[] image : images)
            {
                if(image!=null) {
                    BiometricData data = new BiometricData("biometric-FACE", image);
                    datas.add(data);
                }
            }
        }

        //when done using the camera, release it...
        cameraObject.releaseCamera();
    }
}
